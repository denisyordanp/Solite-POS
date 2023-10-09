plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.socialite.feature.customerorder"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.kotlinCompilerExtensionVersion
    }
    buildFeatures{
        compose = true
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
    implementation(project(":domain"))
    implementation(project(":common:ui"))
    implementation(project(":common:utility"))
    implementation(project(":core:ui"))

    // Compose
    implementation(platform("androidx.compose:compose-bom:${Depedencies.composeBomVersion}"))
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material")

    implementation("androidx.constraintlayout:constraintlayout-compose:${Depedencies.constraintComposeVersion}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Depedencies.hiltVersion}")
    implementation("androidx.hilt:hilt-navigation-compose:${Depedencies.hiltNavigationComposeVersion}")
    kapt("com.google.dagger:hilt-compiler:${Depedencies.hiltVersion}")
}