# Cookie Shape (Clover Background)

## Overview

The widget background is a 4-sided "cookie" shape — a rounded star that approximates the Material 3 **Cookie4Sided** shape. It's generated at runtime as a `Bitmap` using `buildCookie4SidedBitmap()` in `DateWidget.kt`.

## Why Runtime Bitmap?

Glance widgets render via `RemoteViews`, which cannot host Jetpack Compose shapes at runtime. The shape must be rasterized to a `Bitmap` that is set as an `Image`.

There is also a `VectorDrawable` version in `res/drawable/bg_clover.xml` (hand-traced cubic path) used as the widget preview image in the picker. The runtime bitmap is preferred for the actual widget because the `RoundedPolygon` API gives precise control.

## RoundedPolygon Parameters

```kotlin
RoundedPolygon.star(
    numVerticesPerRadius = 4,
    radius = 1f,
    innerRadius = 0.45f,
    rounding = CornerRounding(0.5f),
    innerRounding = CornerRounding(1f),
)
```

| Parameter | Value | Effect |
|---|---|---|
| `numVerticesPerRadius` | 4 | 4-pointed star (4 outer tips + 4 inner concavities) |
| `radius` | 1.0 | Outer vertex distance (unit circle) |
| `innerRadius` | 0.45 | How deep the concavities go — lower = more pinched waist |
| `rounding` | 0.5 | Corner rounding at outer vertices |
| `innerRounding` | 1.0 | Corner rounding at inner vertices (fully rounded concavities) |

### Tuning `innerRadius`

- **0.45** is the current value — produces a gentle clover with clearly visible concavities but not an aggressive pinch.
- Higher values (e.g. 0.6) would make it closer to a rounded square.
- Lower values (e.g. 0.3) would produce deeper "bites" at the sides.

## Rotation

`RoundedPolygon.star` places outer vertices at 0°/90°/180°/270° (tips at N/E/S/W). A **45° rotation** is applied so tips land at the diagonal corners, producing a proper square-oriented cookie rather than a diamond.

```kotlin
path.transform(Matrix().apply { setRotate(45f) })
```

## Scaling & Overscale

After rotation, the path is scaled to fill the widget bounds with a **1.05× overscale** factor. This ensures the cookie bleeds slightly beyond the widget edges, preventing thin gaps at the corners.

```kotlin
val scale = maxOf(scaleX, scaleY) * 1.05f
```
