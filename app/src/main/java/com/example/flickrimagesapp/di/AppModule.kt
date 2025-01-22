package com.example.flickrimagesapp.di

import com.example.flickrimagesapp.data.FlickrRepository
import com.example.flickrimagesapp.data.FlickrRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindFlickrRepository(
        flickrRepositoryImpl: FlickrRepositoryImpl
    ): FlickrRepository
}
