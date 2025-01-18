package com.example.flickrimagesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlickrImageSearchApp(viewModel: FlickrImageViewModel = viewModel()) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text("Flickr Image Search") },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White
            )
        )

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("SearchBar"),
            placeholder = { Text("Search images...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.searchImages(searchText.text) }
            )
        )

        if (viewModel.isLoading.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("LoadingIndicator"),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("ImageGrid")
            ) {
                items(viewModel.images.value.size) { index ->
                    val image = viewModel.images.value[index]
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { viewModel.selectImage(image) }
                            .testTag("ImageItem_$index")
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(image.imageUrl),
                            contentDescription = "Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }

    if (viewModel.selectedImage.value != null) {
        ImageDetailView(image = viewModel.selectedImage.value!!, onBack = { viewModel.clearSelectedImage() })
    }
}

