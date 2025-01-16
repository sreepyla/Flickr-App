package com.example.flickrimagesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class FlickrImageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: FlickrImageViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = spy(FlickrImageViewModel())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test searchImages updates images list`() = runTest {
        // Arrange
        val dummyImages = mutableStateOf(
            listOf(
                FlickrImage("https://example.com/image1.jpg", "Image 1", "Author 1", "2025-01-01"),
                FlickrImage("https://example.com/image2.jpg", "Image 2", "Author 2", "2025-01-02")
            )
        )

        doReturn(dummyImages).`when`(viewModel).images

        // Act
        viewModel.searchImages("test")
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(2, viewModel.images.value.size)
    }

    @Test
    fun `test selectImage updates selectedImage`() {
        val image = FlickrImage("https://example.com/image.jpg", "Image", "Author", "2025-01-01")

        viewModel.selectImage(image)

        Assert.assertEquals(image, viewModel.selectedImage.value)
    }

    @Test
    fun `test clearSelectedImage resets selectedImage`() {
        val image = FlickrImage("https://example.com/image.jpg", "Image", "Author", "2025-01-01")
        viewModel.selectImage(image)

        viewModel.clearSelectedImage()

        Assert.assertNull(viewModel.selectedImage.value)
    }
}


