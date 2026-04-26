package com.example.rickandmortyapplication.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PlasmaPink
import com.example.rickandmortyapplication.ui.theme.PortalGreen

/**
 * Нижний декор в духе «зелёного портала»: вращающиеся дуги и слабое свечение.
 */
@Composable
fun PortalRiftFooter(
    modifier: Modifier = Modifier
) {
    val loop = rememberInfiniteTransition(label = "rift")
    val spin by loop.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(22_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )
    val pulse by loop.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2_400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.98f
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(
                    PortalGreen.copy(alpha = 0.12f * pulse),
                    NeonCyan.copy(alpha = 0.06f * pulse),
                    Color.Transparent
                ),
                center = Offset(cx, cy * 0.6f),
                radius = w * 0.75f
            ),
            topLeft = Offset(0f, h * 0.35f),
            size = Size(w, h * 0.7f)
        )
        for (i in 0..4) {
            val deg = spin + i * 32f
            val dim = 160f + i * 32f
            val top = (h - dim * 0.4f).coerceAtLeast(0f)
            val left = (w - dim) * 0.5f
            rotate(degrees = deg, pivot = Offset(cx, cy)) {
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color.Transparent,
                            PortalGreen.copy(alpha = 0.14f - i * 0.02f),
                            NeonCyan.copy(alpha = 0.1f - i * 0.02f),
                            PlasmaPink.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    ),
                    startAngle = 15f,
                    sweepAngle = 195f,
                    useCenter = false,
                    topLeft = Offset(left, top),
                    size = Size(dim, dim * 0.45f),
                    style = Stroke(width = (1.4f - i * 0.12f).coerceAtLeast(0.6f))
                )
            }
        }
    }
}
