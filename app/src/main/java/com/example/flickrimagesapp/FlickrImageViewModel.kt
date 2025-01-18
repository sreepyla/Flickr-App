package com.example.flickrimagesapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flickrimagesapp.data.FlickrRepository
import com.example.flickrimagesapp.model.FlickrImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlickrImageViewModel @Inject constructor(
    private val flickrRepository: FlickrRepository
) : ViewModel() {

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
            val result = flickrRepository.searchImages(query)
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



