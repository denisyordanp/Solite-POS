plugins {
    id("com.android.library")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.socialite.domain"
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
    implementation(project(":data"))
    implementation(project(":common"))

    implementation("androidx.core:core-ktx:${Depedencies.kotlinCoreVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Depedencies.kotlinCoroutineVersion}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Depedencies.hiltVersion}")
    kapt("com.google.dagger:hilt-compiler:${Depedencies.hiltVersion}")

    // Test
    testImplementation("junit:junit:${Depedencies.junitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Depedencies.junitExtVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Depedencies.espressoVersion}")
}