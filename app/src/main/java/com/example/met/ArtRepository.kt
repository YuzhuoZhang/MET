package com.example.met

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

// ArtRepository.kt
interface ArtRepository {
    suspend fun fetchArtObject(): ArtObject?
    suspend fun fetchNextArtObject(): ArtObject?
    suspend fun fetchPreviousArtObject(): ArtObject?
}

class ArtRepositoryImpl : ArtRepository {

    private var objectIds: MutableList<Int> = mutableListOf()
    private var currentIndex = 0

    override suspend fun fetchArtObject(): ArtObject? {
        // If objectIds is empty, fetch the list of object IDs
        if (objectIds.isEmpty()) {
            fetchObjectIds()
        }

        // If objectIds is still empty, return null
        if (objectIds.isEmpty()) return null

        // Get the current object ID
        val objectId = objectIds[currentIndex]


        // Fetch details of the art object using the object ID
        return fetchArtObjectDetail(objectId);
    }

    override suspend fun fetchNextArtObject(): ArtObject? {
        // If objectIds is empty, fetch the list of object IDs
        if (objectIds.isEmpty()) {
            fetchObjectIds()
        }

        // If objectIds is still empty, return null
        if (objectIds.isEmpty()) return null
        currentIndex = (currentIndex + 1) % objectIds.size
        // Get the current object ID
        val objectId = objectIds[currentIndex]

        // Increment currentIndex and loop back if necessary

        // Fetch details of the art object using the object ID
        return fetchArtObjectDetail(objectId);
    }

    override suspend fun fetchPreviousArtObject(): ArtObject? {
        // If objectIds is empty, fetch the list of object IDs
        if (objectIds.isEmpty()) {
            fetchObjectIds()
        }

        // If objectIds is still empty, return null
        if (objectIds.isEmpty()) return null
        currentIndex =
            (currentIndex - 1 + objectIds.size) % objectIds.size // decrement currentIndex and loop back if necessary
        // Get the current object ID
        val objectId = objectIds[currentIndex]


        // Fetch details of the art object using the object ID
        return fetchArtObjectDetail(objectId);
    }

    private suspend fun fetchObjectIds() {
        withContext(Dispatchers.IO) {
            val url = URL("https://collectionapi.metmuseum.org/public/collection/v1/objects")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val bufferedReader = inputStream.bufferedReader()
                val response = bufferedReader.use { it.readText() }

                val responseObject = Gson().fromJson(response, JsonObject::class.java)
                objectIds =
                    responseObject.get("objectIDs").asJsonArray.map { it.asInt }.toMutableList()
                // Log the fetched IDs
                Log.d("FetchObjectIds", "Fetched IDs: $objectIds")
            }

            connection.disconnect()
        }
    }

    private suspend fun fetchArtObjectDetail(objectId: Int): ArtObject? {
        return withContext(Dispatchers.IO) {
            val url =
                URL("https://collectionapi.metmuseum.org/public/collection/v1/objects/$objectId")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val bufferedReader = inputStream.bufferedReader()
                val response = bufferedReader.use { it.readText() }

                val artObject = Gson().fromJson(response, ArtObject::class.java)
                artObject
            } else {
                null
            }
        }
    }
}