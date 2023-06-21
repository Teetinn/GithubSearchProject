package id.ac.umn.githubsearchproject.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.prefDataStore by preferencesDataStore("settings")

class SettingPreferences constructor(context: Context) {

    private val settingsDataStore = context.prefDataStore
    private val modeKEY = booleanPreferencesKey("mode_setting")

    fun getModeSetting(): Flow<Boolean> =
        settingsDataStore.data.map { preferences ->
            preferences[modeKEY] ?: false
        }

    suspend fun saveModeSetting(isDarkModeActive: Boolean){
        settingsDataStore.edit { preferences ->
            preferences[modeKEY] = isDarkModeActive
        }
    }
}