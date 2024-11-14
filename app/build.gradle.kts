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
        minSdk = 24
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

    //Hilt pour l'Injection de Dépendances
    //implementation (libs.hilt)

    implementation (libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.androidx.fragment.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //visibility + visibilityoff
    implementation (libs.ui)
    implementation (libs.androidx.material)
    implementation (libs.androidx.material.icons.extended.v130)
    implementation (libs.ui.tooling.preview)


    //EncryptedSharedPreferences
    implementation (libs.androidx.security.crypto)

    implementation (libs.androidx.security.crypto.ktx.v110alpha05)


    //navigation
    val nav_version = "2.8.0"
    // Jetpack Compose integration
    implementation(libs.androidx.navigation.compose)

    //Room injection
    val room_version = "2.6.1"
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    //Kapt annotation processor
    kapt(libs.androidx.room.compiler)
    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //live data
    implementation (libs.androidx.lifecycle.livedata.ktx)

    implementation (libs.androidx.material.icons.extended.v150)


    implementation (libs.androidx.material.icons.extended.vversion)
    implementation (libs.material3)

    implementation (libs.androidx.lifecycle.livedata.ktx.v251)
    //icones
    implementation ("androidx.compose.material:material-icons-extended:1.7.5")
    //biometric
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.biometric:biometric:1.4.0-alpha02")
    //hilt dagger
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
}
kapt {
    correctErrorTypes = true
}