package com.amirgoyri.musicapp.data.model

data class Track(
    val title: String,
    val artist: String,
    val albumImage: String
)

fun generateTracks(album: Album): List<Track> =
    (1..10).map { i ->
        Track(
            title = "${album.title} • Track $i",
            artist = album.artist,
            albumImage = album.image
        )
    }
