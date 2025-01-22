package com.example.flickrimagesapp.data

import com.example.flickrimagesapp.model.FlickrImage

interface FlickrRepository {
    suspend fun searchImages(query: String): List<FlickrImage>
}

