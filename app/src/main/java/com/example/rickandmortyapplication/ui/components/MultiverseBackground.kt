package com.example.rickandmortyapplication.ui.components

import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PlasmaPink
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.ScreenBackgroundGradient
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MultiverseBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "portalPulse")
    val pulse by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2_200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val d = LocalDensity.current
        val wf = with(d) { maxWidth.toPx() }
        val hf = with(d) { maxHeight.toPx() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackgroundGradient)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PortalGreen.copy(alpha = 0.12f * pulse),
                            Color.Transparent
                        ),
                        center = Offset(wf * 0.2f, hf * 0.12f),
                        radius = maxOf(wf, hf) * 0.55f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            PlasmaPink.copy(alpha = 0.08f * pulse),
                            Color.Transparent
                        ),
                        center = Offset(wf * 0.88f, hf * 0.22f),
                        radius = maxOf(wf, hf) * 0.45f
                    )
                )
        )
        val strokeAlpha = 0.15f + 0.1f * pulse
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sw = size.width
            val sh = size.height
            drawLine(
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        PortalGreen.copy(alpha = strokeAlpha),
                        NeonCyan.copy(alpha = strokeAlpha * 0.6f),
                        Color.Transparent
                    ),
                    start = Offset(0f, sh * 0.3f),
                    end = Offset(sw, sh * 0.35f)
                ),
                start = Offset(0f, sh * 0.32f),
                end = Offset(sw, sh * 0.32f),
                strokeWidth = 1.2f
            )
        }
        content()
    }
}
