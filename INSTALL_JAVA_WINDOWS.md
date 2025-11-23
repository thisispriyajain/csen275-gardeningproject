# Installing Java 21 on Windows

## Quick Install Guide

### Option 1: Using Chocolatey (Easiest)

If you have Chocolatey package manager:

```powershell
choco install openjdk21
```

### Option 2: Manual Download (Recommended)

1. **Download Java 21 JDK**
   - Go to: https://www.oracle.com/java/technologies/downloads/#java21
   - OR Eclipse Temurin: https://adoptium.net/temurin/releases/?version=21
   - Choose "Windows" and "x64" installer
   - Download the `.msi` file

2. **Run the Installer**
   - Double-click the downloaded `.msi` file
   - Click "Next" through the installation wizard
   - **IMPORTANT**: Check "Set JAVA_HOME variable" if offered
   - Note the installation path (usually `C:\Program Files\Java\jdk-21`)

3. **Set JAVA_HOME Environment Variable** (if not set automatically)
   
   **Method A: Using GUI**
   - Press `Windows + R`, type `sysdm.cpl`, press Enter
   - Click "Advanced" tab → "Environment Variables"
   - Under "System variables", click "New"
   - Variable name: `JAVA_HOME`
   - Variable value: `C:\Program Files\Java\jdk-21` (your Java install path)
   - Click OK
   - Find "Path" in System variables, click "Edit"
   - Click "New" and add: `%JAVA_HOME%\bin`
   - Click OK on all dialogs

   **Method B: Using PowerShell (Run as Administrator)**
   ```powershell
   # Set JAVA_HOME (adjust path if different)
   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', 'C:\Program Files\Java\jdk-21', [System.EnvironmentVariableTarget]::Machine)
   
   # Add to PATH
   $currentPath = [System.Environment]::GetEnvironmentVariable('Path', [System.EnvironmentVariableTarget]::Machine)
   [System.Environment]::SetEnvironmentVariable('Path', $currentPath + ';%JAVA_HOME%\bin', [System.EnvironmentVariableTarget]::Machine)
   ```

4. **Verify Installation**
   - **CLOSE and REOPEN** PowerShell/Command Prompt
   - Run:
   ```powershell
   java -version
   ```
   - Should show: `java version "21.x.x"`
   - Run:
   ```powershell
   echo $env:JAVA_HOME
   ```
   - Should show: `C:\Program Files\Java\jdk-21` or similar

### Option 3: Using Winget (Windows 11)

```powershell
winget install Microsoft.OpenJDK.21
```

## After Java is Installed

### For This Session Only (Quick Test)

If you want to test immediately without restarting:

```powershell
# Set for current session only
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify
java -version
```

### Build the Smart Garden Project

Once Java is installed and verified:

```powershell
cd D:\smartGarden

# Build the project
.\mvnw.cmd clean install

# Run the application
.\mvnw.cmd javafx:run
```

## Troubleshooting

### "java command not found"
- Make sure you closed and reopened PowerShell after setting JAVA_HOME
- Verify JAVA_HOME is set: `echo $env:JAVA_HOME`
- Verify PATH includes Java: `echo $env:PATH | Select-String "java"`

### "JAVA_HOME not found"
- Verify the path exists: `Test-Path "C:\Program Files\Java\jdk-21"`
- Double-check environment variable spelling: `JAVA_HOME` (all caps)

### Multiple Java Versions
- JAVA_HOME should point to Java 21 specifically
- Check: `& "$env:JAVA_HOME\bin\java" -version`

## Quick Reference

Common Java 21 installation paths:
- Oracle JDK: `C:\Program Files\Java\jdk-21`
- Eclipse Temurin: `C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot`
- Microsoft OpenJDK: `C:\Program Files\Microsoft\jdk-21.x.x`

## Alternative: Use Reference Project's Java

If you can run the reference project (`Group_9_Project_Implementation1`), you already have Java installed! Just need to find it:

```powershell
# Find Java in reference project
cd D:\Group_9_Project_Implementation1
.\mvnw.cmd -version
```

This will show you the Java it's using, then set that as your JAVA_HOME.

