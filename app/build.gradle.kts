plugins {
    id("com.android.application")
    kotlin("plugin.serialization") version "2.1.21"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21" // или нужная версия
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"

}

android {
    namespace = "com.translator.app"
    compileSdk = 35

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
    productFlavors {
        create("dev") {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/\"")
        }

        create("qa") {
            buildConfigField("String", "BASE_URL", "\"http://18.193.113.17:8080/api/\"")
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

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Room (только Android)
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Jetpack Compose (включает @Stable)
    implementation("androidx.compose.ui:ui:1.8.3") // или последнюю доступную
    implementation("androidx.compose.runtime:runtime:1.8.3")

    // Kotlinx Serialization (включает @Serializable)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("androidx.security:security-crypto:1.1.0-beta01")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("io.insert-koin:koin-core:3.5.6")
    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.08.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.runtime:runtime-android:1.9.0")
    implementation("androidx.navigation:navigation-runtime-android:2.9.3")
    implementation("androidx.wear.compose:compose-navigation:1.4.1")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.9.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.0")

}
