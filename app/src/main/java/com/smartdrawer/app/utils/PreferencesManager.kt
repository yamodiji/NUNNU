package com.smartdrawer.app.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "smart_drawer_prefs"
        
        // Keys
        private const val KEY_FLOATING_WIDGET_ENABLED = "floating_widget_enabled"
        private const val KEY_SWIPE_UP_ENABLED = "swipe_up_enabled"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"
        private const val KEY_LAYOUT_TYPE = "layout_type"
        private const val KEY_WIDGET_SIZE = "widget_size"
        private const val KEY_WIDGET_X_POSITION = "widget_x_position"
        private const val KEY_WIDGET_Y_POSITION = "widget_y_position"
        private const val KEY_SHOW_SYSTEM_APPS = "show_system_apps"
        private const val KEY_PINNED_APPS = "pinned_apps"
        
        // Layout types
        const val LAYOUT_GRID = 0
        const val LAYOUT_LIST = 1
        
        // Widget sizes
        const val WIDGET_SIZE_SMALL = 0
        const val WIDGET_SIZE_MEDIUM = 1
        const val WIDGET_SIZE_LARGE = 2
    }
    
    var isFloatingWidgetEnabled: Boolean
        get() = prefs.getBoolean(KEY_FLOATING_WIDGET_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_FLOATING_WIDGET_ENABLED, value).apply()
    
    var isSwipeUpEnabled: Boolean
        get() = prefs.getBoolean(KEY_SWIPE_UP_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SWIPE_UP_ENABLED, value).apply()
    
    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE_ENABLED, value).apply()
    
    var layoutType: Int
        get() = prefs.getInt(KEY_LAYOUT_TYPE, LAYOUT_GRID)
        set(value) = prefs.edit().putInt(KEY_LAYOUT_TYPE, value).apply()
    
    var widgetSize: Int
        get() = prefs.getInt(KEY_WIDGET_SIZE, WIDGET_SIZE_MEDIUM)
        set(value) = prefs.edit().putInt(KEY_WIDGET_SIZE, value).apply()
    
    var widgetXPosition: Int
        get() = prefs.getInt(KEY_WIDGET_X_POSITION, -1)
        set(value) = prefs.edit().putInt(KEY_WIDGET_X_POSITION, value).apply()
    
    var widgetYPosition: Int
        get() = prefs.getInt(KEY_WIDGET_Y_POSITION, -1)
        set(value) = prefs.edit().putInt(KEY_WIDGET_Y_POSITION, value).apply()
    
    var showSystemApps: Boolean
        get() = prefs.getBoolean(KEY_SHOW_SYSTEM_APPS, false)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_SYSTEM_APPS, value).apply()
    
    fun getPinnedApps(): Set<String> {
        return prefs.getStringSet(KEY_PINNED_APPS, emptySet()) ?: emptySet()
    }
    
    fun setPinnedApps(pinnedApps: Set<String>) {
        prefs.edit().putStringSet(KEY_PINNED_APPS, pinnedApps).apply()
    }
    
    fun addPinnedApp(packageName: String) {
        val currentPinned = getPinnedApps().toMutableSet()
        currentPinned.add(packageName)
        setPinnedApps(currentPinned)
    }
    
    fun removePinnedApp(packageName: String) {
        val currentPinned = getPinnedApps().toMutableSet()
        currentPinned.remove(packageName)
        setPinnedApps(currentPinned)
    }
    
    fun isAppPinned(packageName: String): Boolean {
        return getPinnedApps().contains(packageName)
    }
}