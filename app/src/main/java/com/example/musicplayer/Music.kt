package com.example.musicplayer

import android.media.MediaPlayer
import android.provider.ContactsContract
import java.io.File

class Music(name:String, photo: Int, text: String, song: MediaPlayer){

    var name: String?=null
    var photo: Int?=0
    var text: String?=null
    var song:MediaPlayer?=null

    init {
        this.name=name
        this.photo=photo
        this.text=text
        this.song=song
    }
}