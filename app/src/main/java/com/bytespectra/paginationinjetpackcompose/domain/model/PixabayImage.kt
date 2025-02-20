package com.bytespectra.paginationinjetpackcompose.domain.model

import java.util.UUID

data class PixabayImage(
    val id: String,
    val imageUrl: String,
    val uuid: String = UUID.randomUUID().toString()
)
