# Widget Architecture

## Components

| File | Role |
|---|---|
| `DateWidget.kt` | `GlanceAppWidget` — renders the clover background bitmap + text layout. Handles responsive sizing. |
| `DateWidgetReceiver.kt` | `GlanceAppWidgetReceiver` — listens for system date/time/timezone/locale broadcasts and triggers updates. |
| `DateUpdateWorker.kt` | `CoroutineWorker` — scheduled via WorkManager to refresh the widget at local midnight. |
| `MainActivity.kt` | Minimal launcher activity with a hint to add the widget. Uses Material 3 dynamic color theme. |

## Rendering Pipeline

1. `DateWidget.provideGlance()` calls `provideContent { WidgetContent() }`
2. `WidgetContent()` is a `@Composable` that:
   - Reads the current `LocalSize` to determine compact/regular/large breakpoint
   - Converts dp size to pixels via `displayMetrics.density`
   - Calls `buildCookie4SidedBitmap()` to rasterize the clover shape
   - Lays out a `Box` with the bitmap as background `Image` + a `Column` of three `Text` elements (weekday, day number, month-year)
3. The entire `Box` is clickable — launches the system Calendar at today's date

## Responsive Sizing

Uses `SizeMode.Responsive` with three breakpoints:

| Name | DpSize | Visual |
|---|---|---|
| SMALL | 110×110 | Compact: short weekday abbreviation, 58sp hero |
| REGULAR | 180×180 | Full weekday name, 88sp hero |
| LARGE | 260×260 | Full weekday name, 100sp hero |

The compact flag triggers at `width < 140dp`, large at `width ≥ 240dp`.

## Update Triggers

### Broadcast-based (immediate)
`DateWidgetReceiver.onReceive()` handles:
- `ACTION_DATE_CHANGED` — date rolled over
- `ACTION_TIMEZONE_CHANGED` — user changed timezone
- `ACTION_TIME_CHANGED` — user manually set time
- `ACTION_LOCALE_CHANGED` — language/locale changed

Each triggers `DateUpdateWorker.enqueueNow()` which runs a one-shot worker.

### Midnight worker (scheduled)
`DateUpdateWorker.scheduleMidnight()` calculates milliseconds until next midnight + 1 second buffer, then enqueues a `OneTimeWorkRequest` with that delay. After running, the worker re-schedules itself for the next midnight.

The worker is set up in `onEnabled()` and cancelled in `onDisabled()`.

### `updatePeriodMillis`
Set to `0` in `date_widget_info.xml` — the widget does **not** use the system's periodic update mechanism. WorkManager handles all scheduling.

## Widget Metadata

From `res/xml/date_widget_info.xml`:
- Min size: 110dp × 110dp
- Target cell size: 2×2
- Resize: horizontal + vertical
- Category: home_screen only
- Preview image: `@drawable/bg_clover`
