package com.example.flickrimagesapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.flickrimagesapp.model.FlickrImage
import org.junit.Rule
import org.junit.Test

class FlickrImageSearchAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val testViewModel = FlickrImageViewModel()


    @Test
    fun `test search bar is displayed`() {
        composeTestRule.setContent {
            FlickrImageSearchApp(viewModel = testViewModel)
        }

        composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed()
    }

    @Test
    fun `test image grid is displayed`() {
        val dummyImages =
            listOf(
                FlickrImage("https://example.com/image1.jpg", "Image 1", "Author 1", "2025-01-01"),
                FlickrImage("https://example.com/image2.jpg", "Image 2", "Author 2", "2025-01-02")
            )

        testViewModel.images.value = dummyImages
        composeTestRule.setContent {
            FlickrImageSearchApp(viewModel = testViewModel)
        }

        composeTestRule.onNodeWithTag("ImageGrid").assertIsDisplayed()
    }


    @Test
    fun `test loading indicator is displayed`() {
        testViewModel.isLoading.value = true

        composeTestRule.setContent {
            FlickrImageSearchApp(viewModel = testViewModel)
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }
}
