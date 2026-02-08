package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.CharacterCard
import com.example.rickandmortyapplication.ui.components.ErrorState
import com.example.rickandmortyapplication.ui.components.LoadingState

@Composable
fun CharacterListRoute(
    onCharacterClick: (Int) -> Unit
) {

    val viewModel: CharacterListViewModel = hiltViewModel()

    CharacterListScreen(
        viewModel = viewModel,
        onCharacterClick = onCharacterClick
    )
}

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val characters = viewModel.characterPagingFlow.collectAsLazyPagingItems()
    val loadState = characters.loadState

    val searchQuery by viewModel.searchQuery.collectAsState()
    val filters by viewModel.filters.collectAsState()

    var isFilterSheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                viewModel.onSearchQueryChange(newValue)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Поиск по имени") },
            singleLine = true
        )

        Button(
            onClick = { isFilterSheetOpen = true },
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text("Фильтры")
        }

        if (isFilterSheetOpen) {
            CharacterFilterBottomSheet(
                currentFilters = filters,
                onApply = { newFilters ->
                    viewModel.onFilterChange(newFilters)
                    isFilterSheetOpen = false
                },
                onDismiss = {
                    isFilterSheetOpen = false
                }
            )
        }


        when {
            loadState.refresh is LoadState.Loading -> {
                LoadingState()
            }

            loadState.refresh is LoadState.Error -> {
                val error = loadState.refresh as LoadState.Error
                ErrorState(
                    message = error.error.localizedMessage ?: "Не удалось загрузить персонажей",
                    onRetry = { characters.retry() }
                )
            }

            else -> {
                CharacterListContent(
                    characters = characters,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}

@Composable
fun CharacterListContent(
    characters: androidx.paging.compose.LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = characters.itemCount
        ) { index ->
            val character = characters[index]
            if (character != null) {
                CharacterCard(
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    imageUrl = character.image,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onCharacterClick(character.id) }
                )
            }
        }
    }
}