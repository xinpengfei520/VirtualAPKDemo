#!/usr/bin/env bash
./gradlew clean assemblePlugin
adb push app/build/outputs/apk/app-release.apk /sdcard/plugin.apk
adb shell am force-stop com.xpf.android.virtualapk
adb shell am start -n com.xpf.android.virtualapk/com.xpf.android.virtualapk.MainActivity
