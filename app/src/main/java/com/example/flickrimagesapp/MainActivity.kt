package com.example.flickrimagesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flickrimagesapp.ui.theme.FlickrImagesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrImagesAppTheme {
                FlickrImageSearchApp()
            }
        }
    }
}