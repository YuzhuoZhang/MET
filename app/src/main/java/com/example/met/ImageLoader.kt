package com.example.met
// ImageLoader.kt
import android.util.Log
import android.widget.ImageView
import coil.load

class ImageLoader(private val imageView: ImageView) {
    fun loadImage(imageUrl: String?) {
        // Use Coil library to load image into ImageView

        if (imageUrl != null) {
            if (imageUrl.isNotBlank()) {
                imageView.load(imageUrl) {
                    // Placeholder image
                    crossfade(true) // Enable crossfade animation
                }
            } else {
                imageView.setImageResource(R.drawable.placeholder_image)
                Log.e("ImageLoader", "Blank imageUrl parameter")
            }
        } else {
            imageView.setImageResource(R.drawable.placeholder_image)
            Log.e("ImageLoader", "Null imageUrl parameter")
        }
    }
}
