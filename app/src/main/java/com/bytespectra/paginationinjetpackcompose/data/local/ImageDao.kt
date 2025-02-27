package com.bytespectra.paginationinjetpackcompose.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bytespectra.paginationinjetpackcompose.data.model.local.ImageEntity

@Dao
interface ImageDao {

    @Upsert
    suspend fun upsertAll(list: List<ImageEntity>)

    @Query("SELECT * FROM ImageEntity WHERE `query`=:q")
    fun getImages(q: String): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM ImageEntity WHERE `query`=:q")
    suspend fun nukeTable(q: String)

    @Query("SELECT COUNT(*) FROM ImageEntity WHERE `query`=:q")
    suspend fun getCountCorrespondingToQuery(q: String): Int

}