package com.bytespectra.paginationinjetpackcompose.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage
import kotlinx.coroutines.flow.Flow

interface RepositoryImage {
    fun getImagesFromPixabay(query: String): Pager<Int, PixabayImage>
    fun getRemoteMediatorImages(query: String): Flow<PagingData<PixabayImage>>
}