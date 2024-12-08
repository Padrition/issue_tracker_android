package cz.mendelu.projek.ui.screens.login_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.theme.MPLUSRounded1C

@Composable
fun LoginScreen(
    navigation: INavigationRouter
){
    Log.d("LoginScreen", "Entered Login Screen")

    BaseScreen{
        LoginScreenContent(
            paddingValues = it
        )
    }
}

@Composable
fun LoginScreenContent(
    paddingValues: PaddingValues,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
    ) {
        Text(text = "Login Screen", fontFamily = MPLUSRounded1C, fontWeight = FontWeight.Black)
    }
}