package com.doool.gooey.samples

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.doool.gooey.GooeyBox
import com.doool.gooey.GooeyCanvas
import kotlin.random.Random

/*
 * design source from : https://codepen.io/mnmxmx/full/VjjvEq
 */
class Blob(originX: Float, originY: Float, val key: Int) {
    companion object {
        val colors = listOf(0xffff1783, 0xff17c9ff, 0xff36ff40)
    }

    var x by mutableStateOf(originX)
    var y by mutableStateOf(originY)
    var angle = Math.PI * 2 * Math.random()
    var vx = ((3f + Math.random() * 1f) * Math.cos(angle)).toFloat()
    var vy = ((3f + Math.random() * 1f) * Math.sin(angle)).toFloat()
    var radius by mutableStateOf(10 + 10 * Math.random())
    var color = colors.random()

    fun update() {
        x += vx
        y += vy
        radius -= .05f
    }

    fun isEnd(width: Float, height: Float): Boolean {
        if (x > width || x < 0) return true
        if (y > height || y < 0) return true
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
        var key = remember { 0 }

        Text(text = "Drag It", color = Color.White)

        LaunchedEffect(key1 = Unit, block = {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (Random.nextLong(3) == (it % 3L)) {
                        blobs.add(Blob(current.x, current.y, key++))
                    }
                    blobs.forEach { it.update() }
                    blobs.removeAll { it.isEnd(width, height) }
                }
            }
        })

        GooeyBox(
            modifier
                .fillMaxSize()
                .pointerInput("Input") {
                    detectDragGestures(
                        onDragStart = {
                            current = it
                        }
                    ) { change, dragAmount ->
                        current += dragAmount
                    }
                }) {
            GooeyCanvas(modifier = Modifier, onDraw = {
                drawIntoCanvas { canvas ->
                    blobs.forEach {
                        canvas.drawGooey(
                            Size(
                                with(density) { it.radius.dp.toPx() },
                                with(density) { it.radius.dp.toPx() }),
                            Color(it.color),
                            CircleShape,
                            Offset(it.x, it.y)
                        )
                    }
                }
            })

            Box(
                Modifier
                    .size(80.dp)
                    .offset(
                        with(density) { current.x.toDp() - 40.dp },
                        with(density) { current.y.toDp() - 40.dp })
                    .gooey(
                        Color(0xffffdd02),
                        CircleShape
                    )
            )
        }
    }
}

@Composable
fun RandomGooeyBubble(modifier: Modifier = Modifier) {
    var current by remember { mutableStateOf(Offset(250f, 250f)) }
    val blobs = remember { mutableStateListOf<Blob>() }
    var key = remember { 0 }

    BoxWithConstraints() {
        val density = LocalDensity.current

        val width = with(density) { maxWidth.toPx() }
        val height = with(density) { maxHeight.toPx() }

        LaunchedEffect(key1 = Unit, block = {
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (Random.nextLong(5) == (it % 5L)) {
                        blobs.add(Blob(current.x, current.y, key++))
                    }
                    blobs.forEach { it.update() }
                    blobs.removeAll { it.isEnd(width, height) }
                }
            }
        })

        GooeyBox(modifier.pointerInput("Input") {
            detectDragGestures(
                onDragStart = {
                    current = it
                }
            ) { change, dragAmount ->
                current += dragAmount
            }
        }) {
            blobs.forEach {
                key(it.key) {
                    Box(
                        Modifier
                            .graphicsLayer(translationX = it.x, translationY = it.y)
                            .size(it.radius.dp)
                            .gooey(Color(it.color), CircleShape)
                    )
                }
            }

            Box(
                Modifier
                    .size(80.dp)
                    .offset(
                        with(density) { current.x.toDp() - 40.dp },
                        with(density) { current.y.toDp() - 40.dp })
                    .gooey(
                        Color.Green,
                        CircleShape
                    )
            )
        }
    }
}