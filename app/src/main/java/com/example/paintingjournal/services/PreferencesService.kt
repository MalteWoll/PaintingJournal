package com.example.paintingjournal.services

import android.content.Context
import android.content.SharedPreferences

interface PreferencesService {
    fun saveData(key: String, value: String, context: Context)
    fun saveData(key: String, value: Boolean, context: Context)
    fun getData(key: String, defaultValue: String, context: Context) : String
    fun getData(key: String, defaultValue: Boolean, context: Context) : Boolean
}