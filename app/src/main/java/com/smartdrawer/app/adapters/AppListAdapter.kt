package com.smartdrawer.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartdrawer.app.R
import com.smartdrawer.app.data.models.AppInfo

class AppListAdapter(
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: (AppInfo) -> Boolean
) : RecyclerView.Adapter<AppListAdapter.AppViewHolder>() {
    
    private var allApps = listOf<AppInfo>()
    private var filteredApps = listOf<AppInfo>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_grid, parent, false)
        return AppViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = filteredApps[position]
        holder.bind(app)
    }
    
    override fun getItemCount(): Int = filteredApps.size
    
    fun submitList(apps: List<AppInfo>) {
        allApps = apps
        filteredApps = apps
        notifyDataSetChanged()
    }
    
    fun filter(query: String) {
        filteredApps = if (query.isBlank()) {
            allApps
        } else {
            allApps.filter { app ->
                app.appName.contains(query, ignoreCase = true) ||
                        app.packageName.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
    
    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.imageViewAppIcon)
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewAppName)
        private val pinIndicator: ImageView = itemView.findViewById(R.id.imageViewPinIndicator)
        
        fun bind(app: AppInfo) {
            iconImageView.setImageDrawable(app.icon)
            nameTextView.text = app.appName
            
            // Show pin indicator if app is pinned
            pinIndicator.visibility = if (app.isPinned) View.VISIBLE else View.GONE
            
            itemView.setOnClickListener {
                onAppClick(app)
            }
            
            itemView.setOnLongClickListener {
                onAppLongClick(app)
            }
        }
    }
} 