package com.bytespectra.paginationinjetpackcompose.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
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
    private val useCase: GetImagesUseCase
): ViewModel() {

    private val _query = MutableStateFlow("")

    fun updateQuery(q: String) {
        _query.update { q }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val images = _query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            useCase.invoke(query).flow
        }.cachedIn(viewModelScope)

}