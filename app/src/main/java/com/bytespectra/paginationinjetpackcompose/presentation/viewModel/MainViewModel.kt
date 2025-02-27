package com.bytespectra.paginationinjetpackcompose.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.bytespectra.paginationinjetpackcompose.domain.useCase.GetImagesFromRemoteMediator
import com.bytespectra.paginationinjetpackcompose.domain.useCase.GetImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: GetImagesUseCase,
    private val remoteMediator: GetImagesFromRemoteMediator
): ViewModel() {

    private val _query = MutableStateFlow("")

    fun updateQuery(q: String) {
        _query.update { q }
    }

    // Simple API Pagination

    /*@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val images = _query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            useCase.invoke(query).flow
        }.cachedIn(viewModelScope)*/

    // Remote Mediator Single Truth

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val images = _query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            remoteMediator.invoke(query)
        }.cachedIn(viewModelScope)

}