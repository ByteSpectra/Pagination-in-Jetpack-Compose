package com.bytespectra.paginationinjetpackcompose.data.di

import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDTO_to_ImageMapper
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService
import com.bytespectra.paginationinjetpackcompose.data.repository.RepositoryImageImpl
import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://pixabay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    
    @Provides
    fun provideImageRepository(apiService: ApiService, mapper: ImageDTO_to_ImageMapper): RepositoryImage {
        return RepositoryImageImpl(
            apiService = apiService,
            mapper = mapper
        )
    }

}