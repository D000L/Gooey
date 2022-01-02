package com.doool.gooey

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.random.Random

val FastOutFastInEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1.0f)

@Composable
fun GooeyFab(modifier: Modifier = Modifier) {
    GooeyBox(modifier) {
        var isShow by remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier.gooey(Color.Blue, CircleGooeyShape),
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
        }

        FloatingActionButton(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
                .gooey(Color.Blue, CircleGooeyShape),
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
        }

        FloatingActionButton(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
                .gooey(Color.Blue, CircleGooeyShape),
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
        }
    }
}

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