package com.amirgoyri.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amirgoyri.musicapp.data.model.Album
import com.amirgoyri.musicapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AlbumsUiState {
    object Loading : AlbumsUiState()
    data class Success(val albums: List<Album>) : AlbumsUiState()
    data class Error(val message: String) : AlbumsUiState()
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
    val uiState: StateFlow<AlbumsUiState> = _uiState

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentAlbum = MutableStateFlow<Album?>(null)
    val currentAlbum: StateFlow<Album?> = _currentAlbum

    init {
        fetchAlbums()
    }

    fun fetchAlbums() {
        viewModelScope.launch {
            _uiState.value = AlbumsUiState.Loading
            try {
                val albums = RetrofitInstance.api.getAlbums()
                _uiState.value = AlbumsUiState.Success(albums)
                if (_currentAlbum.value == null && albums.isNotEmpty()) {
                    _currentAlbum.value = albums.first()
                }
            } catch (e: Exception) {
                _uiState.value = AlbumsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun setCurrentAlbum(album: Album) {
        _currentAlbum.value = album
    }
}
