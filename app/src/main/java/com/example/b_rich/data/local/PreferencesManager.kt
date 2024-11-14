package com.example.b_rich.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.b_rich.ui.theme.PREF_FILE

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
                PREF_FILE, // Name of the preferences file
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

    fun saveDataBool(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // Retrieve a string value from SharedPreferences
    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getDataBool(key: String, defaultValue: Boolean): Boolean  {
        return sharedPreferences.getBoolean(key, defaultValue) ?: defaultValue
    }


}