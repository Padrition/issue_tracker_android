package cz.mendelu.projek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import cz.mendelu.projek.navigation.Destination
import cz.mendelu.projek.navigation.NavGraph
import cz.mendelu.projek.ui.theme.ProjekTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjekTheme {
                NavGraph(startDestination = Destination.LoginScreen.route)
            }
        }
    }
}