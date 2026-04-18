# Pixel Weather Widget Comparison

## Side-by-Side Analysis

When placed next to the stock Pixel weather widget on a home screen, here's how the date widget compares:

## Font / Numbers

**Goal**: The date widget's day number should look identical to the weather widget's temperature number.

| Aspect | Weather widget | Date widget | Match? |
|---|---|---|---|
| Font family | Google Sans Display (high opsz) | `variable-display-large-emphasized` | ✅ Same simplified glyphs |
| "1" glyph | Plain vertical stroke, no flag | Plain vertical stroke, no flag | ✅ |
| Weight | Normal | `FontWeight.Normal` | ✅ |
| Stroke thickness | Thin/regular display weight | Same | ✅ |

The key discovery was that `variable-display-large-emphasized` activates the same optical size variant as the weather widget, producing identical numeral shapes. `FontWeight.Normal` (not `Medium`) matches the stroke thickness exactly.

## Background

| Aspect | Weather widget | Date widget |
|---|---|---|
| Shape | Circle | 4-sided cookie (clover) |
| Color source | **Neutral** dynamic (`system_neutral1_*`) | **Accent** dynamic (`system_accent1_*`) |
| Wallpaper tinting | None — stays pure white/dark | Picks up wallpaper hue tint |

### Why backgrounds look different

The weather widget uses **neutral** palette colors (no hue), so its circle appears pure white in light mode regardless of wallpaper. The date widget uses **accent1** palette colors, which carry the wallpaper's primary hue. On a green wallpaper, the date widget gets a subtle greenish tint; on a blue wallpaper, a bluish tint; etc.

This is **intentional** — the date widget is designed to integrate with the wallpaper palette rather than appear colorless.

### Switching to neutral (if desired)

To make the date widget background match the weather widget's pure white:
- Change `system_accent1_100` → `system_neutral1_10` (or `system_neutral2_100`) in `values-v31/colors.xml`
- Update the corresponding dark and drawable variants similarly

This was considered but decided against to keep the accent-tinted look.
