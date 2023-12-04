package com.nsi_projekat.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nsi_projekat.ui.navigation.MainActivityLayoutAndNavigation
import com.nsi_projekat.ui.theme.NSI_ProjekatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        handleNavigationBugOnXiaomiDevices()

        setContent {

            val viewModel = hiltViewModel<MainViewModel>()

            NSI_ProjekatTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {

                    MainActivityLayoutAndNavigation(viewModel)
                }
            }
        }
    }

    private fun handleNavigationBugOnXiaomiDevices() {
        window.decorView.post {
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }
}