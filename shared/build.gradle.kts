import java.util.Properties
import kotlin.apply

plugins {

    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.1.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" // или нужная версия
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    id("com.google.gms.google-services")

}

kotlin {
    androidTarget {
        kotlin {
            jvmToolchain(17) // желательно явно указать
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.21")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2") // KMM совместимый
            implementation("io.insert-koin:koin-core:4.1.1") // KMM поддержка
            // Используй kotlinx.serialization, если нужно
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

            //Ktor
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("io.ktor:ktor-client-logging:2.3.7")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.7")
        }

        androidMain.dependencies {
            //Core
            implementation("androidx.core:core-ktx:1.17.0")
            implementation("androidx.appcompat:appcompat:1.7.1")
            implementation("com.google.android.material:material:1.13.0")

            // RxJava (если тебе это нужно здесь)
            implementation("io.reactivex.rxjava2:rxjava:2.2.21")
            implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

            //Room
            implementation("androidx.room:room-runtime:2.8.4")
            implementation("androidx.datastore:datastore-preferences:1.2.0")
            implementation("androidx.room:room-runtime:2.8.4")

            // Koin Android
            implementation("io.insert-koin:koin-android:4.1.1")
            implementation("io.insert-koin:koin-androidx-compose:4.1.1")

            //Retrofit
            implementation("io.ktor:ktor-client-okhttp:2.3.7")

//            implementation("com.google.code.gson:gson:2.13.2")
//            implementation("com.squareup.retrofit2:retrofit:3.0.0")
//            implementation("com.squareup.retrofit2:converter-gson:3.0.0")

            // Compose
            implementation("androidx.compose.runtime:runtime-android:1.10.0")
            implementation("androidx.navigation:navigation-runtime-android:2.9.6")
            implementation("androidx.compose.material3:material3-android:1.4.0")
            implementation("androidx.compose.material:material-icons-core:1.7.8")
            implementation("androidx.wear.compose:compose-navigation:1.5.6")
            implementation("androidx.compose.ui:ui-tooling-preview-android:1.10.0")
            implementation("androidx.navigation:navigation-compose:2.9.6")

            implementation("io.coil-kt:coil-compose:2.7.0")

            //Firebase
            implementation("com.google.firebase:firebase-auth:24.0.1")
            implementation("androidx.credentials:credentials:1.5.0")
            implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
            implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
            implementation("com.google.android.gms:play-services-auth:21.4.0")

            //Language ML kit
            implementation("com.google.mlkit:translate:17.0.3")
            implementation("com.google.mlkit:text-recognition:16.0.1")


        }
    }
}

android {
    namespace = "com.example.translatortrainer.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
    val localProps = Properties().apply {
        val file = File(rootDir, "local.properties")
        if (file.exists()) {
            load(file.inputStream())
        }
    }
    flavorDimensions += "version"
    productFlavors {
        val googleClientId: String = localProps.getProperty("CLIENT_ID") ?: throw GradleException("CLIENT_ID is missing in local.properties")

        create("dev") {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/\"")
            buildConfigField("String", "CLIENT_ID", "\"$googleClientId\"")
        }

        create("qa") {
            buildConfigField("String", "BASE_URL", "\"http://35.159.225.33:8080/api/\"")
            buildConfigField("String", "CLIENT_ID", "\"$googleClientId\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        ksp("androidx.room:room-compiler:2.8.4")
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:1.10.0")
}
