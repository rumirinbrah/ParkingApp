package com.jetnsync.parkingapp.core.nav

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jetnsync.parkingapp.feature_auth.presentation.AuthPage
import com.jetnsync.parkingapp.feature_book.presentation.home.HomeRoot

@Composable
fun Navigation(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    Scaffold(
        modifier
            .fillMaxSize()
    ) {paddingValues ->
        Box(
            Modifier.fillMaxSize()
                .padding(paddingValues)
        ){
            NavHost(
                navController = navController,
                startDestination = Screen.Auth,
                modifier = modifier
            ){
                composable<Screen.Auth> {
                    AuthPage(
                        navToHome = {
                            navController.navigate(Screen.Home){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable<Screen.Home> {
                    HomeRoot(
                        logout = {
                            navController.navigate(Screen.Auth)
                        }
                    )
                }
            }
        }
    }

}