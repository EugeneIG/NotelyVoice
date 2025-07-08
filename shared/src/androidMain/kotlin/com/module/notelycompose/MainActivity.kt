package com.module.notelycompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.module.notelycompose.di.init
import com.module.notelycompose.onboarding.data.PreferencesRepository
import com.module.notelycompose.platform.Theme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication


class MainActivity : AppCompatActivity() {
    private lateinit var  permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            KoinApplication(application = {
                androidContext(context.applicationContext)
                init()
            }) {
                val systemUiController = rememberSystemUiController()
                val preferenceRepository by inject<PreferencesRepository>()
                val uiMode by preferenceRepository.getTheme().collectAsState(Theme.SYSTEM.name)
                val darkTheme = when (uiMode) {
                    Theme.DARK.name -> true
                    Theme.LIGHT.name -> false
                    else -> isSystemInDarkTheme()
                }
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
                initializePermissionLauncher()
                App()
            }
        }
    }

    private fun initializePermissionLauncher() {
        val permissionLauncherHolder by inject<PermissionLauncherHolder>()
        permissionLauncherHolder.permissionLauncher = this.permissionLauncher
    }

}

