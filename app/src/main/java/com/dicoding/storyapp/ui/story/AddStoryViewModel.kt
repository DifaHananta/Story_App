package com.dicoding.storyapp.ui.story

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.response.RegisterResponse
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.utils.ApiCallbackString
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {

    private val isloading = MutableLiveData<Boolean>()

    fun uploadImage(
        user: User,
        description: RequestBody,
        imageMultipart: MultipartBody.Part,
        callback: ApiCallbackString
    ) {
        ApiConfig.getApiService().addStories("Bearer ${user.token}", description, imageMultipart)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    isloading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            callback.onResponse(response.body() != null, SUCCESS)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("message")
                        callback.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    isloading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                    callback.onResponse(false, t.message.toString())
                }

            })
    }

    companion object {
        private const val TAG = "PostStoryViewModel"
        private const val SUCCESS = "success"
    }
}