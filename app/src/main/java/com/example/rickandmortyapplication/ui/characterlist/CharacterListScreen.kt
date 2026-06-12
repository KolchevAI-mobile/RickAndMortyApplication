package com.example.rickandmortyapplication.ui.characterlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.rickandmortyapplication.ui.components.PagingAppendErrorFooter
import com.example.rickandmortyapplication.ui.components.PagingAppendLoadingFooter
import com.example.rickandmortyapplication.ui.theme.PortalGreen

private val SearchRowHeight = 52.dp

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
    val refreshState = characters.loadState.refresh
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filters by viewModel.filters.collectAsState()
    var isFilterSheetOpen by remember { mutableStateOf(false) }

    val filtersActive = filters.status != null || filters.gender != null
    val isRefreshing = refreshState is LoadState.Loading && characters.itemCount > 0
    val showInitialLoading = refreshState is LoadState.Loading && characters.itemCount == 0
    val showInitialError = refreshState is LoadState.Error && characters.itemCount == 0
    val showEmpty = refreshState is LoadState.NotLoading && characters.itemCount == 0

    MultiverseBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ListHeaderCard(
                itemCount = characters.itemCount,
                searchQuery = searchQuery,
                filtersActive = filtersActive,
                isRefreshing = isRefreshing,
                onSearchChange = viewModel::onSearchQueryChange,
                onFilterClick = { isFilterSheetOpen = true }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    showInitialLoading -> LoadingAnimation(Modifier.fillMaxSize())
                    showInitialError -> {
                        val error = refreshState as LoadState.Error
                        ErrorWithAnimationState(
                            message = pagingErrorMessage(error.error),
                            onRetry = { characters.retry() }
                        )
                    }
                    showEmpty -> EmptyMultiverseState(Modifier.fillMaxSize())
                    else -> CharacterListContent(
                        characters = characters,
                        onCharacterClick = onCharacterClick
                    )
                }
            }
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
    }
}

@Composable
private fun ListHeaderCard(
    itemCount: Int,
    searchQuery: String,
    filtersActive: Boolean,
    isRefreshing: Boolean,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                BrandedHeader(itemCount = itemCount)
                Spacer(modifier = Modifier.height(12.dp))
                SearchAndFiltersRow(
                    searchQuery = searchQuery,
                    filtersActive = filtersActive,
                    onSearchChange = onSearchChange,
                    onFilterClick = onFilterClick
                )
            }
            AnimatedVisibility(visible = isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
                    color = PortalGreen,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                )
            }
        }
    }
}

@Composable
private fun BrandedHeader(itemCount: Int) {
    val title = stringResource(R.string.list_header_title)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.list_header_subtitle).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
                        append(title)
                    }
                }
            )
        }
        Spacer(Modifier.width(12.dp))
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = stringResource(R.string.loaded_count, itemCount),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SearchAndFiltersRow(
    searchQuery: String,
    filtersActive: Boolean,
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
                .height(SearchRowHeight),
            placeholder = {
                Text(
                    stringResource(R.string.search_placeholder),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchChange("") },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = stringResource(R.string.cancel),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f)
            )
        )
        Button(
            onClick = onFilterClick,
            modifier = Modifier.height(SearchRowHeight),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            border = if (filtersActive) {
                BorderStroke(1.5.dp, PortalGreen.copy(alpha = 0.7f))
            } else {
                null
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (filtersActive) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Tune,
                contentDescription = stringResource(R.string.filters),
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(stringResource(R.string.filters), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun EmptyMultiverseState(modifier: Modifier = Modifier) {
    val haloColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "—",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .drawBehind {
                    drawCircle(
                        color = haloColor,
                        radius = size.minDimension * 0.42f
                    )
                }
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.empty_portal),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CharacterListContent(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit
) {
    val appendState = characters.loadState.append

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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

        when (appendState) {
            is LoadState.Loading -> {
                item(key = "append_loading") {
                    PagingAppendLoadingFooter()
                }
            }
            is LoadState.Error -> {
                item(key = "append_error") {
                    PagingAppendErrorFooter(
                        message = pagingErrorMessage(appendState.error),
                        onRetry = { characters.retry() }
                    )
                }
            }
            else -> Unit
        }
    }
}
