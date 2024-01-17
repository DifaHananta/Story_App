package com.dicoding.storyapp.ui.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.response.RegisterResponse
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.utils.ApiCallbackString
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val IsLoading = MutableLiveData<Boolean>()

    fun register(name: String, email: String, password: String, callback: ApiCallbackString) {
        ApiConfig.getApiService().register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    IsLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error)
                            callback.onResponse(response.body() != null, SUCCESS)
                    } else {
                        Log.e(TAG, "onFailure1: ${response.message()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("message")
                        callback.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    IsLoading.value = false
                    Log.e(TAG, "OnFailure2: ${t.message}")
                    callback.onResponse(false, t.message.toString())
                }
            })
    }

    companion object {
        private const val TAG = "RegisterActivityViewModel"
        private const val SUCCESS = "success"
    }

}