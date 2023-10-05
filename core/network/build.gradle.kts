plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.socialite.core.network"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val properties = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)
    buildTypes {
        // required for placeholder on CI build
        val baseUrlPlaceHolder = "\"placeholder\""
        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", properties.getProperty("RELEASE_BASE_URL") ?: baseUrlPlaceHolder)
        }
        debug {
            buildConfigField("String", "BASE_URL", properties.getProperty("DEVELOP_BASE_URL") ?: baseUrlPlaceHolder)
        }
        create("local") {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", properties.getProperty("LOCAL_BASE_URL") ?: baseUrlPlaceHolder)
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Depedencies.kotlinCoroutineVersion}")

    // Network
    implementation("com.squareup.retrofit2:retrofit:${Depedencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Depedencies.retrofitVersion}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Depedencies.hiltVersion}")
    kapt("com.google.dagger:hilt-compiler:${Depedencies.hiltVersion}")
}