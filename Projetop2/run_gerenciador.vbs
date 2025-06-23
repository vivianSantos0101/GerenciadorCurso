Set objShell = CreateObject("WScript.Shell")
Set objFSO = CreateObject("Scripting.FileSystemObject")

' Get the current directory
strPath = objFSO.GetParentFolderName(WScript.ScriptFullName)
jarPath = strPath & "\target\Projetop2-1.0-SNAPSHOT.jar"

' Check if JAR file exists
If objFSO.FileExists(jarPath) Then
    ' Run the JAR file
    objShell.Run "java -jar """ & jarPath & """", 1, False
Else
    MsgBox "JAR file not found: " & jarPath, vbCritical, "Error"
End If 