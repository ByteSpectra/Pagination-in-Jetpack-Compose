package com.bytespectra.paginationinjetpackcompose.data.mappers

import com.bytespectra.paginationinjetpackcompose.data.model.local.ImageEntity
import com.bytespectra.paginationinjetpackcompose.data.model.remote.ImageDTO

class ImageDtoToImageEntityMapper(private val query: String): Mapper<ImageDTO, ImageEntity> {

    override fun map(from: ImageDTO): ImageEntity {
        return ImageEntity(
            id = from.id.toString(),
            imageUrl = from.largeImageURL,
            query = query
        )
    }
}