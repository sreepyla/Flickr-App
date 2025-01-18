package com.example.flickrimagesapp.data

import com.example.flickrimagesapp.model.FlickrImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject

class FlickrRepositoryImpl @Inject constructor() : FlickrRepository {
    override suspend fun searchImages(query: String): List<FlickrImage> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=$query"
                val response = URL(url).readText()
                val json = JSONObject(response)
                val items = json.getJSONArray("items")

                List(items.length()) { index ->
                    val item = items.getJSONObject(index)
                    val media = item.getJSONObject("media").getString("m")
                    FlickrImage(
                        imageUrl = media,
                        title = item.getString("title"),
                        author = item.getString("author"),
                        publishedDate = item.getString("published")
                    )
                }
            } catch (e: Exception) {
                listOf()
            }
        }
    }
}
