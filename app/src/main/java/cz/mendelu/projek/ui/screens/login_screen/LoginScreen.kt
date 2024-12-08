package cz.mendelu.projek.ui.screens.login_screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            paddingValues = it,
            navigation
        )
    }
}

@Composable
fun LoginScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.weight(0.2f))
        Text(
            text = "Login",
            fontFamily = MPLUSRounded1C,
            fontWeight = FontWeight.Normal,
            fontSize = 64.sp,
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            value = "email",
            onValueChange = {},
            label = {
                Text("Email")
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp),
            value = "password",
            onValueChange = {},
            label = {
                Text("Password")
            },
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(36.dp),
            onClick = {}
        ){
            Text("Login")
        }

        Spacer(Modifier.weight(1.0f))

        Row {
            Text(
                text = "Or create an account by ",
                fontFamily = MPLUSRounded1C,
                fontWeight = FontWeight.Normal,
            )
            Text(
                modifier = Modifier
                    .clickable {
                        navigation.navigateToRegisterScreen()
                    },
                text = "singing up",
                fontFamily = MPLUSRounded1C,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
