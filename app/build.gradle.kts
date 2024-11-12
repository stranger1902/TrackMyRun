plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)

    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.maps.secret)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.example.trackmyrun"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.trackmyrun"
        versionName = "1.0"
        versionCode = 1
        targetSdk = 33
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Android
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlin.serialization)
    implementation(libs.androidx.core.ktx)

    // Jetpack Compose
    implementation(libs.androidx.hilt.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)

    // Datastore
    implementation(libs.datastore.preferences)

    // Room Database
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.core)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Dagger-Hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.compiler)
    ksp(libs.hilt.compiler)

    // Gps Location Service
    implementation(libs.gps.location)

    // Google Maps Compose
    implementation(libs.maps.compose)
    implementation(libs.maps.utils)

    // Coil Image
    implementation(libs.coil.compose)
    implementation(libs.coil.ktor)
    implementation(libs.coil)

    // Android Test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.junit)

    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
}