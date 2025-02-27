package com.bytespectra.paginationinjetpackcompose.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.bytespectra.paginationinjetpackcompose.data.model.local.RemoteKey

@Dao
interface RemoteKeyDao {

    @Upsert
    suspend fun upsertAll(list: List<RemoteKey>)

    @Query("SELECT * FROM remotekey WHERE `id`=:id")
    suspend fun getRemoteKey(id: String): RemoteKey?

    @Query("DELETE FROM remotekey WHERE `query`=:q")
    suspend fun deleteRemoteKeyQuery(q: String)

}