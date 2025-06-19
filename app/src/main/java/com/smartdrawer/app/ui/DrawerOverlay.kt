package com.smartdrawer.app.ui

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartdrawer.app.R
import com.smartdrawer.app.adapters.AppListAdapter
import com.smartdrawer.app.data.models.AppInfo
import com.smartdrawer.app.utils.PreferencesManager
import com.smartdrawer.app.viewmodels.AppDrawerViewModel

class DrawerOverlay(
    private val context: Context,
    private val windowManager: WindowManager,
    private val onDismiss: () -> Unit
) {
    
    private lateinit var overlayView: View
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noAppsTextView: TextView
    private lateinit var appAdapter: AppListAdapter
    private lateinit var params: WindowManager.LayoutParams
    private lateinit var preferencesManager: PreferencesManager
    private var isShowing = false
    
    init {
        preferencesManager = PreferencesManager(context)
        createOverlayView()
    }
    
    private fun createOverlayView() {
        overlayView = LayoutInflater.from(context).inflate(R.layout.drawer_overlay, null)
        
        // Initialize views
        searchEditText = overlayView.findViewById(R.id.editTextSearch)
        recyclerView = overlayView.findViewById(R.id.recyclerViewApps)
        progressBar = overlayView.findViewById(R.id.progressBar)
        noAppsTextView = overlayView.findViewById(R.id.textViewNoApps)
        
        // Set up layout parameters
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        
        setupViews()
        setupRecyclerView()
        setupSearchFunctionality()
    }
    
    private fun setupViews() {
        // Handle back button press
        overlayView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dismiss()
                true
            } else {
                false
            }
        }
        
        // Handle touch outside to dismiss
        overlayView.setOnTouchListener { _, _ ->
            dismiss()
            true
        }
        
        // Prevent RecyclerView touches from dismissing
        recyclerView.setOnTouchListener { _, _ -> true }
        searchEditText.setOnTouchListener { _, _ -> true }
    }
    
    private fun setupRecyclerView() {
        appAdapter = AppListAdapter(
            onAppClick = { appInfo ->
                launchApp(appInfo)
            },
            onAppLongClick = { appInfo ->
                // Toggle pin status
                // This would be handled by ViewModel
                true
            }
        )
        
        // Set layout manager based on preferences
        recyclerView.layoutManager = if (preferencesManager.layoutType == PreferencesManager.LAYOUT_GRID) {
            GridLayoutManager(context, 4)
        } else {
            LinearLayoutManager(context)
        }
        
        recyclerView.adapter = appAdapter
    }
    
    private fun setupSearchFunctionality() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter apps based on search query
                filterApps(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun filterApps(query: String) {
        // This would typically use the ViewModel to filter apps
        // For now, we'll implement basic filtering in the adapter
        appAdapter.filter(query)
        
        // Show/hide no apps message
        val hasResults = appAdapter.itemCount > 0
        noAppsTextView.visibility = if (hasResults) View.GONE else View.VISIBLE
    }
    
    private fun launchApp(appInfo: AppInfo) {
        // Launch the app
        val intent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
        intent?.let {
            it.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(it)
            dismiss()
        }
    }
    
    fun show() {
        if (!isShowing) {
            try {
                windowManager.addView(overlayView, params)
                isShowing = true
                
                // Focus search and show keyboard
                searchEditText.requestFocus()
                showKeyboard()
                
                // Load apps (this would typically be done through ViewModel)
                loadApps()
                
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
    
    fun dismiss() {
        if (isShowing) {
            try {
                hideKeyboard()
                windowManager.removeView(overlayView)
                isShowing = false
                onDismiss()
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
    
    private fun showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }
    
    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
    
    private fun loadApps() {
        // Show loading
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        
        // This is a simplified version - in the real app, this would use ViewModel
        // For now, we'll create a basic implementation
        
        // Hide loading after simulated load
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
} 