package com.dicoding.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.response.ListStoryItem

class DetailStoryViewModel : ViewModel() {

    var storyItem: ListStoryItem? = null

    fun setDetailStory(storyItem: ListStoryItem) {
        this.storyItem = storyItem
    }

}