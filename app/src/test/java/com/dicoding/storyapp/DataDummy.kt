package com.dicoding.storyapp

import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.data.response.LoginResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val newsList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val news = ListStoryItem(
                "story-NulgY-EKAbG9Fi7Z",
                "https://story-api.dicoding.dev/images/stories/photos-1664975420298_dvOgzVcG.jpg",
                "Gwehj",
                "awwww",
                null,
                null
            )
            newsList.add(news)
        }
        return newsList
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            LoginResult(
                "asep",
                "user-z4H1i9K1JHUDffl_",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXo0SDFpOUsxSkhVRGZmbF8iLCJpYXQiOjE2NjQ5NzcyNzB9.qJvEARVz2k4qTbI5YbgpVMBoz_RNbiNOGbMzlXX3EPo"
            ),
            false,
            "success"
        )
    }

    fun generateDummyUser(): User {
        return User(
            "asep",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXo0SDFpOUsxSkhVRGZmbF8iLCJpYXQiOjE2NjYzNjgzMjh9.VicxuSby4QgMbz9ajTgvNF6GovPHTs6bQROZqEegC2g"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }
}