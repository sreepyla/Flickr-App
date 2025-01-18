package com.example.flickrimagesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.flickrimagesapp.data.FlickrRepository
import com.example.flickrimagesapp.model.FlickrImage
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FlickrImageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private lateinit var viewModel: FlickrImageViewModel
    private lateinit var repository: FlickrRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = mock(FlickrRepository::class.java)
        viewModel = FlickrImageViewModel(repository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test searchImages updates images list`() = runTest(testScheduler) {
        // Arrange
        val dummyImages = listOf(
            FlickrImage("https://example.com/image1.jpg", "Image 1", "Author 1", "2025-01-01"),
            FlickrImage("https://example.com/image2.jpg", "Image 2", "Author 2", "2025-01-02")
        )

        whenever(repository.searchImages("test")).thenReturn(dummyImages)

        // Act
        viewModel.searchImages("test")
        testScheduler.advanceUntilIdle()

        // Assert
        assertEquals(2, viewModel.images.value.size)
        assertEquals(dummyImages, viewModel.images.value)
    }

    @Test
    fun `test isLoading state during search`() = runTest(testScheduler) {
        // Arrange: Simulate delay in repository response
        whenever(repository.searchImages("test")).thenAnswer {
            runBlocking {
                delay(100)
                listOf<FlickrImage>()
            }
        }

        // Act
        viewModel.searchImages("test")

        // Assert - isLoading should be true before search completes
        assertTrue(viewModel.isLoading.value)

        testScheduler.advanceUntilIdle()

        // Assert - isLoading should be false after search completes
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `test empty query returns no images`() = runTest(testScheduler) {
        // Act
        viewModel.searchImages("")
        testScheduler.advanceUntilIdle()

        // Assert
        assertTrue(viewModel.images.value.isEmpty())
    }

    @Test
    fun `test selectImage updates selectedImage`() {
        val image = FlickrImage("https://example.com/image.jpg", "Image", "Author", "2025-01-01")

        viewModel.selectImage(image)

        assertEquals(image, viewModel.selectedImage.value)
    }

    @Test
    fun `test clearSelectedImage resets selectedImage`() {
        val image = FlickrImage("https://example.com/image.jpg", "Image", "Author", "2025-01-01")
        viewModel.selectImage(image)

        viewModel.clearSelectedImage()

        assertNull(viewModel.selectedImage.value)
    }
}
