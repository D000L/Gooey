package com.doool.gooey

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import kotlin.random.Random

val FastOutFastInEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1.0f)

@Composable
fun Testt() {
    GooeyBox(Modifier) {
        val progress by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(4000, easing = FastOutFastInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        (0..100).forEach {
            val startX = remember { Random.nextInt(100).toFloat() }
            val endX = remember { Random.nextInt(600).toFloat() + startX }

            val startY = remember { Random.nextInt(100).toFloat() }
            val endY = remember { Random.nextInt(600).toFloat() + startY }

            val color = remember {
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1f
                )
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .graphicsLayer(
                        translationX = startX + progress * endX,
                        translationY = startY + progress * endY,
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                    )
                    .gooey(
                        shape = CircleGooeyShape,
                        color = color
                    )
            )
        }
    }
}