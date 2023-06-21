package id.ac.umn.githubsearchproject

import android.util.Log
import androidx.lifecycle.*
import id.ac.umn.githubsearchproject.data.local.SettingPreferences
import id.ac.umn.githubsearchproject.data.remote.ApiClient
import id.ac.umn.githubsearchproject.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences): ViewModel() {

    val resultUser = MutableLiveData<Result>()

    fun getMode() = preferences.getModeSetting().asLiveData()

    fun getUser(){
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .githubService
                        .getUserGithub()
                    emit(response)
                }.onStart {
                   resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e("ERROR", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it)
                }
            }
        }
    }

    fun getUser(username : String){
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .githubService
                        .searchUser(mapOf(
                            "q" to username
                        )
                    )
                    emit(response)
                }.onStart {
                    resultUser.value = Result.Loading(true)
                }.onCompletion {
                    resultUser.value = Result.Loading(false)
                }.catch {
                    Log.e("ERROR", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Result.Error(it)
                }.collect {
                    resultUser.value = Result.Success(it.items)
                }
            }
        }
    }

    class Factory(private val preferences:SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
        override fun<T: ViewModel>create(modelClass:Class<T>):T = MainViewModel(preferences) as T
    }
}