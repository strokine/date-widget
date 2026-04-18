# Color System

## Dynamic Colors (Android 12+ / API 31)

On Android 12+, the widget uses **wallpaper-derived dynamic colors** from `system_accent1_*`. These tint the widget to match the user's wallpaper palette automatically.

### Light mode (`values-v31/colors.xml`)

| Color name | Resource | Role |
|---|---|---|
| `widget_primary` | `system_accent1_600` | Primary accent |
| `widget_on_primary` | `system_accent1_0` | Text on primary |
| `widget_primary_container` | `system_accent1_100` | Cookie fill color |
| `widget_on_primary_container` | `system_accent1_900` | Day number text |
| `widget_primary_40` | `system_accent1_600` | Label text (weekday, month-year) |

### Dark mode (`values-night-v31/colors.xml`)

| Color name | Resource |
|---|---|
| `widget_primary` | `system_accent1_200` |
| `widget_on_primary` | `system_accent1_800` |
| `widget_primary_container` | `system_accent1_700` |
| `widget_on_primary_container` | `system_accent1_100` |
| `widget_primary_40` | `system_accent1_200` |

### Clover drawable gradient (dynamic)

The `drawable-v31/bg_clover.xml` uses a radial gradient from `system_accent1_100` → `system_accent1_200` (light) and `system_accent1_700` → `system_accent1_800` (dark).

## Static Fallback (Android 8–11)

On pre-Android 12 devices, the widget falls back to a static Material 3 purple palette:

### Light (`values/colors.xml`)

| Color name | Hex | Role |
|---|---|---|
| `widget_primary` | `#6750A4` | M3 Primary |
| `widget_on_primary` | `#FFFFFF` | White |
| `widget_primary_container` | `#EADDFF` | M3 Primary Container |
| `widget_on_primary_container` | `#21005D` | M3 On Primary Container |
| `widget_primary_40` | `#4F378B` | M3 Primary tone 40 |

### Dark (`values-night/colors.xml`)

| Color name | Hex |
|---|---|
| `widget_primary` | `#D0BCFF` |
| `widget_on_primary` | `#381E72` |
| `widget_primary_container` | `#4F378B` |
| `widget_on_primary_container` | `#EADDFF` |
| `widget_primary_40` | `#D0BCFF` |

## Accent vs Neutral — Comparison with Pixel Weather Widget

The Pixel weather widget uses a **neutral** dynamic color (e.g. `system_neutral1_10`) for its circular background — this stays pure white regardless of wallpaper. The date widget uses **accent** colors (`system_accent1_*`), which pick up a hue tint from the wallpaper. For example, on a green wallpaper the weather widget circle appears pure white while the date widget clover has a subtle greenish tint.

This is an intentional design choice — the date widget integrates more closely with the wallpaper palette.
