package com.fintrack.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val darkMode: Boolean = true,
    val currency: String = "₹",
    val notificationsEnabled: Boolean = true
)
