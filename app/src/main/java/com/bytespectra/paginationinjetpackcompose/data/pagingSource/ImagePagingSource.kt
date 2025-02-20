package com.bytespectra.paginationinjetpackcompose.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDTO_to_ImageMapper
import com.bytespectra.paginationinjetpackcompose.data.mappers.mapAll
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage

class ImagePagingSource(
    private val apiService: ApiService,
    private val query: String,
    private val mapper: ImageDTO_to_ImageMapper,
): PagingSource<Int, PixabayImage>() {

    override fun getRefreshKey(state: PagingState<Int, PixabayImage>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayImage> {
        val page = params.key ?: 1
        val pageSize = params.loadSize
        return try {
            val images = apiService.getImages(query = query, page = page)
            LoadResult.Page(
                data = mapper.mapAll(images.hits),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (images.hits.size < pageSize) null else page.plus(1)
            )
        }
        catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}