package com.example.tempstuff

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class LocalDataManager(context: Context) {

    val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val USER_NAME = "user_name"

    }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        preferences.registerOnSharedPreferenceChangeListener(listener)
    }


    fun updateStringField(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun removeField(key: String) {
        preferences.edit().remove(key).apply()
    }



    fun saveProfile(
        name: String
    ) {
        val editor = preferences.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }
}