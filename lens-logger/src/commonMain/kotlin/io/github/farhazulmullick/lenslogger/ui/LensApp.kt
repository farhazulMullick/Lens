package io.github.farhazulmullick.lenslogger.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.farhazulmullick.lenslogger.navigation.LensRoute

private const val TAG = "LensApp"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LensApp(
    modifier: Modifier = Modifier,
    showLensFAB: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    var showContent by remember { mutableStateOf(false) }
    MaterialTheme(
        colorScheme = if(isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Box(
            modifier = Modifier
                .zIndex(Float.MAX_VALUE)
                .fillMaxSize()
                .safeGesturesPadding()
                .safeContentPadding()
                .then(modifier)
        ) {
            // Lens FAB to show bottom sheet.
            if (showLensFAB) {
                LensFAB(modifier = Modifier) {
                    showContent = !showContent
                }
            }
        }

        if (showContent){
            LensBottomSheet(onDismiss = { showContent = !showContent }) {
                LensContent()
            }
        }
    }
    content()
}

@Composable
internal fun LensContent(){
    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) {
        NavHost(
            startDestination = LensRoute.NetLogScreen,
            navController = navController
        ) {
            composable<LensRoute.NetLogScreen> {
                NetLoggingScreen() {
                    // navigate to details screen.
                    navController.navigate(LensRoute.NetLogInfoScreen(index = it))
                }
            }
            composable<LensRoute.NetLogInfoScreen> {entry ->
                val data: LensRoute.NetLogInfoScreen = entry.toRoute<LensRoute.NetLogInfoScreen>()
                NetLoggingInfoScreen(index = data.index) {
                    navController.navigateUp()
                }
            }
        }
    }
}