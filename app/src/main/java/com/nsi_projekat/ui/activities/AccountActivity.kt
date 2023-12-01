package com.nsi_projekat.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.nsi_projekat.ui.navigation.AccountActivityNavigation
import com.nsi_projekat.ui.theme.NSI_ProjekatTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class AccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleNavigationBugOnXiaomiDevices()

        setContent {
            NSI_ProjekatTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.material.MaterialTheme.colors.background)
                ) {

                    AccountActivityNavigation()
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