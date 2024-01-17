package com.dicoding.storyapp.data.retrofit

import com.dicoding.storyapp.data.response.GetAllStoriesResponse
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1
    ): Call<GetAllStoriesResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part("description") des: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getPagingStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): GetAllStoriesResponse
}