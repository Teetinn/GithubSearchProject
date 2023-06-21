package id.ac.umn.githubsearchproject.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import id.ac.umn.githubsearchproject.data.local.DbModule
import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.data.remote.ApiClient
import id.ac.umn.githubsearchproject.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db:DbModule) : ViewModel() {
    val resultUserDetail = MutableLiveData<Result>()
    val resultFollowerUser = MutableLiveData<Result>()
    val resultFollowingUser = MutableLiveData<Result>()
    val resultFavSuccess = MutableLiveData<Boolean>()
    val resultFavDelete = MutableLiveData<Boolean>()

    private var isFavorite = false

    fun setFavorite(item: ResponseUser.Item?){
        viewModelScope.launch{
            item?.let{
                if(isFavorite){
                    db.userDao.delete(item)
                    resultFavDelete.value = true
                }else{
                    db.userDao.insert(item)
                    resultFavSuccess.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun checkFavorite(id : Int, listenerFavorite:() -> Unit){
        viewModelScope.launch{
            val user = db.userDao.findById(id)
            if(user != null){
                listenerFavorite()
                isFavorite = true
            }
        }
    }

    fun getUserDetail(username : String){
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .githubService
                        .getUserDetail(username)

                    emit(response)
                }.onStart {
                    resultUserDetail.value = Result.Loading(true)
                }.onCompletion {
                    resultUserDetail.value = Result.Loading(false)
                }.catch {
                    Log.e("ERROR", it.message.toString())
                    it.printStackTrace()
                    resultUserDetail.value = Result.Error(it)
                }.collect {
                    resultUserDetail.value = Result.Success(it)
                }
            }
        }
    }

    fun getFollowers(username : String){
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .githubService
                        .getFollowerUser(username)

                    emit(response)
                }.onStart {
                    resultFollowerUser.value = Result.Loading(true)
                }.onCompletion {
                    resultFollowerUser.value = Result.Loading(false)
                }.catch {
                    Log.e("ERROR", it.message.toString())
                    it.printStackTrace()
                    resultFollowerUser.value = Result.Error(it)
                }.collect {
                    resultFollowerUser.value = Result.Success(it)
                }
            }
        }
    }

    fun getFollowing(username: String){
        viewModelScope.launch {
            launch(Dispatchers.Main) {
                flow {
                    val response = ApiClient
                        .githubService
                        .getFollowingUser(username)
                    emit(response)
                }.onStart {
                    resultFollowingUser.value = Result.Loading(true)
                }.onCompletion {
                    resultFollowingUser.value = Result.Loading(false)
                }.catch {
                    Log.e("ERROR", it.message.toString())
                    it.printStackTrace()
                    resultFollowingUser.value = Result.Error(it)
                }.collect {
                    resultFollowingUser.value = Result.Success(it)
                }
            }
        }
    }
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory(){
        override fun<T: ViewModel>create(modelClass:Class<T>):T = DetailViewModel(db) as T
    }
}