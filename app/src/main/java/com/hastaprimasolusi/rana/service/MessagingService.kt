package com.hastaprimasolusi.rana.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.local.DbRepository
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasMainActivity
import com.hastaprimasolusi.rana.ui.lp.LpMainActivity
import com.hastaprimasolusi.rana.ui.mitra.MainActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.getDate
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/13/20
 */
class MessagingService : FirebaseMessagingService() {
    private val dbRepository: DbRepository by inject()
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        println("REMOTE MESSAGE ${remoteMessage.data}")

        val id = remoteMessage.data["id"]
        val code = remoteMessage.data["code"]
        val type = remoteMessage.data["type"]
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        var notifModel = MessageModel(id.toString(), code.toString(), type.toString(), title.toString(),
            message.toString(), "0", UtilsPref.getUserRole(), getDate())

        dbRepository.insert(notifModel)

        var intent: Intent = when(UtilsPref.getUserRole()){
            "local_partner" -> {
                Intent(this, LpMainActivity::class.java)
            }
            "canvasser", "spg", "msr" ->  {
                Intent(this, CanvasMainActivity::class.java)
            }
            else -> {
                Intent(this, MainActivity::class.java)
            }
        }

        intent.putExtra("id", id)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "dmlt_channel_message")
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle())
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "dmlt_channel_message",
                "DMLT Sales Online",
                NotificationManager.IMPORTANCE_HIGH
            )
            val attributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.description = message
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setShowBadge(true)
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), attributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())

        if(type == "3"){
            val broadcast = Intent()
            broadcast.putExtra("id", id)
            broadcast.action = "PAYMENTS"
            sendBroadcast(broadcast)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        UtilsPref.saveString("firebaseToken", token)
    }

}