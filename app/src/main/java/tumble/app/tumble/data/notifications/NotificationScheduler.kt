package tumble.app.tumble.data.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import tumble.app.tumble.MainActivity
import tumble.app.tumble.R

class NotificationScheduler(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val title = inputData.getString("title") ?: "Something went wrong."
        val channelId = inputData.getString("channelId") ?: "0"
        val notificationId = inputData.getString("notificationId") ?: "0"
        val description = inputData.getString("description") ?: ""
        val longDescription = inputData.getString("longDescription") ?: ""

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setContentText(description)
            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(longDescription))
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }

        NotificationManagerCompat.from(applicationContext).notify(notificationId.hashCode(), notification)
        return Result.success()
    }
}