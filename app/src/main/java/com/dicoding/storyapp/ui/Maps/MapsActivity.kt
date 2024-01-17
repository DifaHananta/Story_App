package com.dicoding.storyapp.ui.Maps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.local.User
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.ui.main.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var user: User
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>("user") ?: User("", "")

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        viewModel.setStoryLocation(user.token, 1)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        storyLocation()
    }

    private fun storyLocation() {
        viewModel.getStories().observe(this) { stories ->
            stories?.let {
                for (story in it) {
                    val latLng = LatLng(story.lat!!, story.lon!!)
                    mMap.addMarker(MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                    )

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                }
            }
        }
    }
}