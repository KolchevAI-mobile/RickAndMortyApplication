package com.example.rickandmortyapplication.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmortyapplication.ui.theme.CardImageShape
import com.example.rickandmortyapplication.ui.theme.PortalEdgeGradient
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.SurfaceCard
import com.example.rickandmortyapplication.ui.theme.TextPrimary
import com.example.rickandmortyapplication.ui.theme.TextSecondary
import com.example.rickandmortyapplication.ui.theme.statusColorForApiLabel

@Composable
fun CharacterCard(
    name: String,
    status: String,
    species: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "pressScale"
    )
    val statusColor = statusColorForApiLabel(status)
    val cardShape = RoundedCornerShape(16.dp)

    Card(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(
                        PortalGreen.copy(alpha = 0.55f),
                        statusColor.copy(alpha = 0.35f)
                    )
                ),
                shape = cardShape
            )
            .shadow(
                elevation = 10.dp,
                shape = cardShape,
                ambientColor = PortalGreen.copy(alpha = 0.2f),
                spotColor = PortalGreen.copy(alpha = 0.35f)
            ),
        onClick = onClick,
        interactionSource = interaction,
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = SurfaceCard
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            statusColor.copy(alpha = 0.12f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CardImageShape)
                    .border(
                        width = 1.5.dp,
                        brush = PortalEdgeGradient,
                        shape = CardImageShape
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = species,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                StatusChip(status = status, color = statusColor)
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    val label = when (status.trim().lowercase()) {
        "alive" -> "Alive"
        "dead" -> "Dead"
        else -> status
    }
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = color,
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.18f),
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
