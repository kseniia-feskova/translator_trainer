import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
}
val localProps = Properties().apply {
    val file = File(rootDir, "local.properties")
    if (file.exists()) {
        load(file.inputStream())
    }
}

extensions.configure<ApplicationExtension>  {
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

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.kotlin.get() // для Compose 1.10.x
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.room.runtime)
    implementation(libs.koin.core)
    implementation(libs.bundles.koin.android)
    implementation(libs.security)
    implementation(libs.coil.compose)
    implementation(libs.bundles.compose)
    implementation(libs.compose.material3)
    implementation(libs.bundles.navigation)
    ksp(libs.room.compiler)

}
