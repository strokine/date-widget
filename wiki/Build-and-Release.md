# Build and Release

## Requirements

- **Android Studio**: Jellyfish (2023.3.1) or newer
- **JDK**: 17
- **minSdk**: 26 (Android 8.0)
- **targetSdk / compileSdk**: 34

## Dependencies

| Group | Artifact | Version | Purpose |
|---|---|---|---|
| `androidx.glance` | `glance-appwidget` | 1.1.0 | Glance widget framework |
| `androidx.glance` | `glance-material3` | 1.1.0 | Material 3 Glance components |
| `androidx.graphics` | `graphics-shapes` | 1.0.1 | `RoundedPolygon` for cookie shape |
| `androidx.work` | `work-runtime-ktx` | 2.9.0 | WorkManager for midnight refresh |
| `androidx.compose` | BOM `2024.06.00` | — | Compose for MainActivity |
| `androidx.compose.material3` | `material3` | (BOM) | Material 3 theme for launcher activity |

Compose compiler extension version: `1.5.14`

## Gradle Wrapper

The project does **not** commit the Gradle wrapper JAR. First-time setup:

```bash
gradle wrapper --gradle-version 8.7
```

Or just open in Android Studio — it bootstraps automatically.

## Build Commands

```bash
# Debug
./gradlew :app:installDebug

# Release (signed)
./gradlew :app:assembleRelease
```

## Release Signing

| Property | Value |
|---|---|
| Keystore file | `release-keystore.jks` (project root) |
| Key alias | `release` |
| Store password | `datewidget` |
| Key password | `datewidget` |

Configured in `app/build.gradle.kts` under `signingConfigs.create("release")`.

> **Note**: The keystore file is git-ignored (`*.jks` in `.gitignore`). You need the keystore file locally to produce a signed release build.
