# Build and run instructions

## Prerequisites

Use Windows PowerShell from `E:\cashbook`.
Install Android Studio or Android SDK command-line tools. Create `local.properties` locally with your SDK path, for example `sdk.dir=...`; this file is developer-specific and must not be committed.
The app module uses application ID `com.vmk.cashbook`, compile SDK 29, min SDK 16, and target SDK 29. No product flavors are declared.
The repository uses Gradle wrapper 6.1.1 and Android Gradle Plugin 4.0.0. The build files do not set Java `sourceCompatibility` or `targetCompatibility`; that is separate from the JDK needed to run Gradle.

## Known setup blockers

This machine only has Android Studio's JBR 21. Gradle 6.1.1 does not run on it here; `gradlew tasks` failed with a Groovy `NoClassDefFoundError` while starting Gradle.
The local SDK has platforms 28 and 33 through 36.1, but not platform 29. It also lacks Build Tools 29.0.2. Install Android SDK Platform 29 and Build Tools 29.0.2 before expecting this project to build unchanged.
The root build script still uses `jcenter()`. Since JCenter was shut down, dependency resolution can fail for artifacts that are not available from Google Maven or another configured repository.
`app/google-services.json` is present and the Google Services plugin is applied, so Firebase client configuration is part of the build inputs.

## Build

After installing a compatible JDK and the missing SDK packages, build the debug APK:

```powershell
$env:ANDROID_HOME = "C:\path\to\Android\Sdk"
$env:JAVA_HOME = "C:\path\to\compatible\jdk"
.\gradlew.bat assembleDebug --console=plain
```

Expected debug APK path: `app\build\outputs\apk\debug\app-debug.apk`.

## Run

Start an emulator or connect a device with USB debugging enabled, then install and launch:

```powershell
& "$env:ANDROID_HOME\platform-tools\adb.exe" install -r app\build\outputs\apk\debug\app-debug.apk
& "$env:ANDROID_HOME\platform-tools\adb.exe" shell monkey -p com.vmk.cashbook -c android.intent.category.LAUNCHER 1
```

If more than one device is connected, add `-s SERIAL` after `adb.exe`. The app declares internet access and contains sync, registration, Firebase, and ads integrations; running it may contact hosted services or mutate real remote data.