package com.dicoding.storyapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.data.local.UserPreference
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.data.retrofit.ApiConfig
import com.dicoding.storyapp.utils.ApiCallbackString
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()

    fun login(email: String, password: String, callback: ApiCallbackString) {
        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            callback.onResponse(response.body() != null, SUCCESS)
                            val model = User(
                                responseBody.loginResult.name,
                                responseBody.loginResult.token
                            )
                            saveUser(model)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("message")
                        callback.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                    callback.onResponse(false, t.message.toString())
                }
            })
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val SUCCESS = "success"
    }
}