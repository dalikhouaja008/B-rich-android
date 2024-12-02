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
    implementation(libs.androidx.navigation.runtime.ktx)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //navigation
    val nav_version = "2.8.0"
    implementation(libs.androidx.navigation.compose)
    //Room injection
    val room_version = "2.6.1"
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    //Kapt annotation processor
    kapt("androidx.room:room-compiler:2.6.1")
    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //live data
    implementation (libs.androidx.lifecycle.livedata.ktx)
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

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)


    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

}
kapt {
    correctErrorTypes = true
}