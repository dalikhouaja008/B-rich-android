package com.example.b_rich.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferencesManager private constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        fun create(context: Context): PreferencesManager {
            // Create a MasterKey for encryption
            val masterKeyAlias = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            // Create EncryptedSharedPreferences
            val sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "MyEncryptedPrefs", // Name of the preferences file
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            return PreferencesManager(sharedPreferences)
        }
    }

    // Save a string value to SharedPreferences
    fun saveData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    // Retrieve a string value from SharedPreferences
    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}