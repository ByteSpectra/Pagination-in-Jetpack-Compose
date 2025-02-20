package com.bytespectra.paginationinjetpackcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDTO_to_ImageMapper
import com.bytespectra.paginationinjetpackcompose.data.pagingSource.ImagePagingSource
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage
import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import javax.inject.Inject

class RepositoryImageImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: ImageDTO_to_ImageMapper
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
}