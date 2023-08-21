plugins {
    id("com.android.library")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.socialite.data"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Depedencies.kotlinCoroutineVersion}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Depedencies.hiltVersion}")
    kapt("com.google.dagger:hilt-compiler:${Depedencies.hiltVersion}")

    // Network
    implementation("com.squareup.retrofit2:retrofit:${Depedencies.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Depedencies.retrofitVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Depedencies.okhttpLoggingVersion}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Depedencies.firebaseBomVersion}"))
    implementation("com.google.firebase:firebase-config-ktx")

    // Database
    api("androidx.room:room-runtime:${Depedencies.roomVersion}")
    api("androidx.room:room-ktx:${Depedencies.roomVersion}")
    kapt("org.xerial:sqlite-jdbc:${Depedencies.sqliteJdbc}")
    kapt("androidx.room:room-compiler:${Depedencies.roomVersion}")

    // Preferences
    implementation("androidx.security:security-crypto-ktx:${Depedencies.securityCryptoVersion}")
    implementation("androidx.datastore:datastore-preferences:${Depedencies.dataStoreVersion}")

    // Test
    testImplementation("junit:junit:${Depedencies.junitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Depedencies.junitExtVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Depedencies.espressoVersion}")
}