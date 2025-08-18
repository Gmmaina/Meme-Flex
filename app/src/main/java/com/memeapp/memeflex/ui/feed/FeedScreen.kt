package com.memeapp.memeflex.ui.feed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memeapp.memeflex.R
import com.memeapp.memeflex.data.Meme
import com.memeapp.memeflex.data.sampleMemes
import com.memeapp.memeflex.ui.theme.MemeFlexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    memes: List<Meme> = sampleMemes,
    onRefresh: () -> Unit = {},
    onLikeClick: (Meme) -> Unit = {},
    onDownloadClick: (Meme) -> Unit = {},
    onShareClick: (Meme) -> Unit = {},
    onMemeClick: (Meme) -> Unit = {},
    onLongPress: (Meme) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.feed_title)) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(memes) { meme ->
                FeedMemeCard(
                    meme = meme,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onLikeClick = onLikeClick,
                    onDownloadClick = onDownloadClick,
                    onShareClick = onShareClick,
                    onMemeClick = onMemeClick,
                    onLongPress = onLongPress
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    MemeFlexTheme {
        FeedScreen()
    }
}