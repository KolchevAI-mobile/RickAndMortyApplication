package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.CharacterCard
import com.example.rickandmortyapplication.ui.components.ErrorWithAnimationState
import com.example.rickandmortyapplication.ui.components.LoadingAnimation
import com.example.rickandmortyapplication.ui.components.MultiverseBackground
import com.example.rickandmortyapplication.ui.components.PortalRiftFooter
import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.RmGreen
import com.example.rickandmortyapplication.ui.theme.TextSecondary
import com.example.rickandmortyapplication.ui.theme.TitleGradient

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

    MultiverseBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                BrandedHeader()
                Spacer(modifier = Modifier.height(12.dp))
                SearchAndFiltersRow(
                    searchQuery = searchQuery,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onFilterClick = { isFilterSheetOpen = true }
                )
            }

            if (isFilterSheetOpen) {
                CharacterFilterBottomSheet(
                    currentFilters = filters,
                    onApply = { newFilters ->
                        viewModel.onFilterChange(newFilters)
                        isFilterSheetOpen = false
                    },
                    onDismiss = { isFilterSheetOpen = false }
                )
            }

            when {
                loadState.refresh is LoadState.Loading -> {
                    LoadingAnimation()
                }
                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    ErrorWithAnimationState(
                        message = error.error.localizedMessage
                            ?: stringResource(R.string.load_characters_error),
                        onRetry = { characters.retry() }
                    )
                }
                else -> {
                    if (loadState.refresh is LoadState.NotLoading &&
                        characters.itemCount == 0
                    ) {
                        EmptyMultiverseState()
                    } else {
                        CharacterListContent(
                            characters = characters,
                            onCharacterClick = onCharacterClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandedHeader() {
    val title = stringResource(R.string.app_name)
    val tagline = stringResource(R.string.list_header_tagline)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = tagline.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        brush = TitleGradient,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.4.sp
                    )
                ) {
                    append(title)
                }
            }
        )
    }
}

@Composable
private fun SearchAndFiltersRow(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp, max = 50.dp),
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = PortalGreen
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
            },
            shape = MaterialTheme.shapes.large,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = PortalGreen,
                unfocusedBorderColor = TextSecondary.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        Button(
            onClick = onFilterClick,
            modifier = Modifier.heightIn(max = 48.dp),
            shape = MaterialTheme.shapes.large,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = NeonCyan
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = stringResource(R.string.filters),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(stringResource(R.string.filters), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun EmptyMultiverseState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "∅",
            style = MaterialTheme.typography.displayLarge,
            color = RmGreen,
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.12f),
                        radius = size.minDimension * 0.42f
                    )
                }
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.empty_portal),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CharacterListContent(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PortalRiftFooter(
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                count = characters.itemCount,
                key = characters.itemKey { it.id }
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
}
