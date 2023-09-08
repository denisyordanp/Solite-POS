plugins {
    id("com.android.application") version AppPlugins.androidApplication apply false
    id("com.android.library") version AppPlugins.androidLibrary apply false
    id("com.google.dagger.hilt.android") version AppPlugins.hilt apply false
    id("org.jetbrains.kotlin.android") version AppPlugins.kotlin apply false
    id("com.google.devtools.ksp") version AppPlugins.ksp apply false
    id("com.google.gms.google-services") version AppPlugins.googleService apply false
    id("com.google.firebase.crashlytics") version AppPlugins.firebaseCrashlitics apply false
    id("com.starter.easylauncher") version AppPlugins.easyLauncher apply false
    id("org.sonarqube") version AppPlugins.sonarQube apply true
}

allprojects {
    repositories {
        google()
        mavenCentral()

		maven { url = uri("https://jitpack.io") }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "solite_pos")
        property("sonar.organization", "solite")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.androidLint.reportPaths", "${project.buildDir}/reports/lint-results-debug.xml")
    }
}

tasks.create<Delete>("clean") {
    delete {
        rootProject.buildDir
    }
}
