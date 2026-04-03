import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    val xcfName = "sharedKit"

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.framework {
            baseName = xcfName
            isStatic = true
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.serialization.json)

                // Ktor (общий)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.ktor.client.logging)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.appcompat)
                implementation(libs.coil.compose)
                implementation(libs.firebase.auth)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.datastore.preferences)
                implementation(libs.room.runtime)
                implementation(libs.bundles.compose)
                implementation(libs.compose.material3)
                implementation(libs.bundles.navigation)
                implementation(libs.bundles.koin.android)
                implementation(libs.bundles.google.auth)
                implementation(libs.bundles.mlkit)
            }
        }

        val iosMain by creating {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
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
        val file = rootProject.file("local.properties")
        if (file.exists()) load(file.inputStream())
    }

    flavorDimensions += "version"

    productFlavors {

        val googleClientId: String = localProps.getProperty("CLIENT_ID")
            ?: error("CLIENT_ID missing in local.properties")

        create("dev") {
            dimension = "version"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/api/\"")
            buildConfigField("String", "CLIENT_ID", "\"$googleClientId\"")
        }

        create("qa") {
            dimension = "version"
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
        add("kspAndroid", "androidx.room:room-compiler:2.8.4")
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}