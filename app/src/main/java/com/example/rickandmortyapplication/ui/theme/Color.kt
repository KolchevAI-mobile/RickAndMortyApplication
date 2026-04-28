package com.example.rickandmortyapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Portal & brand
val PortalGreen = Color(0xFF79A8FF)
val RmGreen = Color(0xFF9DC183)
val NeonCyan = Color(0xFF7CB7FF)
val PlasmaPink = Color(0xFFBE8BFF)
val RmYellow = Color(0xFFE6D496)

// Surfaces
val VoidBackground = Color(0xFF0E1219)
val VoidBackgroundMid = Color(0xFF161C27)
val SurfaceCard = Color(0xFF1D2431)
val SurfaceCardElevated = Color(0xFF252F3F)
val SurfaceOutline = Color(0xFF4A586E)

// Text
val TextPrimary = Color(0xFFEFF3FA)
val TextSecondary = Color(0xFFAAB5C6)
val TextMuted = Color(0xFF707E92)

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
            Color(0xFF101625),
            VoidBackground,
            Color(0xFF1A2232),
            Color(0xFF121A28)
        )
    )
