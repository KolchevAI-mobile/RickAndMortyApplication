package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.R
import com.example.rickandmortyapplication.domain.model.Character
import com.example.rickandmortyapplication.ui.components.ErrorWithAnimationState
import com.example.rickandmortyapplication.ui.components.LoadingAnimation
import com.example.rickandmortyapplication.ui.components.MultiverseBackground
import com.example.rickandmortyapplication.ui.state.UiState
import com.example.rickandmortyapplication.ui.theme.CharacterDetailPhotoShape
import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PortalEdgeGradient
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.SurfaceCard
import com.example.rickandmortyapplication.ui.theme.TextPrimary
import com.example.rickandmortyapplication.ui.theme.TitleGradient
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
    val statusC = statusColorForApiLabel(character.status)

    Box(Modifier.fillMaxSize()) {
        DetailPortalAtmosphere(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 20.dp,
                        shape = CharacterDetailPhotoShape,
                        spotColor = PortalGreen.copy(alpha = 0.4f),
                        ambientColor = NeonCyan.copy(alpha = 0.2f)
                    )
            ) {
                Box(Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CharacterDetailPhotoShape)
                    )
                    // Лёгкий виньет: темнее внизу, лицо сверху не глушим
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0x00000000),
                                        Color(0x220A0C12),
                                        Color(0x550A1018)
                                    )
                                )
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            border = BorderStroke(1.5.dp, PortalEdgeGradient),
                            shape = CharacterDetailPhotoShape
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 32.dp)
                    .navigationBarsPadding()
            ) {
                Text(
                    text = character.name,
                    style = characterNameTextStyle()
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = character.status,
                    color = statusC,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.height(20.dp))
                InfoPanel {
                    val typeLine = character.type.trim().ifEmpty { "—" }
                    DetailInfoRow(
                        stringResource(R.string.character_species, character.species)
                    )
                    if (typeLine != "—") {
                        Spacer(Modifier.height(8.dp))
                        DetailInfoRow(
                            stringResource(R.string.character_type, typeLine)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    DetailInfoRow(
                        stringResource(
                            R.string.character_gender,
                            character.gender.ifEmpty { "—" }
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    DetailInfoRow(
                        stringResource(
                            R.string.character_origin,
                            character.originName.ifEmpty { "—" }
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    DetailInfoRow(
                        stringResource(
                            R.string.character_location,
                            character.locationName.ifEmpty { "—" }
                        )
                    )
                }
            }
        }
        TopBackButton(onBack = onBackClick)
    }
}

@Composable
private fun TopBackButton(
    onBack: () -> Unit
) {
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
                .shadow(
                    8.dp,
                    CircleShape,
                    ambientColor = PortalGreen,
                    spotColor = PortalGreen
                )
                .background(Color(0xCC1A1F2E), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = NeonCyan
            )
        }
    }
}

@Composable
private fun InfoPanel(
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    Surface(
        shape = shape,
        color = SurfaceCard,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, PortalGreen.copy(alpha = 0.45f), shape)
                .padding(18.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun DetailInfoRow(
    line: String
) {
    Text(
        text = line,
        style = MaterialTheme.typography.bodyLarge,
        color = TextPrimary
    )
}

private fun characterNameTextStyle() = TextStyle(
    fontSize = 30.sp,
    lineHeight = 36.sp,
    fontWeight = FontWeight.Black,
    brush = TitleGradient,
    shadow = Shadow(
        color = Color(0xE6000000),
        offset = Offset(0f, 2.5f),
        blurRadius = 14f
    )
)
