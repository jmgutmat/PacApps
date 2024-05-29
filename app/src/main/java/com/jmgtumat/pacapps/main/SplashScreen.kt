package com.jmgtumat.pacapps.main

//import com.jmgtumat.pacapps.ui.theme.crema
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.jmgtumat.pacapps.R
import com.jmgtumat.pacapps.navigation.AppScreens
import com.jmgtumat.pacapps.ui.theme.PacAppsTheme
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(key1 = true){
        delay(5000)
        navController.popBackStack()
        navController.navigate(AppScreens.MainScreen.route){
            popUpTo(AppScreens.SplashScreen.route) { inclusive = true }
        }
    }
    Splash()
}

@Composable
fun Splash() {
    Image(painter = painterResource(id = R.drawable.inicio),
        contentDescription = "Imagen inicio",
        modifier = Modifier
            .fillMaxSize(),
            //.background(crema),
        contentScale = ContentScale.Crop)
}

@Preview(showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:Nexus One"
)
@Composable
fun SplashScreenPreview() {
    PacAppsTheme() {
        Splash()
    }

}
