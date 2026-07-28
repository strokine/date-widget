# Changelog

All notable changes to this project are documented here. Format based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), versioning follows
[SemVer](https://semver.org/).

## [1.1.3] - 2026-07-28

### Changed
- Now targets Android 17 (API 37), satisfying Google Play's Aug 31, 2026 target API
  requirement and the Aug 31, 2027 one ahead of time. No functional changes.
- Build toolchain modernised: AGP 8.5.2 → 9.3.1, Gradle 8.7 → 9.6.1,
  Kotlin 1.9.24 → 2.4.10. AGP 9's built-in Kotlin support replaces the
  `kotlin-android` plugin, and the Compose compiler now comes from the
  `org.jetbrains.kotlin.plugin.compose` plugin rather than `composeOptions`.
- Dependencies updated to latest stable: Compose BOM 2026.06.01, core-ktx 1.19.0,
  activity-compose 1.13.0, Material 1.14.0, WorkManager 2.11.2, graphics-shapes 1.1.0.

### Security
- Release keystore credentials moved out of the tracked `app/build.gradle.kts` and into
  a gitignored `keystore.properties`.

## [1.1.1] - 2026-05-25

### Fixed
- Dark mode: the cookie shape now follows the system theme together with
  the text. Previously the shape stayed light in dark mode while the text
  switched, leaving the text invisible against the light background.

## [1.1.0] - 2026-05-25

### Changed
- New launcher icon: shows today's date in the cookie shape matching the
  widget, replacing the four-leaf clover that read as a "+" on circular
  launcher masks. Sized to fit fully within the circular safe zone.

### Fixed
- Widget preview now renders correctly in the system widget picker.

## [1.0.1] - 2026-05-25

- Widget refinements.

## [1.0.0] - 2026-04-29

- First release.

[1.1.1]: https://github.com/strokine/date-widget/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/strokine/date-widget/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/strokine/date-widget/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/strokine/date-widget/releases/tag/v1.0.0
