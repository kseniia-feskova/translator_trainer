plugins {

    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.1.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" // или нужная версия
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"

}

kotlin {
    androidTarget {
        kotlin {
            jvmToolchain(17) // желательно явно указать
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.21")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1") // KMM совместимый
            implementation("io.insert-koin:koin-core:3.5.6") // KMM поддержка
            // Используй kotlinx.serialization, если нужно
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
        }

        androidMain.dependencies {
            //Core
            implementation("androidx.core:core-ktx:1.16.0")
            implementation("androidx.appcompat:appcompat:1.7.1")
            implementation("com.google.android.material:material:1.12.0")

            // RxJava (если тебе это нужно здесь)
            implementation("io.reactivex.rxjava2:rxjava:2.2.21")
            implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

            //Room
            implementation("androidx.room:room-runtime:2.7.2")
            implementation("androidx.datastore:datastore-preferences:1.1.7")
            implementation("androidx.room:room-runtime:2.7.2")

            // Koin Android
            implementation("io.insert-koin:koin-android:3.5.6")
            implementation("io.insert-koin:koin-androidx-compose:3.5.6")

            //Retrofit
            implementation("com.google.code.gson:gson:2.11.0")
            implementation("com.squareup.retrofit2:retrofit:2.11.0")
            implementation("com.squareup.retrofit2:converter-gson:2.11.0")

            // Compose
            implementation("androidx.compose.runtime:runtime-android:1.8.3")
            implementation("androidx.navigation:navigation-runtime-android:2.9.0")
            implementation("androidx.compose.material3:material3-android:1.3.2")
            implementation("androidx.wear.compose:compose-navigation:1.4.1")
            implementation("androidx.compose.ui:ui-tooling-preview-android:1.8.3")
            implementation("androidx.navigation:navigation-compose:2.9.0")

            implementation("com.google.mlkit:text-recognition:16.0.1")
            implementation("io.coil-kt:coil-compose:2.5.0")

        }
    }
}

android {
    namespace = "com.example.translatortrainer.shared"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        ksp("androidx.room:room-compiler:2.7.2")
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
