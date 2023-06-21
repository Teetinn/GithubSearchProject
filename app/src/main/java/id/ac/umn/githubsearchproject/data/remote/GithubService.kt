package id.ac.umn.githubsearchproject.data.remote

import id.ac.umn.githubsearchproject.data.model.ResponseUser
import id.ac.umn.githubsearchproject.data.model.ResponseUserDetail
import retrofit2.http.*

interface GithubService {

    @JvmSuppressWildcards
    @GET("users")
    @Headers("Authorization: token GITHUB_PERSONAL_TOKEN")
    suspend fun getUserGithub(
    ): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    @Headers("Authorization: token  GITHUB_PERSONAL_TOKEN")
    suspend fun getUserDetail(@Path("username") username: String): ResponseUserDetail

    @JvmSuppressWildcards
    @Headers("Authorization: token  GITHUB_PERSONAL_TOKEN")
    @GET("users/{username}/followers")
    suspend fun getFollowerUser(@Path("username") username: String): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @Headers("Authorization: token  GITHUB_PERSONAL_TOKEN")
    @GET("users/{username}/following")
    suspend fun getFollowingUser(@Path("username") username: String): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @Headers("Authorization: token  GITHUB_PERSONAL_TOKEN")
    @GET("search/users")
    suspend fun searchUser(@QueryMap params:Map<String, Any>): ResponseUser
}