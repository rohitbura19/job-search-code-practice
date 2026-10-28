' Starts watcher.ps1 with no window. Optional argument: the file to watch.
Set fso = CreateObject("Scripting.FileSystemObject")
dir = fso.GetParentFolderName(WScript.ScriptFullName)
cmd = "powershell.exe -NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File """ & dir & "\watcher.ps1"""
If WScript.Arguments.Count > 0 Then cmd = cmd & " -TriggerFile """ & WScript.Arguments(0) & """"
CreateObject("WScript.Shell").Run cmd, 0, False
