package com.example.flickrimagesapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.flickrimagesapp.data.FlickrRepository
import com.example.flickrimagesapp.model.FlickrImage
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FlickrImageSearchAppTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var testViewModel: FlickrImageViewModel

    private val fakeRepository = object : FlickrRepository {
        override suspend fun searchImages(query: String): List<FlickrImage> {
            return when (query) {
                "valid" -> listOf(
                    FlickrImage("https://example.com/image1.jpg", "Image 1", "Author 1", "2025-01-01"),
                    FlickrImage("https://example.com/image2.jpg", "Image 2", "Author 2", "2025-01-02")
                )
                "empty" -> emptyList()
                else -> throw Exception("Network error")
            }
        }
    }

    @Before
    fun setup() {
        testViewModel = FlickrImageViewModel(fakeRepository)
    }

    @Test
    fun `test search bar is displayed`() {
        composeTestRule.setContent {
            FlickrImageSearchApp(viewModel = testViewModel)
        }

        composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed()
    }


    @Test
    fun `test error message is displayed on API failure`(): Unit = runBlocking {
        composeTestRule.setContent {
            FlickrImageSearchApp(viewModel = testViewModel)
        }

        testViewModel.searchImages("error")

        composeTestRule.onNodeWithText("Something went wrong. Please try again.").assertIsDisplayed()
    }

}
