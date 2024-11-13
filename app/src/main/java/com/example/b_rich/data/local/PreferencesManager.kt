package com.example.b_rich.data.local

import android.content.SharedPreferences


class PreferencesManager(private val sharedPreferences: SharedPreferences) {

    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}