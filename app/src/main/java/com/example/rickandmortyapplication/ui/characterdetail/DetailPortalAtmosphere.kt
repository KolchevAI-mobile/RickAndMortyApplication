package com.example.rickandmortyapplication.ui.characterdetail

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.rickandmortyapplication.ui.theme.NeonCyan
import com.example.rickandmortyapplication.ui.theme.PlasmaPink
import com.example.rickandmortyapplication.ui.theme.PortalGreen
import com.example.rickandmortyapplication.ui.theme.RmGreen
import kotlin.math.cos
import kotlin.math.sin

private data class AuraOrb(
    val baseX: Float,
    val baseY: Float,
    val rMul: Float,
    val c1: Color,
    val c2: Color
)

/**
 * Спокойный космический фон: мягкие сияния, крупные пятна перелива, лёгкий «пыль».
 * Без вращающихся дуг — приятно смотреть, не отвлекает от персонажа.
 */
@Composable
fun DetailPortalAtmosphere(modifier: Modifier = Modifier) {
    val t = rememberInfiniteTransition(label = "atmosphere")
    val breath by t.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(7_200, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )
    val driftA by t.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(16_000, easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftA"
    )
    val driftB by t.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(14_000, easing = CubicBezierEasing(0.17f, 0f, 0.2f, 1f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftB"
    )
    val shimmer by t.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3_200, easing = CubicBezierEasing(0.45f, 0f, 0.55f, 1f)),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // База — слабая заливка, не конкурирует с мультяшным портретом
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0x0C0A1218),
                    Color(0x1A0E1522),
                    Color(0x120A1218)
                ),
                start = Offset(0f, 0f),
                end = Offset(0f, h)
            )
        )
        // Крупные мягкие пятна (аура) — плавно двигаются
        val a = driftA * 2f * Math.PI.toFloat()
        val b = driftB * 2f * Math.PI.toFloat()
        listOf(
            AuraOrb(0.32f, 0.2f, 0.85f, PortalGreen, NeonCyan),
            AuraOrb(0.72f, 0.18f, 0.7f, RmGreen, PlasmaPink),
            AuraOrb(0.5f, 0.7f, 0.95f, NeonCyan, PortalGreen),
            AuraOrb(0.12f, 0.6f, 0.6f, PlasmaPink, Color.Transparent)
        ).forEachIndexed { i, orb ->
            val ph = a + i * 0.7f
            val cx = w * (orb.baseX + 0.04f * sin((ph * 0.5).toDouble()).toFloat())
            val cy = h * (orb.baseY + 0.03f * cos((b + i).toDouble()).toFloat())
            val r = minOf(w, h) * orb.rMul * (0.92f + 0.08f * breath) * 0.55f
            val alpha1 = 0.07f * shimmer * (1f - i * 0.08f)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        orb.c1.copy(alpha = alpha1),
                        orb.c2.copy(alpha = alpha1 * 0.35f),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = r
                )
            )
        }
        // Световая дуга у верхнего края (медленно «скользит»)
        val bandY = h * (0.08f + 0.04f * sin((a * 0.3).toDouble()).toFloat())
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Transparent,
                    PortalGreen.copy(alpha = 0.04f * breath),
                    NeonCyan.copy(alpha = 0.05f * shimmer),
                    Color.Transparent
                ),
                start = Offset(0f, bandY),
                end = Offset(w, bandY + h * 0.22f)
            ),
            topLeft = Offset(0f, 0f),
            size = size
        )
        // «Пыль/звёзды» — крошечные точки, тихо мерцают
        for (i in 0..35) {
            val sx = w * (0.08f + (i * 37 + 11) % 100 / 100f * 0.86f)
            val sy = h * (0.12f + (i * 23 + 7) % 100 / 100f * 0.8f)
            val tw = (0.5f * sin((a * 0.2f + i * 0.4f).toDouble()).toFloat() + 0.5f) * 0.6f + 0.4f * shimmer
            val alphaD = 0.04f + 0.08f * tw
            if (i % 3 == 0) {
                drawCircle(
                    color = RmGreen.copy(alpha = alphaD),
                    radius = 0.7f,
                    center = Offset(sx, sy)
                )
            } else {
                drawCircle(
                    color = Color.White.copy(alpha = alphaD * 0.7f),
                    radius = 0.45f,
                    center = Offset(sx, sy)
                )
            }
        }
    }
}
