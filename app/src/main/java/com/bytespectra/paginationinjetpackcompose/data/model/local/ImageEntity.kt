package com.bytespectra.paginationinjetpackcompose.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val imageUrl: String,
    val query: String
)