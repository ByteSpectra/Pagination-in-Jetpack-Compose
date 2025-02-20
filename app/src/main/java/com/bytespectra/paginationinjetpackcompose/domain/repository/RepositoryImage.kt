package com.bytespectra.paginationinjetpackcompose.domain.repository

import androidx.paging.Pager
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage

interface RepositoryImage {
    fun getImagesFromPixabay(query: String): Pager<Int, PixabayImage>
}