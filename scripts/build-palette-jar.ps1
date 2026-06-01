param(
    [string]$JarName = "ShUIComponents.jar"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$srcDir = Join-Path $projectRoot "src"
$buildDir = Join-Path $projectRoot "build"
$classesDir = Join-Path $buildDir "classes"
$distDir = Join-Path $projectRoot "dist"
$sourcesFile = Join-Path $buildDir "sources.txt"
$jarPath = Join-Path $distDir $JarName

New-Item -ItemType Directory -Force -Path $classesDir | Out-Null
New-Item -ItemType Directory -Force -Path $distDir | Out-Null

$resolvedProject = (Resolve-Path -LiteralPath $projectRoot).Path
$resolvedClasses = (Resolve-Path -LiteralPath $classesDir).Path
if (-not $resolvedClasses.StartsWith($resolvedProject, [System.StringComparison]::OrdinalIgnoreCase)) {
    throw "Refusing to clean classes directory outside project: $resolvedClasses"
}

Get-ChildItem -Path $classesDir -Recurse -Force | Remove-Item -Recurse -Force
Get-ChildItem -Path $srcDir -Recurse -File -Include *.java |
        ForEach-Object { $_.FullName } |
        Set-Content -Path $sourcesFile -Encoding ASCII

javac --release 21 -encoding UTF-8 -d $classesDir "@$sourcesFile"

Get-ChildItem -Path $srcDir -Recurse -File |
        Where-Object { $_.Extension -ne ".java" -and $_.Extension -ne ".form" } |
        ForEach-Object {
            $relative = $_.FullName.Substring($srcDir.Length).TrimStart([System.IO.Path]::DirectorySeparatorChar)
            $target = Join-Path $classesDir $relative
            New-Item -ItemType Directory -Force -Path (Split-Path -Parent $target) | Out-Null
            Copy-Item -LiteralPath $_.FullName -Destination $target -Force
        }

if (Test-Path $jarPath) {
    Remove-Item -LiteralPath $jarPath -Force
}

Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::Open($jarPath, [System.IO.Compression.ZipArchiveMode]::Create)
try {
    Get-ChildItem -Path $classesDir -Recurse -File | ForEach-Object {
        $relative = $_.FullName.Substring($classesDir.Length).TrimStart([System.IO.Path]::DirectorySeparatorChar)
        $entryName = $relative.Replace([System.IO.Path]::DirectorySeparatorChar, "/")
        [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, $_.FullName, $entryName) | Out-Null
    }
} finally {
    $zip.Dispose()
}
Remove-Item -LiteralPath $sourcesFile -Force

Write-Output $jarPath
