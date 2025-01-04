plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")

}

android {
    namespace = "kz.petprojects.qrreader"
    compileSdk = 35

    defaultConfig {
        applicationId = "kz.petprojects.qrreader"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

//    implementation(libs.koin.androidx.compose)
//    implementation(libs.koin.core)
//    implementation(libs.koin.android.compat)
//    implementation(libs.koin.android)

//    implementation(libs.koin.android)
//    implementation(libs.koin.androidx.viewmodel)
//    implementation(libs.koin.androidx.scope)

    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.insert-koin:koin-android:3.2.0")
    implementation ("io.insert-koin:koin-androidx-compose:3.2.0")

    implementation("androidx.navigation:navigation-compose:2.7.2")


    implementation ("androidx.room:room-ktx:2.5.2")
    annotationProcessor ("androidx.room:room-compiler:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    // Activity Compose
    implementation("androidx.activity:activity-compose:1.4.0")

    // CameraX
    implementation("androidx.camera:camera-camera2:1.0.2")
    implementation("androidx.camera:camera-lifecycle:1.0.2")
    implementation("androidx.camera:camera-view:1.0.0-alpha31")

    // Zxing
    implementation("com.google.zxing:core:3.3.3")


}