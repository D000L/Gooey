package com.doool.gooey.samples

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import com.doool.gooey.GooeyCanvas
import com.doool.gooey.createGooeyRenderEffect
import com.doool.gooey.gooeyEffect

@Composable
fun GooeyLoading(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier) {
        val width = with(LocalDensity.current) { maxWidth.roundToPx() }
        val height = with(LocalDensity.current) { maxHeight.roundToPx() }

        val transition = rememberInfiniteTransition()
        val progress by transition.animateFloat(
            initialValue = -1f, targetValue = 6f, animationSpec = InfiniteRepeatableSpec(
                tween(easing = FastOutSlowInEasing, durationMillis = 4000),
                repeatMode = RepeatMode.Reverse
            )
        )

        GooeyCanvas(modifier = Modifier
            .fillMaxSize()
            .gooeyEffect(),
            onDraw = {
                for (i in 0..5) {
                    val circleProgress = (progress - i.toFloat()).coerceIn(-1f, 1f)
                    val x = width / 2f + circleProgress * 100f
                    drawGooey(Size(30f, 30f), Color.Blue, CircleShape, Offset(x, height / 2f))
                }
                drawGooey(
                    Size(progress * 5, progress * 5),
                    Color.Blue,
                    CircleShape,
                    Offset(width / 2f + 100f, height / 2f)
                )
                drawGooey(
                    Size(25 - progress * 5, 25 - progress * 5),
                    Color.Blue,
                    CircleShape,
                    Offset(width / 2f -100f, height / 2f)
                )
            })
    }
}