package com.minimaldev.recentappswitcher.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.minimaldev.recentappswitcher.R
import com.minimaldev.recentappswitcher.utils.Constants.INTENT_SHOW_WINDOW

class FloatingWindowService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("InflateParams")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || !intent.hasExtra(INTENT_SHOW_WINDOW.value) ||
            !intent.getBooleanExtra(INTENT_SHOW_WINDOW.value, false)
        ) {
            stopService()
            return START_NOT_STICKY
        }
        val showWindow = intent.getBooleanExtra(INTENT_SHOW_WINDOW.value, false)
        if (showWindow) {
            floatingView = LayoutInflater.from(this).inflate(
                R.layout.floating_window,
                null
            )

            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.OPAQUE
            )
            params.gravity = Gravity.BOTTOM or Gravity.END
            windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(floatingView, params)
        }
        return START_STICKY
    }

    private fun stopService() {
        stopSelf()
        windowManager.removeView(floatingView)
    }

}