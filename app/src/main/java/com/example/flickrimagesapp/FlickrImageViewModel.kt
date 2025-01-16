package com.example.flickrimagesapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

data class FlickrImage(
    val imageUrl: String,
    val title: String,
    val author: String,
    val publishedDate: String
)

class FlickrImageViewModel : ViewModel() {
    var images = mutableStateOf(listOf<FlickrImage>())
        private set
    var isLoading = mutableStateOf(false)
        private set
    var selectedImage = mutableStateOf<FlickrImage?>(null)
        private set

    fun searchImages(query: String) {
        if (query.isBlank()) {
            images.value = emptyList()
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val url =
                        "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=$query"
                    //Log.d("API_URL", url)
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
                   // Log.e("API_ERROR", e.message.toString())
                    listOf()
                }
            }
            images.value = result
            isLoading.value = false
        }
    }

    fun selectImage(image: FlickrImage) {
        selectedImage.value = image
    }

    fun clearSelectedImage() {
        selectedImage.value = null
    }
}

