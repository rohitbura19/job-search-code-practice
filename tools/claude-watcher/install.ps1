# Registers the watcher to start hidden at every logon, and starts it now.
# Run once:  powershell -ExecutionPolicy Bypass -File install.ps1
$ErrorActionPreference = 'Stop'
$here = Split-Path -Parent $MyInvocation.MyCommand.Path

foreach ($tool in 'claude', 'git') {
    if (-not (Get-Command $tool -ErrorAction SilentlyContinue)) { throw "'$tool' is not on PATH. Install it first." }
}

$action   = New-ScheduledTaskAction -Execute 'wscript.exe' -Argument "`"$here\start-hidden.vbs`""
$trigger  = New-ScheduledTaskTrigger -AtLogOn -User $env:USERNAME
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries `
            -ExecutionTimeLimit ([TimeSpan]::Zero) -RestartCount 3 -RestartInterval (New-TimeSpan -Minutes 1)
Register-ScheduledTask -TaskName 'ClaudeFileTriggerWatcher' -Action $action -Trigger $trigger `
    -Settings $settings -Description 'Runs Claude Code when the trigger file is saved' -Force | Out-Null

Start-ScheduledTask -TaskName 'ClaudeFileTriggerWatcher'
$cfg = Get-Content "$here\config.json" -Raw | ConvertFrom-Json
Write-Host "Installed and started. Write a request in: $($cfg.TriggerFile)"
Write-Host "Log: $env:LOCALAPPDATA\ClaudeWatcher\watcher.log"
