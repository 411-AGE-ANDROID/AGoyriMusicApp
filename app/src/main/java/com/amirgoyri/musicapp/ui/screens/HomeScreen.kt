package com.amirgoyri.musicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amirgoyri.musicapp.data.model.Album
import com.amirgoyri.musicapp.ui.components.AlbumCard
import com.amirgoyri.musicapp.ui.components.MiniPlayer
import com.amirgoyri.musicapp.viewmodel.AlbumsUiState
import com.amirgoyri.musicapp.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAlbumClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentAlbum by viewModel.currentAlbum.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5EEFF))) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item { HomeHeader() }

            when (val state = uiState) {
                is AlbumsUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF6B2FA0))
                        }
                    }
                }
                is AlbumsUiState.Error -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error: ${state.message}",
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.fetchAlbums() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B2FA0))
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                is AlbumsUiState.Success -> {
                    val albums = state.albums

                    item {
                        Text(
                            text = "Albums",
                            color = Color(0xFF1A1A2E),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 20.dp, top = 4.dp, bottom = 12.dp)
                        )
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(albums) { album ->
                                AlbumCard(
                                    album = album,
                                    onClick = {
                                        viewModel.setCurrentAlbum(album)
                                        onAlbumClick(album.id)
                                    }
                                )
                            }
                        }
                    }
                    item {
                        Text(
                            text = "Recently Played",
                            color = Color(0xFF1A1A2E),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp)
                        )
                    }
                    items(albums) { album ->
                        RecentlyPlayedItem(
                            album = album,
                            onClick = {
                                viewModel.setCurrentAlbum(album)
                                onAlbumClick(album.id)
                            }
                        )
                    }
                }
            }
        }

        MiniPlayer(
            album = currentAlbum,
            isPlaying = isPlaying,
            onTogglePlay = { viewModel.togglePlayPause() },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A0E8F), Color(0xFF8B3FD8), Color(0xFFF5EEFF))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp, top = 40.dp)
        ) {
            Text(
                text = "Good Morning!",
                color = Color(0xFFE0C8F8),
                fontSize = 16.sp
            )
            Text(
                text = "Amir Goyri",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "What do you want to listen to?",
                color = Color(0xFFD0B0F0),
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun RecentlyPlayedItem(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.image)
                    .crossfade(true)
                    .build(),
                contentDescription = album.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.title,
                    color = Color(0xFF1A1A2E),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = album.artist,
                    color = Color(0xFF757575),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
