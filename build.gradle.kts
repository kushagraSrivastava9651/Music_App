// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
   // id("org.jetbrains.kotlin.jvm") version "1.9.10"
}

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        // other repositories if needed
    }
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath ("com.android.tools.build:gradle:4.2.0")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}