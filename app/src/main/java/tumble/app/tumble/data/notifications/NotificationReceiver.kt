package tumble.app.tumble.data.notifications

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import tumble.app.tumble.R

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission") // app will check permission elsewhere
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getStringExtra("notification_id") ?: return
        val notificationTitle = intent.getStringExtra("notification_title")
        val notificationBody = intent.getStringExtra("notification_body")
        val builder = NotificationCompat.Builder(context, "default_channel_id")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId.hashCode(), builder.build())
        }
    }
}
