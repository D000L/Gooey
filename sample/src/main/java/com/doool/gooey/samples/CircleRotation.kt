package com.doool.gooey.samples

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.doool.gooey.gooeyBackground
import com.doool.gooey.gooeyEffect
import kotlin.random.Random

fun RandomOffset() = Offset(
    Random.nextFloat(), Random.nextFloat()
)

@Composable
fun CircleRotation(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .gooeyEffect(), contentAlignment = Alignment.Center
    ) {
        val positions = remember { mutableStateListOf<Offset>() }
        val pivots = remember { mutableStateListOf<Offset>() }
        val rotates = remember { mutableStateListOf<Float>() }

        for (i in 0..3) {
            positions.add(RandomOffset() * 200f - Offset(100f, 100f))
            pivots.add(RandomOffset() * 1f - Offset(0.5f, 0.5f))
            rotates.add(90f * i)
        }

        LaunchedEffect(Unit) {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    for (i in 0..3) {
                        if (it % 10 == Random.nextLong(10)) {
                            positions[i] = RandomOffset() * 200f - Offset(100f, 100f)
                            pivots[i] = RandomOffset() * 0.4f - Offset(0.2f, 0.2f)
                        }
                        if (it % 20 == Random.nextLong(20)) {
                            rotates[i] = Random.nextFloat() * 720f - 360f
                        }
                    }
                }
            }
        }

        val progress by rememberInfiniteTransition().animateFloat(
            initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart
            )
        )

        val colors = remember { listOf(Color.Red, Color.Blue, Color.Green, Color.Magenta) }

        for (i in 0..3) {
            val position by animateOffsetAsState(targetValue = positions[i], tween(2000))
            val pivot by animateOffsetAsState(targetValue = pivots[i], tween(2000))
            val rotate by animateFloatAsState(targetValue = rotates[i], tween(2000))

            Box(
                modifier = Modifier
                    .size(30.dp + 20.dp.times(i))
                    .graphicsLayer(
                        rotationZ = progress + rotate,
                        translationX = position.x,
                        translationY = position.y,
                        transformOrigin = TransformOrigin(pivot.x, pivot.x)
                    )
                    .gooeyBackground(
                        shape = CircleShape, color = colors[i]
                    )
            )
        }
    }
}