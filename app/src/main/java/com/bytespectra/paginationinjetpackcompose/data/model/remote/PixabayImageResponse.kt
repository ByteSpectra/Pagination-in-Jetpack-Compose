package com.bytespectra.paginationinjetpackcompose.data.model.remote

data class PixabayImageResponse(
    val hits: List<ImageDTO>,
    val total: Int,
    val totalHits: Int
)