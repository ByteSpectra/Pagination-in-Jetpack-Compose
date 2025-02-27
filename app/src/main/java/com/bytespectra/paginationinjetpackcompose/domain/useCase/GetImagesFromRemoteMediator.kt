package com.bytespectra.paginationinjetpackcompose.domain.useCase

import com.bytespectra.paginationinjetpackcompose.domain.repository.RepositoryImage
import javax.inject.Inject

class GetImagesFromRemoteMediator @Inject constructor(
    private val repositoryImage: RepositoryImage
) {

    operator fun invoke(q: String) = repositoryImage.getRemoteMediatorImages(q)

}