package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.ui.components.ErrorState
import com.example.rickandmortyapplication.ui.components.LoadingState
import com.example.rickandmortyapplication.ui.state.UiState
import com.example.rickandmortyapplication.domain.model.Character

@Composable
fun CharacterDetailRoute(
    onBackClick: () -> Unit
) {
    val viewModel: CharacterDetailViewModel = hiltViewModel()

    CharacterDetailScreen(
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@Composable
fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (val s = state) {
        is UiState.Loading -> LoadingState()
        is UiState.Error -> ErrorState(
            message = s.message,
            onRetry = { viewModel.loadCharacter() }
        )
        is UiState.Success -> CharacterDetailContent(
            character = s.data,
            onBackClick = onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailContent(
    character: Character,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character.name) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Text(text = "Статус: ${character.status}")
            Text(text = "Вид: ${character.species}")
        }
    }
}
