plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "daluai.app.cpptmin"
    compileSdk = 34

    defaultConfig {
        applicationId = "daluai.app.cpptmin"
        minSdk = 26
        targetSdk = 34
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
}

//noinspection UseTomlInstead
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("daluai.lib:sdk-boost:1.4.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}