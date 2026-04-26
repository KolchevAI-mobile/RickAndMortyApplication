package com.example.rickandmortyapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapplication.ui.characterdetail.CharacterDetailRoute
import com.example.rickandmortyapplication.ui.characterlist.CharacterListRoute
import com.example.rickandmortyapplication.ui.navigation.Screen
import com.example.rickandmortyapplication.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.CharacterList
                    ) {
                        composable<Screen.CharacterList>(
                            enterTransition = { fadeIn(tween(300)) + scaleIn(tween(280), initialScale = 0.98f) },
                            exitTransition = { fadeOut(tween(220)) + scaleOut(tween(220), targetScale = 0.95f) },
                            popEnterTransition = { fadeIn(tween(280)) + scaleIn(tween(280), initialScale = 0.96f) },
                            popExitTransition = { slideOutHorizontally(tween(280)) { it / 2 } + fadeOut(tween(200)) }
                        ) {
                            CharacterListRoute(
                                onCharacterClick = { id ->
                                    navController.navigate(Screen.CharacterDetail(id))
                                }
                            )
                        }

                        composable<Screen.CharacterDetail>(
                            enterTransition = { slideInHorizontally(tween(340)) { it / 4 } + fadeIn(tween(300)) },
                            popExitTransition = { slideOutHorizontally(tween(300)) { it } + fadeOut(tween(200)) }
                        ) {
                            CharacterDetailRoute(
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}