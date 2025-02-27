package com.bytespectra.paginationinjetpackcompose.data.di

import android.content.Context
import com.bytespectra.paginationinjetpackcompose.AppDatabase
import com.bytespectra.paginationinjetpackcompose.data.local.ImageDao
import com.bytespectra.paginationinjetpackcompose.data.local.RemoteKeyDao
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDTO_to_ImageMapper
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageEntityToImageMapper
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService
import com.bytespectra.paginationinjetpackcompose.data.repository.RepositoryImageImpl
import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideImageRepository(
        apiService: ApiService,
        mapper: ImageDTO_to_ImageMapper,
        imageDao: ImageDao,
        remoteKeyDao: RemoteKeyDao,
        imageEntityToImageMapper: ImageEntityToImageMapper
    ): RepositoryImage {
        return RepositoryImageImpl(
            apiService = apiService,
            mapper = mapper,
            imageDao = imageDao,
            remoteKeyDao = remoteKeyDao,
            imageEntityToImageMapper = imageEntityToImageMapper
        )
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.getImageDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(appDatabase: AppDatabase): RemoteKeyDao {
        return appDatabase.getRemoteKeyDao()
    }

}