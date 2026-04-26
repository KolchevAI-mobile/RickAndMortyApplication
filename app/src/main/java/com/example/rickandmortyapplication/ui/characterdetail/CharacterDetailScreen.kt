package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.ErrorWithAnimationState
import com.example.rickandmortyapplication.ui.components.LoadingAnimation
import com.example.rickandmortyapplication.ui.state.UiState

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

    when (val st = state) {
        UiState.Loading -> LoadingAnimation()
        is UiState.Error -> {
            ErrorWithAnimationState(
                message = getCharacterErrorString(st.error),
                onRetry = { viewModel.loadCharacter() }
            )
        }
        is UiState.Success -> {
            CharacterDetailContent(
                character = st.data,
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
                        Text(stringResource(R.string.back))
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

            Text(text = stringResource(R.string.character_status, character.status))
            Text(text = stringResource(R.string.character_species, character.species))
        }
    }
}
