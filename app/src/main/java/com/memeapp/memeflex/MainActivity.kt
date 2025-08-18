package com.memeapp.memeflex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.memeapp.memeflex.navigation.MemeFlexNavigation
import com.memeapp.memeflex.ui.theme.MemeFlexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemeFlexTheme {
                MemeFlexNavigation()
            }
        }
    }
}