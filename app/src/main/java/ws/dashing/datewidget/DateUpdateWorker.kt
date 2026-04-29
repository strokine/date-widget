package ws.dashing.datewidget

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DateUpdateWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        DateWidget().updateAll(applicationContext)
        scheduleMidnight(applicationContext)
        return Result.success()
    }

    companion object {
        private const val MIDNIGHT_WORK = "date_widget_midnight_update"
        private const val NOW_WORK = "date_widget_immediate_update"

        fun enqueueNow(context: Context) {
            val req = OneTimeWorkRequestBuilder<DateUpdateWorker>().build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(NOW_WORK, ExistingWorkPolicy.REPLACE, req)
        }

        fun scheduleMidnight(context: Context) {
            val now = LocalDateTime.now()
            val nextMidnight = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT)
            val delayMs = Duration.between(now, nextMidnight).toMillis() + 1_000L
            val req = OneTimeWorkRequestBuilder<DateUpdateWorker>()
                .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(MIDNIGHT_WORK, ExistingWorkPolicy.REPLACE, req)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(MIDNIGHT_WORK)
            WorkManager.getInstance(context).cancelUniqueWork(NOW_WORK)
        }
    }
}
