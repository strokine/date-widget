# Typography

## Font Families

Two font families are used, both targeting Pixel-shipped Google Sans:

| Usage | FontFamily identifier | Kotlin constant |
|---|---|---|
| Day number (hero) | `variable-display-large-emphasized` | `GoogleSansDisplay` |
| Labels (weekday, month-year) | `google-sans` | `GoogleSans` |

On non-Pixel devices that don't ship these families, both fall back to the default sans-serif.

### Why `variable-display-large-emphasized`?

This identifier activates the **display variant** of Google Sans with a high optical size (`opsz=57`). At that optical size the font uses **simplified numeral glyphs** — most notably, the digit "1" renders as a plain vertical stroke without the flag/serif. This matches exactly how the Pixel weather widget renders its temperature number.

Using the regular `google-sans` family for the hero number would produce a "1" with a flag, which looks different from the weather widget.

## Font Weights

| Element | FontWeight | Rationale |
|---|---|---|
| Day number | `Normal` | Matches the stroke thickness of the Pixel weather widget temperature. Was initially set to `Medium` but changed to `Normal` to be an exact match. |
| Weekday label | `Medium` | Slightly bolder than the hero to provide visual hierarchy at small size |
| Month-year label | `Medium` | Same as weekday for consistency |

## Responsive Sizes

The widget reports three size breakpoints:

| Breakpoint | Width threshold | Hero (day number) | Label (weekday, month-year) |
|---|---|---|---|
| Compact | `< 140dp` | 58 sp | 13 sp |
| Regular | `140dp – 239dp` | 88 sp | 18 sp |
| Large | `≥ 240dp` | 100 sp | 22 sp |

Size breakpoints are defined in `DateWidget.companion`:
- `SMALL_SIZE = DpSize(110dp, 110dp)`
- `REGULAR_SIZE = DpSize(180dp, 180dp)`
- `LARGE_SIZE = DpSize(260dp, 260dp)`
