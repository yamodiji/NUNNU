package com.smartdrawer.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.smartdrawer.app.R
import com.smartdrawer.app.services.FloatingWidgetService
import com.smartdrawer.app.utils.PreferencesManager

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings_title)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        
        private lateinit var preferencesManager: PreferencesManager

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            
            preferencesManager = PreferencesManager(requireContext())
            
            setupPreferences()
        }

        private fun setupPreferences() {
            // Floating Widget Toggle
            findPreference<SwitchPreferenceCompat>("floating_widget_enabled")?.setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                preferencesManager.isFloatingWidgetEnabled = enabled
                
                if (enabled) {
                    // Start service if enabled
                    val intent = Intent(requireContext(), FloatingWidgetService::class.java)
                    requireContext().startService(intent)
                } else {
                    // Stop service if disabled
                    val intent = Intent(requireContext(), FloatingWidgetService::class.java)
                    requireContext().stopService(intent)
                }
                true
            }
            
            // Swipe Up Gesture Toggle
            findPreference<SwitchPreferenceCompat>("swipe_up_enabled")?.setOnPreferenceChangeListener { _, newValue ->
                preferencesManager.isSwipeUpEnabled = newValue as Boolean
                true
            }
            
            // Dark Mode Toggle
            findPreference<SwitchPreferenceCompat>("dark_mode_enabled")?.setOnPreferenceChangeListener { _, newValue ->
                preferencesManager.isDarkModeEnabled = newValue as Boolean
                // Restart activity to apply theme
                requireActivity().recreate()
                true
            }
            
            // Show System Apps Toggle
            findPreference<SwitchPreferenceCompat>("show_system_apps")?.setOnPreferenceChangeListener { _, newValue ->
                preferencesManager.showSystemApps = newValue as Boolean
                true
            }
            
            // Reset Widget Position
            findPreference<Preference>("reset_widget_position")?.setOnPreferenceClickListener {
                preferencesManager.widgetXPosition = -1
                preferencesManager.widgetYPosition = -1
                Toast.makeText(requireContext(), getString(R.string.widget_position_reset), Toast.LENGTH_SHORT).show()
                
                // Restart floating service to apply new position
                val intent = Intent(requireContext(), FloatingWidgetService::class.java)
                requireContext().stopService(intent)
                requireContext().startService(intent)
                true
            }
        }
    }
} 