package com.bytespectra.paginationinjetpackcompose.data.pagingSource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bytespectra.paginationinjetpackcompose.data.local.ImageDao
import com.bytespectra.paginationinjetpackcompose.data.local.RemoteKeyDao
import com.bytespectra.paginationinjetpackcompose.data.mappers.ImageDtoToImageEntityMapper
import com.bytespectra.paginationinjetpackcompose.data.mappers.mapAll
import com.bytespectra.paginationinjetpackcompose.data.model.local.ImageEntity
import com.bytespectra.paginationinjetpackcompose.data.model.local.RemoteKey
import com.bytespectra.paginationinjetpackcompose.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class ImageRemoteMediator(
    private val query: String,
    private val imageDao: ImageDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val apiService: ApiService
): RemoteMediator<Int, ImageEntity>() {

    override suspend fun initialize(): InitializeAction {
        val hasLocalData = imageDao.getCountCorrespondingToQuery(query) > 0
        return when (hasLocalData) {
            true -> InitializeAction.SKIP_INITIAL_REFRESH
            false -> InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        val mapper = ImageDtoToImageEntityMapper(query)
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = state.anchorPosition?.let { anchorPosition ->
                    state.closestItemToPosition(anchorPosition)?.let {
                        remoteKeyDao.getRemoteKey(it.id)
                    }
                }
                remoteKey?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKey = state.pages.firstOrNull {
                    it.data.isNotEmpty()
                }?.data?.firstOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.prevKey ?: return MediatorResult.Success(remoteKey != null)
            }
            LoadType.APPEND -> {
                val remoteKey = state.pages.lastOrNull {
                    it.data.isNotEmpty()
                }?.data?.lastOrNull()?.let {
                    remoteKeyDao.getRemoteKey(it.id)
                }
                remoteKey?.nextKey ?: return MediatorResult.Success(remoteKey != null)
            }
        }

        if (imageDao.getCountCorrespondingToQuery(query) > page.times(state.config.pageSize)) {
            return MediatorResult.Success(false)
        }

        return try {
            val response = apiService.getImages(query = query, page = page)
            val remoteImages = response.hits.distinctBy { it.id }
            val endOfPaginationReached = remoteImages.size < state.config.pageSize
            val prevPage = if (page > 1) page - 1 else null
            val nextPage = if (endOfPaginationReached) null else page + 1

            val remoteKeys = remoteImages.map {
                RemoteKey(
                    id = it.id.toString(),
                    prevKey = prevPage,
                    nextKey = nextPage,
                    query = query
                )
            }
            remoteKeyDao.upsertAll(remoteKeys)
            imageDao.upsertAll(mapper.mapAll(remoteImages))
            MediatorResult.Success(endOfPaginationReached)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}