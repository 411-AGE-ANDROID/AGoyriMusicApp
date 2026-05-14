package com.amirgoyri.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirgoyri.musicapp.data.model.Album
import com.amirgoyri.musicapp.data.model.Track
import com.amirgoyri.musicapp.data.model.generateTracks
import com.amirgoyri.musicapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AlbumDetailUiState {
    object Loading : AlbumDetailUiState()
    data class Success(val album: Album, val tracks: List<Track>) : AlbumDetailUiState()
    data class Error(val message: String) : AlbumDetailUiState()
}

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun fetchAlbumDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = AlbumDetailUiState.Loading
            try {
                val album = RetrofitInstance.api.getAlbumById(id)
                val tracks = generateTracks(album)
                _uiState.value = AlbumDetailUiState.Success(album, tracks)
            } catch (e: Exception) {
                _uiState.value = AlbumDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }
}
