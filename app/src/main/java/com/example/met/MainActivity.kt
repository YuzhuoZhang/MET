package com.example.met
// MainActivity.kt
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var isLaoded: Boolean = false
    private var currentIndex = 0
    private var objectIds: MutableList<Int> = mutableListOf()
    private lateinit var imageView: ImageView
    private lateinit var infoTextView: TextView
    private lateinit var loadButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevArrow: ImageButton
    private lateinit var nextArrow: ImageButton
    private lateinit var artRepository: ArtRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        infoTextView = findViewById(R.id.infoTextView)
        loadButton = findViewById(R.id.loadButton)

        prevArrow = findViewById(R.id.prevArrow)
        nextArrow = findViewById(R.id.nextArrow)

        artRepository = ArtRepositoryImpl()

        loadButton.setOnClickListener {
            lifecycleScope.launch {
                fetchAndDisplayArtObject()
            }
        }


        prevArrow.setOnClickListener {
            lifecycleScope.launch {
                navigateToPreviousArtObject()
            }

        }

        nextArrow.setOnClickListener {
            lifecycleScope.launch {
                navigateToNextArtObject()
            }
        }


    }

    private suspend fun fetchAndDisplayArtObject() {

        if (!isLaoded) {
            val artObject = artRepository.fetchArtObject()
            if (artObject != null) {
                Log.d("fetchAndDisplayArtObject", "Fetched ArtObject: $artObject")
                ArtObjectPresenter(artObject, imageView, infoTextView).present()
                isLaoded = true;
            } else {
                Toast.makeText(this, "Failed to fetch art object", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "the objects are already loaded", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun navigateToPreviousArtObject() {
        val artObject = artRepository.fetchPreviousArtObject()
        if (artObject != null) {
            Log.d("fetchAndDisplayArtObject", "Fetched ArtObject: $artObject")
            ArtObjectPresenter(artObject, imageView, infoTextView).present()

        } else {
            Toast.makeText(this, "Failed to fetch art object", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun navigateToNextArtObject() {
        // Navigate to next art object
        val artObject = artRepository.fetchNextArtObject()
        if (artObject != null) {
            Log.d("fetchAndDisplayArtObject", "Fetched ArtObject: $artObject")
            ArtObjectPresenter(artObject, imageView, infoTextView).present()

        } else {
            Toast.makeText(this, "Failed to fetch art object", Toast.LENGTH_SHORT).show()
        }
    }

}
