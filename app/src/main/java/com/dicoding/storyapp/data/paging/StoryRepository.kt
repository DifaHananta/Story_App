package com.dicoding.storyapp.data.paging

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.local.UserPreference
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryRepository(private val dataStore: DataStore<Preferences>, private val apiService: ApiService, private val storyDatabase: StoryDatabase) {

    private val userPreference: UserPreference = UserPreference.getInstance(dataStore)

    private val tokenFlow: Flow<String> = userPreference.getUser().map { it.token }

    private val tokenLiveData: LiveData<String> = tokenFlow.asLiveData()

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return tokenLiveData.switchMap { token ->
            Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
                pagingSourceFactory = {
//                    StoryPagingSource(token, apiService)
                    storyDatabase.storyDao().getAllStory()
                }
            ).liveData
        }
    }

}