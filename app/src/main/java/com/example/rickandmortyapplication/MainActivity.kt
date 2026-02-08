package com.example.rickandmortyapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.rickandmortyapplication.ui.characterdetail.CharacterDetailRoute
import com.example.rickandmortyapplication.ui.characterlist.CharacterListRoute
import com.example.rickandmortyapplication.ui.naviagtion.Screen
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.CharacterList
                    ) {
                        composable<Screen.CharacterList> {
                            CharacterListRoute(
                                onCharacterClick = {id ->
                                    navController.navigate(Screen.CharacterDetail(id))
                                }
                            )
                        }

                        composable<Screen.CharacterDetail> {
                            CharacterDetailRoute(
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun StubDetailScreen(id: Int) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Это экран деталей персонажа #$id",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}