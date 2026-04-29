package ws.dashing.datewidget

import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class DateWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = DateWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_LOCALE_CHANGED -> DateUpdateWorker.enqueueNow(context)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        DateUpdateWorker.scheduleMidnight(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        DateUpdateWorker.cancel(context)
    }
}
