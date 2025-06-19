package com.smartdrawer.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.smartdrawer.app.R
import com.smartdrawer.app.databinding.ActivityMainBinding
import com.smartdrawer.app.services.FloatingWidgetService
import com.smartdrawer.app.services.GestureDetectionService
import com.smartdrawer.app.utils.PermissionsUtils
import com.smartdrawer.app.utils.PreferencesManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager
    
    companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 1001
        private const val REQUEST_USAGE_STATS_PERMISSION = 1002
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferencesManager = PreferencesManager(this)
        
        setupViews()
        checkPermissions()
    }
    
    override fun onResume() {
        super.onResume()
        checkPermissions()
    }
    
    private fun setupViews() {
        binding.buttonRequestOverlay.setOnClickListener {
            PermissionsUtils.requestOverlayPermission(this, REQUEST_OVERLAY_PERMISSION)
        }
        
        binding.buttonRequestAccessibility.setOnClickListener {
            PermissionsUtils.requestAccessibilityPermission(this)
        }
        
        binding.buttonStartService.setOnClickListener {
            startFloatingService()
        }
        
        binding.buttonSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    
    private fun checkPermissions() {
        updateOverlayPermissionStatus()
        updateAccessibilityPermissionStatus()
        updateStartButtonState()
    }
    
    private fun updateOverlayPermissionStatus() {
        val hasOverlayPermission = PermissionsUtils.canDrawOverlays(this)
        
        if (hasOverlayPermission) {
            binding.textViewOverlayStatus.text = getString(R.string.overlay_permission_granted)
            binding.textViewOverlayStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.buttonRequestOverlay.visibility = android.view.View.GONE
        } else {
            binding.textViewOverlayStatus.text = getString(R.string.overlay_permission_denied)
            binding.textViewOverlayStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.buttonRequestOverlay.visibility = android.view.View.VISIBLE
        }
    }
    
    private fun updateAccessibilityPermissionStatus() {
        val hasAccessibilityPermission = PermissionsUtils.isAccessibilityServiceEnabled(this, GestureDetectionService::class.java)
        
        if (hasAccessibilityPermission) {
            binding.textViewAccessibilityStatus.text = getString(R.string.accessibility_permission_granted)
            binding.textViewAccessibilityStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.buttonRequestAccessibility.visibility = android.view.View.GONE
        } else {
            binding.textViewAccessibilityStatus.text = getString(R.string.accessibility_permission_denied)
            binding.textViewAccessibilityStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.buttonRequestAccessibility.visibility = android.view.View.VISIBLE
        }
    }
    
    private fun updateStartButtonState() {
        val hasOverlay = PermissionsUtils.canDrawOverlays(this)
        val hasAccessibility = PermissionsUtils.isAccessibilityServiceEnabled(this, GestureDetectionService::class.java)
        
        val canStart = hasOverlay && (hasAccessibility || !preferencesManager.isSwipeUpEnabled)
        binding.buttonStartService.isEnabled = canStart
        
        if (canStart) {
            binding.buttonStartService.text = if (isServiceRunning()) {
                getString(R.string.restart_smart_drawer)
            } else {
                getString(R.string.start_smart_drawer)
            }
        } else {
            binding.buttonStartService.text = getString(R.string.missing_permissions)
        }
    }
    
    private fun startFloatingService() {
        if (preferencesManager.isFloatingWidgetEnabled) {
            val intent = Intent(this, FloatingWidgetService::class.java)
            startService(intent)
            Toast.makeText(this, getString(R.string.smart_drawer_started), Toast.LENGTH_SHORT).show()
            
            // Minimize the app
            moveTaskToBack(true)
        } else {
            Toast.makeText(this, getString(R.string.floating_widget_disabled), Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun isServiceRunning(): Boolean {
        // Check if FloatingWidgetService is running
        val activityManager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (FloatingWidgetService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            REQUEST_OVERLAY_PERMISSION -> {
                checkPermissions()
            }
            REQUEST_USAGE_STATS_PERMISSION -> {
                checkPermissions()
            }
        }
    }
}