package com.amirgoyri.musicapp.data.remote

import com.amirgoyri.musicapp.data.model.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicApiService {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}
