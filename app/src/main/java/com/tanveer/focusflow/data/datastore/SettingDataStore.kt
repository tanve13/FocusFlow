package com.tanveer.focusflow.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

private const val NAME = "focusflow_prefs"
val Context.dataStore by preferencesDataStore(NAME)

class SettingDataStore(private val context: Context) {
    companion object {
        val KEY_USE_DARK = booleanPreferencesKey("use_dark")
        val KEY_FOCUS_MIN = intPreferencesKey("focus_minutes")
        val KEY_BREAK_MIN = intPreferencesKey("break_minutes")
    }

    val useDarkFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_USE_DARK] ?: true
    }

    suspend fun setUseDark(use: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USE_DARK] = use
        }
    }

    val focusDurationFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_FOCUS_MIN] ?: 25
    }

    suspend fun setFocusMinutes(min: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_FOCUS_MIN] = min }
    }

    val breakDurationFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_BREAK_MIN] ?: 5
    }

    suspend fun setBreakMinutes(min: Int) {
        context.dataStore.edit { prefs -> prefs[KEY_BREAK_MIN] = min }
    }
}
