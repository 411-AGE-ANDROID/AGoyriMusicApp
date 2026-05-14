package com.amirgoyri.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.amirgoyri.musicapp.navigation.AppNavigation
import com.amirgoyri.musicapp.ui.theme.AGoyriMusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AGoyriMusicAppTheme {
                AppNavigation()
            }
        }
    }
}
