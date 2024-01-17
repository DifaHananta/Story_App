package com.dicoding.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var storyItem: ListStoryItem
    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailStoryViewModel::class.java]

        val name = intent.getStringExtra(EXTRA_NAME)
        val image = intent.getStringExtra(EXTRA_IMAGE)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val lon = intent.getDoubleExtra(EXTRA_LON, 0.0)
        val lat = intent.getDoubleExtra(EXTRA_LAT, 0.0)
        storyItem = ListStoryItem("", image, name, desc, lon, lat)

        viewModel.setDetailStory(storyItem)

        getDetailStory()
    }

    private fun getDetailStory() {
        with(binding) {
            tvItemName.text = storyItem.name
            tvItemDesc.text = storyItem.description
            Glide.with(ivItemPhoto)
                .load(storyItem.photoUrl)
                .into(ivItemPhoto)
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_LON = "extra_lon"
        const val EXTRA_LAT = "extra_lat"
    }
}