# Claude discussion watcher (Windows)

A watcher that runs only while you're working. You start it, save a file to talk to Claude, and stop it with `#stop` or by deleting the file. Nothing runs at logon, and nothing keeps running after a session ends.

## Start

Double-click `start-discussion.cmd`. It watches `discussion\discussion.md` in this repo by default. To watch a different file, run it with that file's path; the file must be inside a git repo:

```
start-discussion.cmd "C:\Users\Rohit Bura\Desktop\Code\job-search-code-practice\discussion\car-oop.md"
```

## Each save

The watcher checks the file every 10 seconds. When the file differs from its last committed version, the save is picked up right away, including the first save. Then:

1. **Diff:** everything in the repo is staged. Claude is given the **diff of the discussion file since the last commit**, which is your new text, plus the **diff of any other files you changed**, such as code you edited for review.
2. **Claude:** Claude does the work, writing code files in the repo and running `mvn` tests. It's the **same Claude conversation for the whole session**, so it remembers the earlier rounds.
3. **Reply:** Claude's reply is added to `discussion.reply.md`, next to your file. Your own file is never changed by the watcher.
4. **Commit and push:** everything is committed and pushed. That commit becomes the baseline the next save is compared against.

## Stop

Any one of these stops the watcher. Afterwards, no watcher process is left running.

- Add a line `#stop` and save. If the same save has other new text, it's handled first; then the watcher commits, pushes and exits.
- Delete the discussion file. The deletion is committed and pushed, and the watcher exits.
- No save for 120 minutes (`IdleStopMinutes`). The watcher exits.
- Emergency: run `stop-all.cmd`. It kills every watcher, including a Claude run in progress.

## Resource use

- **While waiting:** it checks one file's save time every 10 seconds, which uses almost no CPU.
- **While running:** the watcher, Claude and Maven run at **Below Normal** priority, so IntelliJ and anything else you use get the CPU first. Maven's memory is capped with `MAVEN_OPTS=-Xmx768m`.
- Only one watcher runs per file. Starting it twice does nothing.

## Visibility

- **Screen:** there is no window. You only see a Windows notification at start, after each round, and at stop. Set `"Notify": false` to turn them off.
- **Task Manager:** `powershell.exe` shows while the watcher runs, and `claude`/`node` while Claude is working.
- **GitHub:** each round makes a `Discussion: …` commit. If the repo is public, anyone can see the commits and their contents.

## Settings (`config.json`)

| Key | Meaning |
|---|---|
| `TriggerFile` | The default file to watch |
| `PollSeconds` | How often the file is checked (10) |
| `IdleStopMinutes` | Stop after this long with no save (120) |
| `TimeoutMinutes` | Stop a single Claude run that takes longer than this (30) |
| `Branch`, `Push` | The branch to push to; set `Push` to false to commit only |
| `Notify` | Show Windows notifications |
| `MavenOpts` | JVM options for Maven runs started by Claude |
| `AllowedTools` | What Claude may do. Git is left out on purpose; the watcher does the git steps |

Logs and Claude's raw output are in `%LOCALAPPDATA%\ClaudeWatcher\`. You need `claude` (logged in) and `git` on your PATH. Each round uses your Claude plan's usage.
