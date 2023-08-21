buildscript {
    dependencies {
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.0.0.2929")
    }
}

plugins {
    id("com.android.application") version "7.3.0" apply false
    id("com.android.library") version "7.3.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.android") version "1.7.10" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.2" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()

		maven { url = uri("https://jitpack.io") }
    }
}

tasks.create<Delete>("clean") {
    delete {
        rootProject.buildDir
    }
}
