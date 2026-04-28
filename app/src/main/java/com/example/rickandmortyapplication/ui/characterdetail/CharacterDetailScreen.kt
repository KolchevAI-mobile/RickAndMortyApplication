package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.ErrorWithAnimationState
import com.example.rickandmortyapplication.ui.components.LoadingAnimation
import com.example.rickandmortyapplication.ui.components.MultiverseBackground
import com.example.rickandmortyapplication.ui.state.UiState
import com.example.rickandmortyapplication.ui.theme.statusColorForApiLabel

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

    MultiverseBackground {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                (
                    fadeIn(spring(dampingRatio = 0.88f, stiffness = 200f)) togetherWith
                        fadeOut(spring(dampingRatio = 0.88f, stiffness = 200f))
                )
            },
            label = "detailState"
        ) { st ->
            when (st) {
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
    }
}

@Composable
private fun CharacterDetailContent(
    character: Character,
    onBackClick: () -> Unit
) {
    val scroll = rememberScrollState()
    val statusColor = statusColorForApiLabel(character.status)

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 12.dp)
                    .aspectRatio(1.05f),
                shape = RoundedCornerShape(28.dp),
                shadowElevation = 14.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xB20B0D14))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(8.dp))
                        StatusBadge(character.status, statusColor)
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.list_header_tagline),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    MetadataRow(character = character)
                    Spacer(Modifier.height(14.dp))
                    InfoPanel {
                        DetailInfoRow(
                            title = stringResource(R.string.character_origin, ""),
                            value = character.originName.ifEmpty { "Unknown origin" }
                        )
                        Spacer(Modifier.height(10.dp))
                        DetailInfoRow(
                            title = stringResource(R.string.character_location, ""),
                            value = character.locationName.ifEmpty { "Unknown location" }
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
        TopBackButton(onBack = onBackClick)
    }
}

@Composable
private fun StatusBadge(status: String, statusColor: Color) {
    Surface(
        shape = CircleShape,
        color = statusColor.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Text(
            text = status,
            color = statusColor,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MetadataRow(character: Character) {
    val typeValue = character.type.trim().ifEmpty { "Unknown type" }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetaPill(title = "Species", value = character.species)
        MetaPill(title = "Type", value = typeValue)
        MetaPill(title = "Gender", value = character.gender.ifEmpty { "Unknown" })
    }
}

@Composable
private fun MetaPill(title: String, value: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TopBackButton(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .shadow(8.dp, CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.82f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun InfoPanel(
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), shape)
                .padding(14.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun DetailInfoRow(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title.trimEnd(':').trim(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
