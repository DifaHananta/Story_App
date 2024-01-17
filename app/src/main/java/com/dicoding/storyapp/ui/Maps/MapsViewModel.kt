package com.dicoding.storyapp.ui.Maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.response.GetAllStoriesResponse
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel() {

    private val mItemStory = MutableLiveData<List<ListStoryItem>>()
    val itemStory: LiveData<List<ListStoryItem>> = mItemStory
    private val isLoading = MutableLiveData<Boolean>()
    private val dataFound = MutableLiveData<Boolean>()

    fun setStoryLocation(token: String, location: Int) {
        isLoading.value = true
        dataFound.value = true
        val client = ApiConfig.getApiService().getAllStories("Bearer $token", location)
        client.enqueue(object : Callback<GetAllStoriesResponse> {
            override fun onResponse(call: Call<GetAllStoriesResponse>, response: Response<GetAllStoriesResponse>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (response != null) {
                        if (!responseBody!!.error) {
                            mItemStory.value = response.body()?.listStory
                            dataFound.value = responseBody.message == "Stories fetched successfully"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetAllStoriesResponse>, t: Throwable) {
                isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getStories() : LiveData<List<ListStoryItem>> {
        return itemStory
    }

    companion object {
        private const val TAG = "MapsViewModel"
    }
}