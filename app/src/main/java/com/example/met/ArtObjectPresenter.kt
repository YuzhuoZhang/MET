package com.example.met
// ArtObjectPresenter.kt
import android.widget.ImageView
import android.widget.TextView

class ArtObjectPresenter(
    private val artObject: ArtObject,
    private val imageView: ImageView,
    private val infoTextView: TextView
) {
    fun present() {
        // Load image
        ImageLoader(imageView).loadImage(artObject.primaryImage)

        // Populate TextView with object information
        val info = "Title: ${artObject.title}\n" +
                "Culture: ${artObject.culture}\n" +
                "Period: ${artObject.period}\n" +
                "Dynasty: ${artObject.dynasty}\n" +
                "Department: ${artObject.department}"
        infoTextView.text = info
    }
}
