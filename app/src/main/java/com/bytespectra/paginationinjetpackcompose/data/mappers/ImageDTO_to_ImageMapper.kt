package com.bytespectra.paginationinjetpackcompose.data.mappers

import com.bytespectra.paginationinjetpackcompose.data.model.remote.ImageDTO
import com.bytespectra.paginationinjetpackcompose.domain.model.PixabayImage
import javax.inject.Inject

class ImageDTO_to_ImageMapper @Inject constructor(): Mapper<ImageDTO, PixabayImage> {
    override fun map(from: ImageDTO): PixabayImage {
        return PixabayImage(
            id = from.id.toString(),
            imageUrl = from.largeImageURL
        )
    }
}