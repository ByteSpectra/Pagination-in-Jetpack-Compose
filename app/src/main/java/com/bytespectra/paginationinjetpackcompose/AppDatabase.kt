package com.bytespectra.paginationinjetpackcompose

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bytespectra.paginationinjetpackcompose.data.local.ImageDao
import com.bytespectra.paginationinjetpackcompose.data.local.RemoteKeyDao
import com.bytespectra.paginationinjetpackcompose.data.model.local.ImageEntity
import com.bytespectra.paginationinjetpackcompose.data.model.local.RemoteKey

@Database(entities = [ImageEntity::class, RemoteKey::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db"
        ).build()
    }

    abstract fun getImageDao(): ImageDao
    abstract fun getRemoteKeyDao(): RemoteKeyDao

}