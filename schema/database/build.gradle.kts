plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.socialite.schema.database"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        create("local") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Depedencies.kotlinCoreVersion}")

    // Database
    implementation("androidx.room:room-runtime:${Depedencies.roomVersion}")
    implementation("androidx.room:room-ktx:${Depedencies.roomVersion}")
    ksp("org.xerial:sqlite-jdbc:${Depedencies.sqliteJdbc}")
    ksp("androidx.room:room-compiler:${Depedencies.roomVersion}")
}