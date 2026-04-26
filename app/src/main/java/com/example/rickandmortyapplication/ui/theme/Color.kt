package com.example.rickandmortyapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Portal & brand
val PortalGreen = Color(0xFF00FFAA)
val RmGreen = Color(0xFF97CE4C)
val NeonCyan = Color(0xFF4ECDC4)
val PlasmaPink = Color(0xFFFF6B9D)
val RmYellow = Color(0xFFFFE66D)

// Surfaces
val VoidBackground = Color(0xFF0A0C10)
val VoidBackgroundMid = Color(0xFF12151C)
val SurfaceCard = Color(0xFF1A1F2E)
val SurfaceCardElevated = Color(0xFF232A3A)
val SurfaceOutline = Color(0xFF3D4A5C)

// Text
val TextPrimary = Color(0xFFF0F4FC)
val TextSecondary = Color(0xFF9AA5B8)
val TextMuted = Color(0xFF5C6778)

// Status (episode tones)
val StatusAlive = Color(0xFF4ADE80)
val StatusDead = Color(0xFFF87171)
val StatusUnknown = Color(0xFF94A3B8)

val TitleGradient: Brush
    get() = Brush.linearGradient(
        listOf(PortalGreen, NeonCyan, RmGreen, RmYellow)
    )

val PortalEdgeGradient: Brush
    get() = Brush.linearGradient(
        listOf(
            PortalGreen.copy(alpha = 0.45f),
            NeonCyan.copy(alpha = 0.25f),
            PlasmaPink.copy(alpha = 0.2f)
        )
    )

val ScreenBackgroundGradient: Brush
    get() = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0D1118),
            VoidBackground,
            Color(0xFF15192A),
            Color(0xFF0A1620)
        )
    )
