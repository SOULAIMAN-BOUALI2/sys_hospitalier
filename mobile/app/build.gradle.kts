plugins {
    alias(libs.plugins.android.application)
    kotlin("plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.systemhospitalier"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        viewBinding=true
    }

    defaultConfig {
        applicationId = "com.example.systemhospitalier"
        minSdk = 24
        targetSdk = 36
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Supabase BOM
    implementation(platform("io.github.jan-tennert.supabase:bom:2.6.1"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")


    // Ktor client
    implementation("io.ktor:ktor-client-android:2.3.12")

    implementation("org.mindrot:jbcrypt:0.4")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Gemini AI
    implementation(libs.generativeai)
}

tasks.withType<Test> {
    useJUnitPlatform()
}