package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import android.media.MediaPlayer.create as create1

class MainActivity : AppCompatActivity() {
    private var handler = Handler()
    lateinit var runable: Runnable
    private var totalTime: Int = 0
    private var position: Int = 0
    lateinit var timePicker: TimePickerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timePicker = TimePickerHelper(this, false, false)

        val mySong1 = Music("Haroinfather - Forever", R.drawable.music1, "music1.txt", create1(this, R.raw.music1))
        val mySong2 = Music("Скриптонит - Стиль", R.drawable.music2, "music2.txt", create1(this, R.raw.music2))
        val mySong3 = Music("Eminem feat. Juice WRLD - Godzilla", R.drawable.music3, "music3.txt", create1(this, R.raw.music3))
        val mySong4 = Music("Айкын Толепберген - Алтыным", R.drawable.music4, "music4.txt", create1(this, R.raw.music4))
        val mySong5 = Music("RAIM & ARTUR - САУКЕЛЕ", R.drawable.music5, "music5.txt", create1(this, R.raw.music5))

        val myList1: ArrayList<Music>? = arrayListOf(
            mySong1,
            mySong2,
            mySong3,
            mySong4,
            mySong5
        )

        var currMusic: Music = myList1!![position]
        var currSong: MediaPlayer = currMusic.song!!
        totalTime = currSong.duration
        music_title.text = currMusic.name
        icon!!.setImageResource(currMusic.photo!!)

        text_btn?.setOnClickListener{
            val imageParam = icon?.layoutParams
            val scrollTextParam = scroll_text?.layoutParams
            if (imageParam?.height != 0 && imageParam?.width != 0) {
                imageParam?.height = 0
                imageParam?.width = 0
                scrollTextParam?.height = (400 * Resources.getSystem().displayMetrics.density).toInt()
                scrollTextParam?.width = (400 * Resources.getSystem().displayMetrics.density).toInt()
                text.text = application.assets.open(currMusic.text!!).bufferedReader().use { it.readText() }
            } else {
                imageParam.height = (400 * Resources.getSystem().displayMetrics.density).toInt()
                imageParam.width = (400 * Resources.getSystem().displayMetrics.density).toInt()
                scrollTextParam?.height = 0
                scrollTextParam?.width = 0
                text.text = ""
            }
        }
        seekBar!!.progress = 0
        seekBar!!.max = totalTime

        //process of pause and resume
        play_btn?.setOnClickListener {
            if (!currSong.isPlaying) {
                currSong.start()
                play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                currSong.pause()
                play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
            CreateNotification().createNotification(this@MainActivity, currMusic, R.drawable.image_main_page, 1, 1)
        }



        //process of next song
        next_btn?.setOnClickListener {
            currSong.pause()
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            if (position < 4) {
                position = position + 1
            } else {
                position = 0
            }

            currMusic = myList1[position]
            currSong = currMusic.song!!
            totalTime = currSong.duration
            music_title.text = currMusic.name!!
            icon!!.setImageResource(currMusic.photo!!)
            currSong.start()
            play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
            CreateNotification().createNotification(this@MainActivity, currMusic, R.drawable.image_main_page, 1, 1)
        }

        //process of previous song
        previous_btn?.setOnClickListener {
            currSong.pause()
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            if (position > 0) {
                position = position - 1
            } else {
                position = 4
            }
            currMusic = myList1[position]
            currSong = currMusic.song!!
            totalTime = currSong.duration
            music_title.text = currMusic.name
            icon.setImageResource(currMusic.photo!!)
            currSong.start()
            play_btn!!.setImageResource(R.drawable.ic_baseline_pause_24)
            CreateNotification().createNotification(this@MainActivity, currMusic, R.drawable.image_main_page, 1, 1)
        }

        notify_btn.setOnClickListener{
            showTimePickerDialog()
            var nmc: NotificationManagerCompat = NotificationManagerCompat.from(this)
            var icon: Bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.image_main_page)
            var notification = NotificationCompat.Builder(this, "10 seconds")
                    .setSmallIcon(R.drawable.ic_timer)
                    .setContentTitle("Time is up")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setLargeIcon(icon)
                    .setColor(Color.RED)
                    .setOnlyAlertOnce(true)//show notification for only first time
                    .setShowWhen(false)
                    .build()

            nmc.notify(2, notification)
        }

        // Volume Bar
        volumeBar!!.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val volumeNum = progress / 100.0f
                        currSong.setVolume(volumeNum, volumeNum)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            }
        )

        //process of progress bar
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, pos: Int, changed: Boolean) {
                if (changed) {
                    currSong.seekTo(pos)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runable = Runnable {
            seekBar!!.progress = currSong.currentPosition
            handler.postDelayed(runable, 1000)
        }
        handler.postDelayed(runable, 1000)
        currSong.setOnCompletionListener {
            play_btn!!.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            seekBar!!.progress = 0
        }

        //process of increasing and decreasing time
        Thread(Runnable {
            while (currSong != null) {
                try {
                    var msg = Message()
                    msg.what = currSong.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

        @SuppressLint("HandlerLeak")
        handler = object : Handler() {

            override fun handleMessage(msg: Message) {
                val currentPosition = msg.what
                val elapsedTime = createTimeLabel(currentPosition)
                current_time.text = elapsedTime
                val remainingTime = createTimeLabel(totalTime - currentPosition)
                finish_time.text = "-$remainingTime"
            }
        }
    }

    private fun showTimePickerDialog() {
        val cal = Calendar.getInstance()
        val h = cal.get(Calendar.HOUR_OF_DAY)
        val m = cal.get(Calendar.MINUTE)
        timePicker.showDialog(h, m, object : TimePickerHelper.Callback {
            override fun onTimeSelected(hourOfDay: Int, minute: Int) {
                val hourStr = if (hourOfDay < 10) "0${hourOfDay}" else "${hourOfDay}"
                val minuteStr = if (minute < 10) "0${minute}" else "${minute}"
            }
        })
    }

    fun createTimeLabel(time: Int): String {
        var timelabel = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        timelabel = "$min:"
        if (sec < 10) timelabel += "0"
        timelabel += sec
        return timelabel
    }
}