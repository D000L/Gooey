package com.doool.gooey.samples

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.doool.gooey.GooeyCanvas
import com.doool.gooey.gooeyBackground
import com.doool.gooey.gooeyEffect
import kotlin.random.Random

/*
 * design source from : https://codepen.io/mnmxmx/full/VjjvEq
 */
class Blob(originX: Float, originY: Float) {
    companion object {
        val colors = listOf(0xffff1783, 0xff17c9ff, 0xff36ff40)
    }

    var offset by mutableStateOf(Offset(originX,originY))
    var angle = Math.PI * 2 * Math.random()
    var vx = ((3f + Math.random() * 1f) * Math.cos(angle)).toFloat()
    var vy = ((3f + Math.random() * 1f) * Math.sin(angle)).toFloat()
    var radius by mutableStateOf(10 + 10 * Math.random())
    var color = colors.random()

    fun update() {
        offset = Offset(offset.x + vx, offset.y + vy)
        radius -= .05f
    }

    fun isEnd(width: Float, height: Float): Boolean {
        if (offset.x > width || offset.x < 0) return true
        if (offset.y > height || offset.y < 0) return true
        if (radius < 0) return true
        return false
    }
}

@Composable
fun RandomGooeyBubbleCanvas(modifier: Modifier = Modifier) {

    BoxWithConstraints(Modifier.background(Color(0xff102132))) {
        val density = LocalDensity.current

        val width = with(density) { maxWidth.toPx() }
        val height = with(density) { maxHeight.toPx() }

        var current by remember { mutableStateOf(Offset(width / 2f, height / 2f)) }
        val blobs = remember { mutableStateListOf<Blob>() }

        Text(text = "Drag It", color = Color.White)

        LaunchedEffect(key1 = Unit, block = {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (Random.nextLong(3) == (it % 3L)) {
                        blobs.add(Blob(current.x, current.y))
                    }
                    blobs.forEach { it.update() }
                    blobs.removeAll { it.isEnd(width, height) }
                }
            }
        })

        Box(
            modifier
                .fillMaxSize()
                .pointerInput("Input") {
                    detectDragGestures(onDragStart = {
                        current = it
                    }) { change, dragAmount ->
                        current += dragAmount
                    }
                }
                .gooeyEffect()) {
            GooeyCanvas(modifier = Modifier, onDraw = {
                blobs.forEach {
                    drawGooey(
                        Size(with(density) { it.radius.dp.toPx() },
                            with(density) { it.radius.dp.toPx() }),
                        Color(it.color),
                        CircleShape,
                        it.offset
                    )
                }
            })

            Box(
                Modifier
                    .size(80.dp)
                    .offset(with(density) { current.x.toDp() - 40.dp },
                        with(density) { current.y.toDp() - 40.dp })
                    .gooeyBackground(
                        Color(0xffffdd02), CircleShape
                    )
            )
        }
    }
}

@Composable
fun RandomGooeyBubble(modifier: Modifier = Modifier) {
    var current by remember { mutableStateOf(Offset(250f, 250f)) }
    val blobs = remember { mutableStateListOf<Blob>() }

    BoxWithConstraints() {
        val density = LocalDensity.current

        val width = with(density) { maxWidth.toPx() }
        val height = with(density) { maxHeight.toPx() }

        LaunchedEffect(key1 = Unit, block = {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (Random.nextLong(5) == (it % 5L)) {
                        blobs.add(Blob(current.x, current.y))
                    }
                    blobs.forEach { it.update() }
                    blobs.removeAll { it.isEnd(width, height) }
                }
            }
        })

        Box(
            modifier
                .fillMaxSize()
                .pointerInput("Input") {
                    detectDragGestures(onDragStart = {
                        current = it
                    }) { change, dragAmount ->
                        current += dragAmount
                    }
                }
                .gooeyEffect()) {
            blobs.forEach {
                Box(
                    Modifier
                        .graphicsLayer(translationX = it.offset.x, translationY = it.offset.y)
                        .size(it.radius.dp)
                        .gooeyBackground(Color(it.color), CircleShape)
                )
            }

            Box(
                Modifier
                    .size(80.dp)
                    .offset(with(density) { current.x.toDp() - 40.dp },
                        with(density) { current.y.toDp() - 40.dp })
                    .gooeyBackground(
                        Color.Green, CircleShape
                    )
            )
        }
    }
}