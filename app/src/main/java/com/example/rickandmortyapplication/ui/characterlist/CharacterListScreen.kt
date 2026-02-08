package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    viewModel.onSearchQueryChange(newValue)
                },
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text("Поиск по имени") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { isFilterSheetOpen = true }
            ) {
                Text("Фильтры")
            }
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
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onClick = { onCharacterClick(character.id) }
                )
            }
        }
    }

}