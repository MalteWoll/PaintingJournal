package com.example.paintingjournal.services

import android.content.Context
import android.content.SharedPreferences

class PreferencesServiceImpl: PreferencesService {
    override fun saveData(key: String, value: String, context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun saveData(key: String, value: Boolean, context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun getData(key: String, defaultValue: String, context: Context) : String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun getData(key: String, defaultValue: Boolean, context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}