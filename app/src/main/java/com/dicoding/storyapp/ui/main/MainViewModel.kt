package com.dicoding.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.paging.StoryRepository
import com.dicoding.storyapp.data.response.ListStoryItem

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val itemStory: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}