package com.noxchat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.noxchat.app.navigation.NoxNavGraph
import com.noxchat.app.navigation.Routes
import com.noxchat.app.ui.theme.NoxChatTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NoxChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NoxNavGraph(
                        startDestination = Routes.ONBOARDING,
                        onOnboardingComplete = { },
                        onAuthComplete = { }
                    )
                }
            }
        }
    }
}