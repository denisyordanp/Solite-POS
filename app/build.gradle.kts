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
    compileSdk = 33
    namespace = "com.socialite.solite_pos"

    defaultConfig {
        applicationId = "com.socialite.solite_pos"
        minSdk = 23
        targetSdk = 33
        versionCode = 40
        versionName = "3.3.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
	buildFeatures{
		viewBinding = true
        compose = true
	}
    buildToolsVersion = "30.0.3"
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
    implementation(project(":data"))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.core:core-ktx:$kotlinCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutineVersion")

    // UI
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.recyclerview:recyclerview:$recycleViewVersion")
    implementation("androidx.cardview:cardview:$cardViewVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleExtensionVersion")

    // Compose
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    implementation("androidx.activity:activity-compose:$activityComposeVersion")
    implementation("androidx.constraintlayout:constraintlayout-compose:$constraintComposeVersion")
    implementation("androidx.navigation:navigation-compose:$navigationComposeVersion")
    implementation("com.google.accompanist:accompanist-pager:$accompanistPagerVersion")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanistPagerVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistPagerVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationComposeVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Utils
    implementation("pub.devrel:easypermissions:$easyPermissionVersion")

    // Play In App Update
    implementation("com.google.android.play:app-update:$googlePlayVersion")
    implementation("com.google.android.play:app-update-ktx:$googlePlayVersion")

    // Test
    testImplementation("junit:junit:$junitVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitExtVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
}

kapt {
    correctErrorTypes = true
}
