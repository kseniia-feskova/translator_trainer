import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("plugin.serialization") version "2.1.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    id("com.google.gms.google-services")
}
val localProps = Properties().apply {
    val file = File(rootDir, "local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

android {
    namespace = "com.translator.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.translator.app"
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "version"
    val googleClientId: String = localProps.getProperty("CLIENT_ID") ?: throw GradleException("CLIENT_ID is missing in local.properties")

    productFlavors {
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
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

}

dependencies {
    implementation(project(":shared"))

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")

    implementation("com.google.firebase:firebase-auth:24.0.1")
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("com.google.android.gms:play-services-auth:21.4.0")

    // Room (только Android)
    implementation("androidx.room:room-runtime:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    // Jetpack Compose (включает @Stable)
    implementation("androidx.compose.ui:ui:1.9.5") // или последнюю доступную
    implementation("androidx.compose.runtime:runtime:1.9.5")

    // Kotlinx Serialization (включает @Serializable)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("androidx.security:security-crypto:1.1.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("io.insert-koin:koin-core:4.1.1")
    implementation("io.insert-koin:koin-android:4.1.1")
    implementation("io.insert-koin:koin-androidx-compose:4.1.1")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.11.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-android:1.9.5")
    implementation("androidx.navigation:navigation-runtime-android:2.9.6")
    implementation("androidx.wear.compose:compose-navigation:1.5.5")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.9.5")
    implementation("androidx.navigation:navigation-compose:2.9.6")
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.5")

}
