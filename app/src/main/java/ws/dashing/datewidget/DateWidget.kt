package ws.dashing.datewidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.provider.CalendarContract
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle as JTextStyle

class DateWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(SMALL_SIZE, REGULAR_SIZE, LARGE_SIZE)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent { WidgetContent() }
    }

    @Composable
    private fun WidgetContent() {
        val context = LocalContext.current
        val size = LocalSize.current
        val density = context.resources.displayMetrics.density
        val widthPx = (size.width.value * density).toInt().coerceAtLeast(1)
        val heightPx = (size.height.value * density).toInt().coerceAtLeast(1)
        val compact = size.width < 140.dp
        val large = size.width >= 240.dp
        val locale: Locale = Locale.getDefault()
        val today: LocalDate = LocalDate.now()

        val weekday = today.dayOfWeek
            .getDisplayName(if (compact) JTextStyle.SHORT else JTextStyle.FULL, locale)
            .uppercase(locale)
        val dayNum = today.dayOfMonth.toString()
        val monthYear = today.format(DateTimeFormatter.ofPattern("LLLL yyyy", locale))

        val heroSize = when {
            large -> 100.sp
            compact -> 58.sp
            else -> 88.sp
        }
        val labelSize = when {
            large -> 22.sp
            compact -> 13.sp
            else -> 18.sp
        }

        val onContainer = ColorProvider(resId = R.color.widget_on_primary_container)
        val primary40 = ColorProvider(resId = R.color.widget_primary_40)
        val fillColor = ContextCompat.getColor(context, R.color.widget_primary_container)
        val cookieBitmap = buildCookie4SidedBitmap(widthPx, heightPx, fillColor)

        val openCalendar = actionStartActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = CalendarContract.CONTENT_URI.buildUpon()
                    .appendPath("time")
                    .appendPath(System.currentTimeMillis().toString())
                    .build()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .clickable(openCalendar),
            contentAlignment = Alignment.Center
        ) {
            Image(
                provider = ImageProvider(cookieBitmap),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = GlanceModifier.fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = GlanceModifier.height(12.dp))
                Text(
                    text = weekday,
                    style = TextStyle(
                        color = primary40,
                        fontSize = labelSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = GoogleSans
                    )
                )
                Text(
                    text = dayNum,
                    style = TextStyle(
                        color = onContainer,
                        fontSize = heroSize,
                        fontWeight = FontWeight.Normal,
                        fontFamily = GoogleSansDisplay
                    )
                )
                Text(
                    text = monthYear,
                    style = TextStyle(
                        color = primary40,
                        fontSize = labelSize,
                        fontWeight = FontWeight.Medium,
                        fontFamily = GoogleSans
                    )
                )
                Spacer(modifier = GlanceModifier.height(12.dp))
            }
        }
    }

    companion object {
        private val SMALL_SIZE = DpSize(110.dp, 110.dp)
        private val REGULAR_SIZE = DpSize(180.dp, 180.dp)
        private val LARGE_SIZE = DpSize(260.dp, 260.dp)
    }
}

// Pixel/Google system "Google Sans" family. Falls back to the default
// sans-serif on devices that don't ship the family.
private val GoogleSans = FontFamily("google-sans")

// Display variant with high optical size (opsz=57) — triggers simplified
// numeral glyphs (unflagged "1") matching the Pixel weather widget.
private val GoogleSansDisplay = FontFamily("variable-display-large-emphasized")

private fun buildCookie4SidedBitmap(widthPx: Int, heightPx: Int, color: Int): Bitmap {
    // Reuse Material's RoundedPolygon shape library: a 4-pointed rounded star
    // approximates the Material 3 "Cookie4Sided" shape.
    val polygon = RoundedPolygon.star(
        numVerticesPerRadius = 4,
        radius = 1f,
        innerRadius = 0.45f,
        rounding = CornerRounding(0.5f),
        innerRounding = CornerRounding(1f),
    )

    val path = Path()
    val cubics = polygon.cubics
    if (cubics.isNotEmpty()) {
        path.moveTo(cubics.first().anchor0X, cubics.first().anchor0Y)
        for (c in cubics) {
            path.cubicTo(
                c.control0X, c.control0Y,
                c.control1X, c.control1Y,
                c.anchor1X, c.anchor1Y,
            )
        }
        path.close()
    }

    // RoundedPolygon.star puts outer vertices at 0/90/180/270 deg (tips at
    // N/E/S/W). Rotate 45 deg so vertices land at the corners -- i.e. a proper
    // square-oriented 4-sided cookie rather than a diamond.
    path.transform(Matrix().apply { setRotate(45f) })

    // Scale the rotated path's bounds to fill the entire widget (no padding).
    val rect = RectF().also { path.computeBounds(it, true) }
    val polyW = rect.width().coerceAtLeast(0.0001f)
    val polyH = rect.height().coerceAtLeast(0.0001f)
    val scaleX = widthPx / polyW
    val scaleY = heightPx / polyH
    val scale = maxOf(scaleX, scaleY) * 1.05f
    val scaledW = polyW * scale
    val scaledH = polyH * scale
    path.transform(Matrix().apply {
        postTranslate(-rect.left, -rect.top)
        postScale(scale, scale)
        postTranslate((widthPx - scaledW) / 2f, (heightPx - scaledH) / 2f)
    })

    val bmp = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
    canvas.drawPath(path, paint)
    return bmp
}
