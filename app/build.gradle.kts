import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "ws.dashing.datewidget"

    // Target the newest stable API (37 / Android 17). Google Play only requires 36 by
    // Aug 31, 2026, but targeting 37 also satisfies the Aug 31, 2027 deadline, so the next
    // forced update is Aug 31, 2028.
    compileSdk = 37

    defaultConfig {
        applicationId = "ws.dashing.datewidget"
        minSdk = 26
        targetSdk = 37
        versionCode = 7
        versionName = "1.1.3"
        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // AGP 9 built-in Kotlin: jvmTarget defaults to compileOptions.targetCompatibility (17),
    // so the old kotlinOptions block is no longer needed. composeOptions is likewise obsolete —
    // the Compose compiler ships with the org.jetbrains.kotlin.plugin.compose plugin from
    // Kotlin 2.0 onward.

    buildFeatures { compose = true }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("com.google.android.material:material:1.14.0")

    // Compose (for MainActivity)
    implementation(platform("androidx.compose:compose-bom:2026.06.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Glance (widget)
    implementation("androidx.glance:glance-appwidget:1.1.1")
    implementation("androidx.glance:glance-material3:1.1.1")

    // Material shapes (Cookie4Sided)
    implementation("androidx.graphics:graphics-shapes:1.1.0")

    // WorkManager (midnight refresh)
    implementation("androidx.work:work-runtime-ktx:2.11.2")
}
