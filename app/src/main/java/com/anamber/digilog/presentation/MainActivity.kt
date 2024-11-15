package com.anamber.digilog.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.rememberScalingLazyListState
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.digilog.presentation.theme.DigilogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    DigilogTheme {
        val scalingLazyListState: ScalingLazyListState = rememberScalingLazyListState()
        Scaffold(
            timeText = {
                if (!scalingLazyListState.isScrollInProgress) {
                    TimeText()
                }
            },
        ) {
            val navController = rememberSwipeDismissableNavController()
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "colorAndWallpaperPicker"
            ) {
                composable("colorAndWallpaperPicker") {
                    ColorAndWallpaperPicker()
                }
                // Add other composable routes here
            }
        }
    }
}
@Composable
fun ColorAndWallpaperPicker() {
    val colorList = ColorOptions.colorOptionsDataSet
    val wallpaperList = WallpaperOptions.wallpaperOptionsDataSet
    val adapter = ColorAndWallpaperAdapter(colorList, wallpaperList)

    Column(modifier = Modifier.fillMaxSize()) {
        // Add a title or other UI elements here if needed

        // Color and Wallpaper Picker
        RecyclerView(
            adapter = adapter,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ... (Your ColorAndWallpaperAdapter code)

// ... (Preview function if needed)