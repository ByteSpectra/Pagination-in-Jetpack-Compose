package com.bytespectra.paginationinjetpackcompose.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.bytespectra.paginationinjetpackcompose.presentation.ui.theme.PaginationInJetpackComposeTheme
import com.bytespectra.paginationinjetpackcompose.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel = hiltViewModel<MainViewModel>()

            var query by rememberSaveable { mutableStateOf("") }

            PaginationInJetpackComposeTheme {
                Scaffold(
                    topBar = {
                        TextField(
                            value = query, onValueChange = {
                                query = it
                                viewModel.updateQuery(it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier
                        .safeContentPadding()
                        .fillMaxSize()
                ) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {

    val images = viewModel.images.collectAsLazyPagingItems()

    if (images.loadState.refresh is LoadState.NotLoading) {
        if (images.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text("Nothing Found")
            }
        }
    }

    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier) {

        // Prepend Section
        if (images.loadState.prepend is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
        if (images.loadState.prepend is LoadState.Error) {
            item {
                Box(
                    Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { images.retry() }
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }

        if (images.loadState.refresh is LoadState.NotLoading) {
            if (images.itemCount != 0) {
                items(
                    count = images.itemCount,
                    key = images.itemKey { it.uuid },
                    contentType = images.itemContentType { "content_type" }
                ) { index ->
                    AsyncImage(
                        model = images[index]?.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().padding(2.dp).height(200.dp)
                    )
                }
            }
        }

        // Append Section
        if (images.loadState.append is LoadState.Loading) {
            item {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        if (images.loadState.append is LoadState.Error) {
            item {
                Box(
                    Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { images.retry() }
                    ) {
                        Text(text = "Retry")
                    }
                }
            }
        }

    }

}

