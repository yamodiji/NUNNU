package com.smartdrawer.app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartdrawer.app.data.models.AppInfo
import com.smartdrawer.app.utils.AppInfoUtils
import com.smartdrawer.app.utils.PreferencesManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AppDrawerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferencesManager = PreferencesManager(application)
    
    private val _allApps = MutableLiveData<List<AppInfo>>()
    val allApps: LiveData<List<AppInfo>> = _allApps
    
    private val _filteredApps = MutableLiveData<List<AppInfo>>()
    val filteredApps: LiveData<List<AppInfo>> = _filteredApps
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery
    
    private var loadAppsJob: Job? = null
    private var currentAppsList: List<AppInfo> = emptyList()
    
    init {
        loadInstalledApps()
    }
    
    fun loadInstalledApps() {
        loadAppsJob?.cancel()
        loadAppsJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                val apps = AppInfoUtils.getInstalledApps(getApplication())
                val filteredApps = if (preferencesManager.showSystemApps) {
                    apps
                } else {
                    apps.filter { !it.isSystemApp }
                }
                
                // Add pinned status
                val appsWithPinnedStatus = filteredApps.map { app ->
                    app.copy(isPinned = preferencesManager.isAppPinned(app.packageName))
                }
                
                // Sort: pinned apps first, then alphabetically
                val sortedApps = appsWithPinnedStatus.sortedWith(
                    compareByDescending<AppInfo> { it.isPinned }
                        .thenBy { it.appName.lowercase() }
                )
                
                currentAppsList = sortedApps
                _allApps.value = sortedApps
                
                // Apply current search filter
                val currentQuery = _searchQuery.value
                if (currentQuery.isNullOrBlank()) {
                    _filteredApps.value = sortedApps
                } else {
                    filterApps(currentQuery)
                }
            } catch (e: Exception) {
                // Handle error
                _allApps.value = emptyList()
                _filteredApps.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchApps(query: String) {
        _searchQuery.value = query
        filterApps(query)
    }
    
    private fun filterApps(query: String) {
        if (query.isBlank()) {
            _filteredApps.value = currentAppsList
        } else {
            val filtered = currentAppsList.filter { app ->
                app.appName.contains(query, ignoreCase = true) ||
                        app.packageName.contains(query, ignoreCase = true)
            }
            _filteredApps.value = filtered
        }
    }
    
    fun launchApp(packageName: String) {
        AppInfoUtils.launchApp(getApplication(), packageName)
    }
    
    fun toggleAppPin(packageName: String) {
        if (preferencesManager.isAppPinned(packageName)) {
            preferencesManager.removePinnedApp(packageName)
        } else {
            preferencesManager.addPinnedApp(packageName)
        }
        loadInstalledApps() // Refresh the list
    }
    
    fun updateShowSystemApps(showSystemApps: Boolean) {
        preferencesManager.showSystemApps = showSystemApps
        loadInstalledApps()
    }
    
    fun getLayoutType(): Int = preferencesManager.layoutType
    
    fun setLayoutType(layoutType: Int) {
        preferencesManager.layoutType = layoutType
    }
}