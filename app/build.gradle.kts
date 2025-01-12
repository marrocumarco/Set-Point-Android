// Define versions as constants
val coreKtxVersion by extra("1.15.0")
val playServicesWearableVersion by extra("19.0.0")
val percentLayoutVersion by extra("1.0.0")
val legacySupportV4Version by extra("1.0.0")
val recyclerViewVersion by extra("1.3.2")
val composeBomVersion by extra("2023.03.00")
val material3Version by extra("1.3.1")
val material3AdaptiveVersion by extra("1.4.0-alpha05")
val materialIconsExtendedVersion by extra("1.7.6")
val wearComposeFoundationVersion by extra("1.4.0")
val horologistComposeLayoutVersion by extra("0.6.0")
val lifecycleRuntimeKtxVersion by extra("2.8.7")
val activityComposeVersion by extra("1.9.3")
val animationGraphicsVersion by extra("1.7.6")
val wearToolingPreviewVersion by extra("1.0.0")
val hiltAndroidVersion by extra("2.51.1")
val mockitoCoreVersion by extra("5.5.0")
val mockitoKotlinVersion by extra("5.1.0")

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
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("com.google.android.gms:play-services-wearable:$playServicesWearableVersion")
    implementation("androidx.percentlayout:percentlayout:$percentLayoutVersion")
    implementation("androidx.legacy:legacy-support-v4:$legacySupportV4Version")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:$material3Version")
    implementation("androidx.compose.material3:material3-window-size-class:$material3Version")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:$material3AdaptiveVersion")
    implementation("androidx.compose.material:material-icons-extended:$materialIconsExtendedVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeFoundationVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeFoundationVersion")
    implementation("com.google.android.horologist:horologist-compose-layout:$horologistComposeLayoutVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleRuntimeKtxVersion")
    implementation("androidx.activity:activity-compose:$activityComposeVersion")
    implementation("androidx.compose.animation:animation-graphics-android:$animationGraphicsVersion")
    implementation("androidx.wear:wear-tooling-preview:$wearToolingPreviewVersion")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("com.google.dagger:hilt-android:$hiltAndroidVersion")
    implementation("androidx.compose.animation:animation:$animationGraphicsVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltAndroidVersion")
    testImplementation(kotlin("test"))
    testImplementation("org.mockito:mockito-core:$mockitoCoreVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}