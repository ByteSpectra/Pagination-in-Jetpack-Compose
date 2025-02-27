package com.bytespectra.paginationinjetpackcompose.data.mappers

import com.bytespectra.paginationinjetpackcompose.data.model.local.ImageEntity
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage
import java.util.UUID
import javax.inject.Inject

class ImageEntityToImageMapper @Inject constructor(): Mapper<ImageEntity, PixabayImage> {

    override fun map(from: ImageEntity): PixabayImage {
        return PixabayImage(
            id = from.id,
            imageUrl = from.imageUrl,
            uuid = UUID.randomUUID().toString()
        )
    }
}