plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.marrocumarcodeveloper.set_point"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.marrocumarcodeveloper.set_point"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha04")
    implementation("androidx.compose.material:material-icons-extended:1.0.0")
    implementation("androidx.wear.compose:compose-foundation:1.0.0")
    implementation("androidx.wear.compose:compose-navigation:1.4.0")
    implementation("com.google.android.horologist:horologist-compose-layout:0.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("com.google.ar:core:1.46.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}