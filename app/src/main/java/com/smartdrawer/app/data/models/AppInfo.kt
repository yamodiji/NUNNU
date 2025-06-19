package com.smartdrawer.app.data.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
    val isSystemApp: Boolean = false,
    val isPinned: Boolean = false
) 