package com.bytespectra.paginationinjetpackcompose.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bytespectra.paginationinjetpackcompose.data.local.ImageDao
import com.bytespectra.paginationinjetpackcompose.data.local.RemoteKeyDao
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDTO_to_ImageMapper
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageEntityToImageMapper
import com.bytespectra.paginationinjetpackcompose.data.pagingSource.ImagePagingSource
import com.bytespectra.paginationinjetpackcompose.data.pagingSource.ImageRemoteMediator
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage
import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImageImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: ImageDTO_to_ImageMapper,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val imageEntityToImageMapper: ImageEntityToImageMapper
) : RepositoryImage {

    override fun getImagesFromPixabay(query: String): Pager<Int, PixabayImage> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 15,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                ImagePagingSource(
                    apiService = apiService,
                    query = query,
                    mapper = mapper
                )
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRemoteMediatorImages(query: String): Flow<PagingData<PixabayImage>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 1,
                enablePlaceholders = true,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                imageDao.getImages(query)
            },
            remoteMediator = ImageRemoteMediator(
                query = query,
                imageDao = imageDao,
                remoteKeyDao = remoteKeyDao,
                apiService = apiService
            )
        ).flow
            .map {
                it.map {
                    imageEntityToImageMapper.map(it)
                }
            }
    }
}