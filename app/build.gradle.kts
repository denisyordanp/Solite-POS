plugins {
    id("com.android.application")
    id("kotlin-android")
	id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android")
    id("org.sonarqube")
}

android {
    compileSdk = AppConfig.compileSdk
    namespace = "com.socialite.solite_pos"

    defaultConfig {
        applicationId = "com.socialite.solite_pos"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = Versions.getVersionCode()
        versionName = Versions.getVersionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = AppConfig.jvmTarget
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AppConfig.kotlinCompilerExtensionVersion
    }
	buildFeatures{
		viewBinding = true
        compose = true
	}
    buildToolsVersion = AppConfig.buildToolsVersion
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "solite_pos")
        property("sonar.organization", "solite")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.core:core-ktx:${Depedencies.kotlinCoreVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Depedencies.kotlinCoroutineVersion}")

    // UI
    implementation("com.google.android.material:material:${Depedencies.materialVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Depedencies.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Depedencies.lifecycleVersion}")
    implementation("androidx.appcompat:appcompat:${Depedencies.appCompatVersion}")
    implementation("androidx.lifecycle:lifecycle-extensions:${Depedencies.lifecycleExtensionVersion}")

    // Compose
    implementation(platform("androidx.compose:compose-bom:${Depedencies.composeBomVersion}"))
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Depedencies.lifecycleVersion}")

    implementation("androidx.activity:activity-compose:${Depedencies.activityComposeVersion}")
    implementation("androidx.constraintlayout:constraintlayout-compose:${Depedencies.constraintComposeVersion}")
    implementation("androidx.navigation:navigation-compose:${Depedencies.navigationComposeVersion}")
    implementation("com.google.accompanist:accompanist-pager:${Depedencies.accompanistPagerVersion}")
    implementation("com.google.accompanist:accompanist-pager-indicators:${Depedencies.accompanistPagerVersion}")
    implementation("com.google.accompanist:accompanist-insets:${Depedencies.accompanistPagerVersion}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Depedencies.firebaseBomVersion}"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Depedencies.hiltVersion}")
    implementation("androidx.hilt:hilt-navigation-compose:${Depedencies.hiltNavigationComposeVersion}")
    kapt("com.google.dagger:hilt-compiler:${Depedencies.hiltVersion}")

    // Play In App Update
    implementation("com.google.android.play:app-update:${Depedencies.googlePlayVersion}")
    implementation("com.google.android.play:app-update-ktx:${Depedencies.googlePlayVersion}")

    // Test
    testImplementation("junit:junit:${Depedencies.junitVersion}")
    androidTestImplementation("androidx.test.ext:junit:${Depedencies.junitExtVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Depedencies.espressoVersion}")
}

kapt {
    correctErrorTypes = true
}
