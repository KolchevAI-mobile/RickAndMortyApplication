package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CharacterListRoute(
    onCharacterClick: (Int) -> Unit
) {

    val viewModel: CharacterListViewModel = hiltViewModel()

    CharacterListScreen (
        viewModel = viewModel,
        onCharacterClick = onCharacterClick
    )
}

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {

}