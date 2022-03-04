package com.example.thayalan

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val videoHolder = VideoView(this)
            setContentView(videoHolder)
            val video: Uri = Uri.parse("android.resource://" + packageName + "/raw/splash")
            videoHolder.setVideoURI(video)
            videoHolder.setOnCompletionListener { jump() }
            videoHolder.start()
        } catch (ex: Exception) {
            jump()
        }
    }
    private fun jump() {
        if (isFinishing) return
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        jump()
        return true
    }
}