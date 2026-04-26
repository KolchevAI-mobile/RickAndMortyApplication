package com.example.rickandmortyapplication.ui.theme

import androidx.compose.ui.graphics.Color

fun statusColorForApiLabel(status: String): Color = when (status.trim().lowercase()) {
    "alive" -> StatusAlive
    "dead" -> StatusDead
    else -> StatusUnknown
}
