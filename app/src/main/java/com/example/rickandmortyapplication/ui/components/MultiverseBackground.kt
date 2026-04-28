package com.example.rickandmortyapplication.ui.components

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
import com.example.rickandmortyapplication.ui.theme.ScreenBackgroundGradient

@Composable
fun MultiverseBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "ambientPulse")
    val pulse by transition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4_200, easing = LinearEasing),
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
                .blur(92.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF7AA2FF).copy(alpha = 0.12f * pulse),
                            Color.Transparent
                        ),
                        center = Offset(wf * 0.1f, hf * 0.1f),
                        radius = maxOf(wf, hf) * 0.6f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(120.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFB58CFF).copy(alpha = 0.11f * pulse),
                            Color.Transparent
                        ),
                        center = Offset(wf * 0.86f, hf * 0.2f),
                        radius = maxOf(wf, hf) * 0.5f
                    )
                )
        )
        val strokeAlpha = 0.08f + 0.06f * pulse
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sw = size.width
            val sh = size.height
            drawLine(
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        Color(0xFF9FB8FF).copy(alpha = strokeAlpha),
                        Color(0xFF94A8D6).copy(alpha = strokeAlpha * 0.7f),
                        Color.Transparent
                    ),
                    start = Offset(0f, sh * 0.24f),
                    end = Offset(sw, sh * 0.28f)
                ),
                start = Offset(0f, sh * 0.26f),
                end = Offset(sw, sh * 0.26f),
                strokeWidth = 1f
            )
        }
        content()
    }
}
