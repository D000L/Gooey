package com.doool.gooey

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.doool.gooey.gooey.GooeyBox
import com.doool.gooey.gooey.GooeyDirection
import kotlin.random.Random

@Composable
fun Rain() {
    GooeyBox(
        Modifier
            .fillMaxSize()
            .background(Color.Blue), frame = 60
    ) {
        val progress by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            Modifier
                .padding(top = 40.dp)
                .size(width = 160.dp, height = 20.dp)
                .gooeyFixedSize(0.8f, Color.White, GooeyDirection.VerticalRectangle)
        )

        Box(
            Modifier
                .padding(top = 50.dp, start = 40.dp)
                .size(10.dp)
                .graphicsLayer(translationY = progress * 200f)
                .gooeyFixedSize(0.8f, Color.White, GooeyDirection.Circle)
        )

        Box(
            Modifier
                .padding(top = 50.dp, start = 80.dp)
                .size(10.dp)
                .graphicsLayer(translationY = progress * 200f)
                .gooeyFixedSize(0.8f, Color.White, GooeyDirection.Circle)
        )

        Box(
            Modifier
                .padding(top = 50.dp, start = 120.dp)
                .size(10.dp)
                .graphicsLayer(translationY = progress * 200f)
                .gooeyFixedSize(0.8f, Color.White, GooeyDirection.Circle)
        )
    }
}

@Composable
fun CircleRotation() {
    GooeyBox(
        Modifier
            .fillMaxSize()
            .background(Color.Black), frame = 60
    ) {
        var offset1 by remember { mutableStateOf(Offset(0f, 0f)) }
        var offset2 by remember { mutableStateOf(Offset(0f, 0f)) }
        var offset3 by remember { mutableStateOf(Offset(0f, 0f)) }
        var offset4 by remember { mutableStateOf(Offset(0f, 0f)) }


        LaunchedEffect(Unit) {
            var start = 0L
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (start + 1000L < it) {
                        start = it
                        offset1 = Offset(
                            Random.nextFloat() + 0.5f,
                            Random.nextFloat() + 0.5f
                        )
                        offset2 = Offset(
                            Random.nextFloat() + 0.5f,
                            Random.nextFloat() + 0.51f
                        )
                        offset3 =  Offset(
                            Random.nextFloat() + 0.5f,
                            Random.nextFloat() + 0.5f
                        )
                        offset4 =  Offset(
                            Random.nextFloat() + 0.5f,
                            Random.nextFloat() + 0.5f
                        )
                    }
                }
            }
        }

        val progress by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(2000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val circle1 by animateOffsetAsState(targetValue = offset1, tween(2000))
        Box(
            Modifier
                .size(40.dp)
                .graphicsLayer(
                    rotationZ = progress,
                    translationX = 200f,
                    translationY = 200f,
                    transformOrigin = TransformOrigin(circle1.x, circle1.x)
                )
                .gooeyFixedSize(0.8f, Color.White, GooeyDirection.Circle)
        )

        val circle2 by animateOffsetAsState(targetValue = offset2, tween(2000))
        Box(
            Modifier
                .size(20.dp)
                .graphicsLayer(
                    rotationZ = progress + 60,
                    translationX = 200f,
                    translationY = 200f,
                    transformOrigin = TransformOrigin(circle2.x, circle2.x)
                )
                .gooeyFixedSize(0.8f, Color.Green, GooeyDirection.Circle)
        )

        val circle3 by animateOffsetAsState(targetValue = offset3, tween(2000))
        Box(
            Modifier
                .size(50.dp)
                .graphicsLayer(
                    rotationZ = progress + 150,
                    translationX = 200f,
                    translationY = 200f,
                    transformOrigin = TransformOrigin(circle3.x, circle3.x)
                )
                .gooeyFixedSize(0.8f, Color.Blue, GooeyDirection.Circle)
        )

        val circle4 by animateOffsetAsState(targetValue = offset4, tween(2000))
        Box(
            Modifier
                .size(30.dp)
                .graphicsLayer(
                    rotationZ = progress + 270,
                    translationX = 200f,
                    translationY = 200f,
                    transformOrigin = TransformOrigin(circle4.x, circle4.x)
                )
                .gooeyFixedSize(0.8f, Color.Red, GooeyDirection.Circle)
        )
    }
}