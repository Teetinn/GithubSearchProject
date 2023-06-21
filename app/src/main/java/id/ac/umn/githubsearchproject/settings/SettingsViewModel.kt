package id.ac.umn.githubsearchproject.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.ac.umn.githubsearchproject.data.local.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref:SettingPreferences):ViewModel() {

    fun getMode() = pref.getModeSetting().asLiveData()

    fun saveMode(isDark:Boolean){
        viewModelScope.launch{
            pref.saveModeSetting(isDark)
        }
    }

    class Factory(private val pref:SettingPreferences): ViewModelProvider.NewInstanceFactory(){
        override fun <T:ViewModel> create(modelClass:Class<T>): T = SettingsViewModel(pref) as T
    }
}