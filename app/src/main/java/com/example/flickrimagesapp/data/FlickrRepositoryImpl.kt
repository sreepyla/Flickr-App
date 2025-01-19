package com.example.flickrimagesapp.data

import android.util.Log
import com.example.flickrimagesapp.model.FlickrImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor() : FlickrRepository {
    override suspend fun searchImages(query: String): List<FlickrImage> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=$query")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("FlickrRepository", "Error: HTTP ${connection.responseCode}")
                    return@withContext emptyList()
                }

                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val items = json.getJSONArray("items")

                List(items.length()) { index ->
                    val item = items.getJSONObject(index)
                    val media = item.getJSONObject("media").getString("m")
                    FlickrImage(
                        imageUrl = media,
                        title = item.optString("title", "Untitled"),
                        author = item.optString("author", "Unknown"),
                        publishedDate = item.optString("published", "N/A")
                    )
                }
            } catch (e: Exception) {
                Log.e("FlickrRepository", "API Call Failed: ${e.message}")
                emptyList()
            }
        }
    }
}
