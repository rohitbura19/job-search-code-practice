# Claude discussion watcher, one session at a time.
#
# Start it with start-discussion.cmd. It watches ONE file inside a git repo and checks it every 10 s.
# Each save: whatever you added since the last commit (the git diff) goes to Claude, together with the
# diff of any other files you changed. Claude writes code into the repo and its reply goes into
# <file>.reply.md. Then everything is committed and pushed, and that commit is the baseline for the next save.
# It stops when you add a line "#stop", delete the file, or after IdleStopMinutes without a save.

param([string]$TriggerFile)

$ErrorActionPreference = 'Stop'
$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$cfg  = Get-Content (Join-Path $here 'config.json') -Raw | ConvertFrom-Json

# Low priority for this script and everything it starts (claude, mvn inherit it), so IntelliJ stays responsive
(Get-Process -Id $PID).PriorityClass = 'BelowNormal'
$env:MAVEN_OPTS = $cfg.MavenOpts
# Read git output as UTF-8 (otherwise characters like the rupee sign break the diff)
try { [Console]::OutputEncoding = New-Object Text.UTF8Encoding($false) } catch { }

$dataDir = Join-Path $env:LOCALAPPDATA 'ClaudeWatcher'
New-Item -ItemType Directory -Force -Path $dataDir | Out-Null
$logFile = Join-Path $dataDir 'watcher.log'

function Log([string]$msg) {
    Add-Content -Path $logFile -Value ('{0:yyyy-MM-dd HH:mm:ss}  {1}' -f (Get-Date), $msg) -Encoding UTF8
}

function Notify([string]$text) {
    if (-not $cfg.Notify) { return }
    try {
        Add-Type -AssemblyName System.Windows.Forms
        $n = New-Object System.Windows.Forms.NotifyIcon
        $n.Icon = ([type]'System.Drawing.SystemIcons')::Information
        $n.Visible = $true
        $n.ShowBalloonTip(6000, 'Claude watcher', $text, 'Info')
        Start-Sleep -Seconds 6
        $n.Dispose()
    } catch { }
}

function Invoke-Git([string[]]$gitArgs) {
    # git writes progress to stderr; in Windows PowerShell 5.1 that would throw under 'Stop'
    $ErrorActionPreference = 'Continue'
    $out = & git -C $script:repo @gitArgs 2>&1 | ForEach-Object { "$_" } | Out-String
    return @{ Code = $LASTEXITCODE; Out = $out.TrimEnd() }
}

function Normalize([string]$s) { if ($null -eq $s) { return '' } return ($s -replace "`r`n", "`n").TrimEnd() }

function Read-Trigger {
    for ($i = 0; $i -lt 5; $i++) {
        try { return Normalize (Get-Content $TriggerFile -Raw -Encoding UTF8) } catch { Start-Sleep -Milliseconds 500 }
    }
    return $null
}

function Committed-Trigger {
    $r = Invoke-Git @('show', "HEAD:$script:rel")
    if ($r.Code -eq 0) { return Normalize $r.Out } else { return '' }
}

function Clip([string]$s) {
    $max = [int]$cfg.MaxDiffChars
    if ($s.Length -le $max) { return $s }
    return $s.Substring(0, $max) + "`n... (diff truncated; read the files directly for the rest)"
}

function Commit-And-Push([string]$message, [switch]$KeepTriggerSnapshot) {
    if ($KeepTriggerSnapshot) {
        # The discussion file stays at the version Claude saw; anything you typed during the run is the next round
        $null = Invoke-Git @('add', '-A', '--', '.', ":(exclude)$script:rel")
    } else {
        $null = Invoke-Git @('add', '-A')
    }
    if ((Invoke-Git @('diff', '--cached', '--quiet')).Code -eq 0) { return 'nothing to commit' }
    $c = Invoke-Git @('commit', '-m', $message)
    if ($c.Code -ne 0) { return "commit FAILED: $($c.Out)" }
    if (-not $cfg.Push) { return 'committed (push disabled)' }
    $pull = Invoke-Git @('pull', '--rebase', '--autostash', 'origin', $cfg.Branch)
    if ($pull.Code -ne 0) { $null = Invoke-Git @('rebase', '--abort'); return "committed locally; pull FAILED, not pushed: $($pull.Out)" }
    $p = Invoke-Git @('push', 'origin', "HEAD:$($cfg.Branch)")
    if ($p.Code -ne 0) { return "committed locally; push FAILED: $($p.Out)" }
    return "committed and pushed to $($cfg.Branch)"
}

function Run-Claude([string]$prompt) {
    $stamp      = Get-Date -Format 'yyyyMMdd-HHmmss'
    $promptFile = Join-Path $dataDir "prompt-$stamp.txt"
    $outFile    = Join-Path $dataDir "output-$stamp.txt"
    [IO.File]::WriteAllText($promptFile, $prompt, (New-Object Text.UTF8Encoding($false)))

    # Same Claude conversation for the whole session, so it remembers the earlier rounds
 for ($attempt = 0; $attempt -lt 2; $attempt++) {
    $sessionArg = if ($script:firstRound) { "--session-id $script:sessionId" } else { "--resume $script:sessionId" }
    $cmd = "claude -p $sessionArg --permission-mode acceptEdits --allowedTools `"$($cfg.AllowedTools)`" < `"$promptFile`" > `"$outFile`" 2>&1"
    $p = Start-Process -FilePath 'cmd.exe' -ArgumentList '/c', $cmd -WorkingDirectory $script:repo -WindowStyle Hidden -PassThru
    $null = $p.Handle   # needed so ExitCode is available in Windows PowerShell 5.1
    if (-not $p.WaitForExit([int]$cfg.TimeoutMinutes * 60 * 1000)) {
        & taskkill /T /F /PID $p.Id | Out-Null
        return @{ Ok = $false; Text = "Claude ran longer than $($cfg.TimeoutMinutes) min and was stopped." }
    }
    $text = if (Test-Path $outFile) { Get-Content $outFile -Raw -Encoding UTF8 } else { '' }
    # Session bookkeeping: a failed first run may or may not have created the conversation
    if ($script:firstRound -and $text -match 'already in use') { $script:firstRound = $false; continue }
    if (-not $script:firstRound -and $text -match 'No conversation found') { $script:firstRound = $true; continue }
    if ($p.ExitCode -eq 0) { $script:firstRound = $false }
    Remove-Item $promptFile -ErrorAction SilentlyContinue
    return @{ Ok = ($p.ExitCode -eq 0); Text = (Normalize $text) }
 }
    return @{ Ok = $false; Text = (Normalize $text) }
}

function Run-Round([string]$snapshot) {
    $null = Invoke-Git @('add', '-A')
    $fileDiff  = (Invoke-Git @('diff', '--cached', '--', $script:rel)).Out
    $otherDiff = (Invoke-Git @('diff', '--cached', '--', '.', ":(exclude)$script:rel", ":(exclude)$script:replyRel")).Out

    $added = @($fileDiff -split "`r?`n" | Where-Object { $_.StartsWith('+') -and -not $_.StartsWith('+++') } |
               ForEach-Object { $_.Substring(1) })
    $stopRe = '^\s*#\s*stop\s*$'
    $stop    = [bool]($added | Where-Object { $_ -match $stopRe })
    $newText = (($added | Where-Object { $_ -notmatch $stopRe }) -join "`n").Trim()
    $title   = if ($newText) { ($newText -split "`n")[0] } else { 'update' }
    if ($title.Length -gt 60) { $title = $title.Substring(0, 60) }

    if ($newText -or $otherDiff) {
        $prompt = @"
You are in an ongoing discussion with the user, driven by a file they edit: $script:rel
Repository: $script:repo
Rules:
- Work without asking questions; state any assumptions in your reply.
- Create or edit files only inside this repository. Put code in proper files, not in your reply.
- Do NOT run git, and do NOT edit $script:rel or $script:replyRel. The service appends your reply to $script:replyRel, then commits and pushes.
- If you write Java with tests, run them with mvn and make them pass.
- Keep your reply short: what you did, files changed, test result, then your answer or explanation.

NEW in $script:rel since the last commit (git diff; lines starting with + were added):
$(Clip $fileDiff)

OTHER files the user changed since the last commit:
$(if ($otherDiff) { Clip $otherDiff } else { '(none)' })

The whole discussion is in $script:rel and your earlier replies are in $script:replyRel; read them if you need more context.
"@
        Log "round: $title"
        $res = Run-Claude $prompt
        $header = if ($res.Ok) { '' } else { '**FAILED** ' }
        $entry = "`n## {0:yyyy-MM-dd HH:mm} - {1}{2}`n`n{3}`n" -f (Get-Date), $header, $title, $res.Text
        Add-Content -Path $script:replyFile -Value $entry -Encoding UTF8
    }

    $result = Commit-And-Push ("Discussion: " + $title) -KeepTriggerSnapshot
    Log $result
    $script:baseline = $snapshot
    if ($stop) { return 'stop' }
    Notify "$title - $result"
    return 'ok'
}

# ---------- setup ----------
if (-not $TriggerFile) { $TriggerFile = $cfg.TriggerFile }
$TriggerFile = [IO.Path]::GetFullPath($TriggerFile)
$dir = Split-Path -Parent $TriggerFile
New-Item -ItemType Directory -Force -Path $dir | Out-Null
if (-not (Test-Path $TriggerFile)) { Set-Content -Path $TriggerFile -Value '' -Encoding UTF8 }

$ErrorActionPreference = 'Continue'
$top = (& git -C $dir rev-parse --show-toplevel 2>$null)
$ErrorActionPreference = 'Stop'
if (-not $top) { Log "not inside a git repo: $TriggerFile"; Notify "Not started: $TriggerFile is not inside a git repo."; exit 1 }
$script:repo = ($top.Trim() -replace '/', '\')
if (-not $TriggerFile.StartsWith($script:repo, [StringComparison]::OrdinalIgnoreCase)) { Log 'path mismatch'; exit 1 }
$script:rel       = $TriggerFile.Substring($script:repo.Length).TrimStart('\') -replace '\\', '/'
$script:replyFile = [IO.Path]::ChangeExtension($TriggerFile, '.reply.md')
$script:replyRel  = $script:replyFile.Substring($script:repo.Length).TrimStart('\') -replace '\\', '/'

# One watcher per file
$md5 = [Security.Cryptography.MD5]::Create()
$key = -join ($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($TriggerFile.ToLowerInvariant())) | ForEach-Object { $_.ToString('x2') })
$mutex = New-Object Threading.Mutex($false, "Local\ClaudeWatcher_$key")
if (-not $mutex.WaitOne(0)) { Notify "Already watching $script:rel"; exit 0 }

$script:sessionId  = [guid]::NewGuid().ToString()
$script:firstRound = $true
$script:baseline   = Committed-Trigger
$lastSeenWrite = [DateTime]::MinValue
$lastActivity  = Get-Date
Log "started: $TriggerFile (repo $script:repo)"
Notify "Watching $script:rel. Add '#stop' or delete the file to end."

# ---------- loop ----------
try {
    while ($true) {
        Start-Sleep -Seconds ([int]$cfg.PollSeconds)

        if (-not (Test-Path $TriggerFile)) {
            $r = Commit-And-Push "Discussion ended: $script:rel deleted"
            Log "file deleted; $r; stopping"
            Notify 'Discussion file deleted - watcher stopped.'
            break
        }
        if (((Get-Date) - $lastActivity).TotalMinutes -ge [int]$cfg.IdleStopMinutes) {
            Log 'idle timeout; stopping'
            Notify "No saves for $($cfg.IdleStopMinutes) min - watcher stopped."
            break
        }

        try { $w = (Get-Item $TriggerFile -ErrorAction Stop).LastWriteTimeUtc } catch { continue }
        if ($w -eq $lastSeenWrite) { continue }
        if (([DateTime]::UtcNow - $w).TotalSeconds -lt 3) { continue }   # editor may still be writing
        $lastSeenWrite = $w

        $content = Read-Trigger
        if ($null -eq $content -or $content -eq $script:baseline) { continue }

        $lastActivity = Get-Date
        try {
            if ((Run-Round $content) -eq 'stop') { Log 'stop command; stopping'; Notify 'Got #stop - watcher stopped.'; break }
        } catch { Log "round error: $_" }
    }
} finally {
    $mutex.ReleaseMutex()
    Log 'exited'
}
