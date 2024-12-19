plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")




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
    implementation ("androidx.compose.material:material-icons-extended:1.5.0")
    //biometric
    implementation("androidx.biometric:biometric:1.1.0")
    implementation(libs.biometric)
    //hilt dagger
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation(libs.hilt.android)
    implementation("com.google.dagger:hilt-android:2.44")
    kapt(libs.hilt.android.compiler)
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    //google authentication
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid.vlatestversion)
    //animatedBar
    implementation(libs.animated.navigation.bar)
    //YChart
    implementation (libs.ycharts)

    /////////////////
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation ("androidx.compose.runtime:runtime:1.5.4")

    /////////
    implementation ("androidx.compose.foundation:foundation:1.5.0")
    implementation ("androidx.compose.ui:ui:1.5.0")
    implementation ("androidx.compose.material3:material3:1.1.1" )// Update as needed
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
}
kapt {
    correctErrorTypes = true
}