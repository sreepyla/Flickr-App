package com.example.flickrimagesapp

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailView(image: FlickrImage, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(1f).background(Color.Gray)
    ) {
        CenterAlignedTopAppBar(
            title = { Text("Image Details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(image.imageUrl),
            contentDescription = image.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .testTag("DetailImage")
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Title: ${image.title}", modifier = Modifier.padding(8.dp).testTag("DetailTitle"))
        Text("Author: ${image.author}", modifier = Modifier.padding(8.dp).testTag("DetailAuthor"))
        Text("Published: ${image.publishedDate}", modifier = Modifier.padding(8.dp).testTag("DetailPublished"))

        Button(
            onClick = {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Check out this image: ${image.imageUrl}")
                    type = "text/plain"
                }
                MyApplication.instance.startActivity(Intent.createChooser(intent, "Share Image"))
            },
            modifier = Modifier.padding(8.dp).testTag("ShareButton")
        ) {
            Text("Share Image")
        }
    }
}


