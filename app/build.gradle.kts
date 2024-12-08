plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-kapt")


}

android {
    namespace = "com.example.b_rich"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.b_rich"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)

    implementation (libs.androidx.material.icons.extended)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.play.services.basement)
    implementation(libs.googleid)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.foundation.android)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //navigation
    val nav_version = "2.8.0"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    //Room injection
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    //Kapt annotation processor
    kapt("androidx.room:room-compiler:2.6.1")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //live data
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    //icones
    implementation (libs.androidx.material.icons.extended.v150)
    //biometric
    implementation(libs.androidx.biometric.v110)
    implementation(libs.biometric)
    //hilt dagger
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //google authentication
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid.vlatestversion)
    //animatedBar
    implementation(libs.animated.navigation.bar)
    //YChart
    implementation (libs.ycharts)
    // QR Code generation
    implementation (libs.core)
    implementation (libs.zxing.android.embedded)
    // QR Code scanner
    implementation (libs.barcode.scanning)
    //caméra
    val camerax_version = "1.3.0"
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
}
kapt {
    correctErrorTypes = true
}