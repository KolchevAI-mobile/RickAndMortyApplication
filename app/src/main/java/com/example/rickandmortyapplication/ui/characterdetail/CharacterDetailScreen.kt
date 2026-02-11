package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.ui.state.UiState
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.ErrorWithAnimationState
import com.example.rickandmortyapplication.ui.components.LoadingAnimation

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

    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(state) {
        if (state is UiState.Loading) {
            showLoading = true
        } else {
            kotlinx.coroutines.delay(1000)
            showLoading = false
        }
    }

    when {
        showLoading -> LoadingAnimation()
        state is UiState.Error -> {
            val s = state as UiState.Error
            ErrorWithAnimationState(
                message = s.message,
                onRetry = { viewModel.loadCharacter() }
            )
        }
        state is UiState.Success -> {
            val s = state as UiState.Success
            CharacterDetailContent(
                character = s.data,
                onBackClick = onBackClick
            )
        }
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
