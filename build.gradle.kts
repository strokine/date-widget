plugins {
    // AGP 9 has built-in Kotlin support, so org.jetbrains.kotlin.android is no longer applied.
    id("com.android.application") version "9.3.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.10" apply false
}
