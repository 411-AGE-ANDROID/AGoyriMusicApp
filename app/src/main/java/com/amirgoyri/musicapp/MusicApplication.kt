package com.amirgoyri.musicapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MusicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header(
                                "User-Agent",
                                "Mozilla/5.0 (Android; Mobile) AGoyriMusicApp/1.0"
                            )
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(true)
            .build()
        Coil.setImageLoader(imageLoader)
    }
}
