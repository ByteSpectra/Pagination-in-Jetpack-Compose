package com.bytespectra.paginationinjetpackcompose.domain.useCase

import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val repositoryImage: RepositoryImage
) {
    operator fun invoke (q: String) = repositoryImage.getImagesFromPixabay(q)
}