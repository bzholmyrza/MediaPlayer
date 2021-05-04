package com.example.musicplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class CreateNotification {
    val CHANNEL_ID: String = "channel1"
    val ACTIONPREVIUOS: String = "actionprevious"
    val CHANNEL_PLAY: String = "actionplay"
    val CHANNEL_NEXT: String = "actionnext"

    fun createNotification(context: Context, track: Music, playbutton: Int, pos: Int, size: Int) {
        var nmc: NotificationManagerCompat = NotificationManagerCompat.from(context)
        var msc: MediaSessionCompat = MediaSessionCompat(context, "tag")

        var icon: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.main_page)

        //create notification
        var notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(track.name)
//                .setContentText(track.artist)
                .setLargeIcon(icon)
                .setColor(Color.MAGENTA)
                .setOnlyAlertOnce(true)//show notification for only first time
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        nmc.notify(1, notification)

    }
}