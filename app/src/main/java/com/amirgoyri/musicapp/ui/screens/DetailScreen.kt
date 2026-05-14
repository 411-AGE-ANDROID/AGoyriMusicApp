package com.amirgoyri.musicapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.amirgoyri.musicapp.data.model.Album
import com.amirgoyri.musicapp.data.model.Track
import com.amirgoyri.musicapp.ui.components.MiniPlayer
import com.amirgoyri.musicapp.ui.components.TrackItem
import com.amirgoyri.musicapp.viewmodel.AlbumDetailUiState
import com.amirgoyri.musicapp.viewmodel.DetailViewModel

@Composable
fun DetailScreen(
    albumId: String,
    viewModel: DetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.fetchAlbumDetail(albumId)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5EEFF))) {
        when (val state = uiState) {
            is AlbumDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF6B2FA0))
                }
            }
            is AlbumDetailUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${state.message}", color = Color.Red)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.fetchAlbumDetail(albumId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B2FA0))
                    ) {
                        Text("Retry")
                    }
                }
            }
            is AlbumDetailUiState.Success -> {
                DetailContent(
                    album = state.album,
                    tracks = state.tracks,
                    isPlaying = isPlaying,
                    onTogglePlay = { viewModel.togglePlayPause() },
                    onBackClick = onBackClick,
                    modifier = Modifier.padding(bottom = 72.dp)
                )
                MiniPlayer(
                    album = state.album,
                    isPlaying = isPlaying,
                    onTogglePlay = { viewModel.togglePlayPause() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 8.dp)
                .background(Color(0x66000000), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun DetailContent(
    album: Album,
    tracks: List<Track>,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { AlbumHeroSection(album = album, isPlaying = isPlaying, onTogglePlay = onTogglePlay) }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About this album",
                        color = Color(0xFF6B2FA0),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = album.description,
                        color = Color(0xFF424242),
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    )
                }
            }
        }
        item {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFEDD6FF)
            ) {
                Text(
                    text = "Artist: ${album.artist}",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    color = Color(0xFF6B2FA0),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        item {
            Text(
                text = "Tracks",
                color = Color(0xFF1A1A2E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
            )
        }
        itemsIndexed(tracks) { index, track ->
            TrackItem(track = track, trackNumber = index + 1)
        }
    }
}

@Composable
private fun AlbumHeroSection(
    album: Album,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.image)
                .crossfade(true)
                .build(),
            contentDescription = album.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x336B2FA0),
                            Color(0x996B2FA0),
                            Color(0xFF4A0E8F)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp, end = 100.dp)
        ) {
            Text(
                text = album.title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 30.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = album.artist,
                color = Color(0xFFD0B0F0),
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = "Shuffle",
                    tint = Color(0xFF6B2FA0),
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(
                onClick = onTogglePlay,
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF6B2FA0), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
