package com.smartdrawer.app.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.smartdrawer.app.R
import com.smartdrawer.app.ui.DrawerOverlay
import com.smartdrawer.app.utils.PreferencesManager

class FloatingWidgetService : Service() {
    
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var floatingIcon: ImageView
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var preferencesManager: PreferencesManager
    private var drawerOverlay: DrawerOverlay? = null
    
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var isDragging = false
    
    override fun onCreate() {
        super.onCreate()
        
        preferencesManager = PreferencesManager(this)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        createFloatingWidget()
    }
    
    private fun createFloatingWidget() {
        // Inflate the floating widget layout
        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget, null)
        floatingIcon = floatingView.findViewById(R.id.imageViewFloatingIcon)
        
        // Set up layout parameters
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        
        // Set initial position
        params.gravity = Gravity.TOP or Gravity.START
        params.x = getSavedXPosition()
        params.y = getSavedYPosition()
        
        // Add touch listener for dragging and clicking
        floatingView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        return true
                    }
                    
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - initialTouchX
                        val deltaY = event.rawY - initialTouchY
                        
                        if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                            isDragging = true
                            params.x = initialX + deltaX.toInt()
                            params.y = initialY + deltaY.toInt()
                            windowManager.updateViewLayout(floatingView, params)
                        }
                        return true
                    }
                    
                    MotionEvent.ACTION_UP -> {
                        if (!isDragging) {
                            // It's a click, not a drag
                            onFloatingIconClicked()
                        } else {
                            // Save position after dragging
                            savePosition(params.x, params.y)
                        }
                        return true
                    }
                }
                return false
            }
        })
        
        // Add the floating widget to window
        windowManager.addView(floatingView, params)
    }
    
    private fun onFloatingIconClicked() {
        // Show slide up animation
        animateIconSlideUp()
        
        // Show the app drawer overlay
        showDrawerOverlay()
    }
    
    private fun animateIconSlideUp() {
        floatingIcon.animate()
            .translationY(-20f)
            .setDuration(200)
            .withEndAction {
                // Reset position after animation
                floatingIcon.animate()
                    .translationY(0f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }
    
    private fun showDrawerOverlay() {
        if (drawerOverlay == null) {
            drawerOverlay = DrawerOverlay(this, windowManager) { 
                // Callback when overlay is dismissed
                drawerOverlay = null
            }
        }
        drawerOverlay?.show()
    }
    
    private fun getSavedXPosition(): Int {
        val savedX = preferencesManager.widgetXPosition
        return if (savedX == -1) {
            // Default to right edge
            val displayMetrics = resources.displayMetrics
            displayMetrics.widthPixels - 200
        } else {
            savedX
        }
    }
    
    private fun getSavedYPosition(): Int {
        val savedY = preferencesManager.widgetYPosition
        return if (savedY == -1) {
            // Default to middle of screen
            val displayMetrics = resources.displayMetrics
            displayMetrics.heightPixels / 2
        } else {
            savedY
        }
    }
    
    private fun savePosition(x: Int, y: Int) {
        preferencesManager.widgetXPosition = x
        preferencesManager.widgetYPosition = y
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Remove floating widget
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
        
        // Remove drawer overlay if exists
        drawerOverlay?.dismiss()
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}