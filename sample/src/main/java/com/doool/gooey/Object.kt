package com.doool.gooey

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random

val FastOutFastInEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1.0f)
val SlowOutFastInEasing = CubicBezierEasing(0.56f, 0.43f, 0.96f, -0.07f)

@Composable
fun GooeyEffectButton(modifier: Modifier = Modifier) {
    var isGooeyShow by remember { mutableStateOf(false) }

    val animatedDp by animateDpAsState(
        targetValue = if (isGooeyShow) -32.dp else 0.dp,
        tween(durationMillis = if (isGooeyShow) 600 else 0, easing = SlowOutFastInEasing)
    ) {
        isGooeyShow = false
    }

    val animatedSize by animateDpAsState(
        targetValue = if (isGooeyShow) 30.dp else 4.dp,
        tween(durationMillis = if (isGooeyShow) 100 else 0, easing = LinearEasing)
    )

    GooeyBox(
        modifier.padding(start = 100.dp, top = 100.dp),
        GooeyIntensity.Custom(20f, 40f, -5000f)
    ) {
        if (isGooeyShow) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .size(animatedSize)
                    .offset(
                        x = animatedDp,
                        y = animatedDp
                    )
                    .gooey(Color.Green, CircleShape)
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(animatedSize)
                    .offset(
                        x = -animatedDp,
                        y = -animatedDp
                    )
                    .gooey(Color.Green, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .height(height = 56.dp)
                .gooey(Color.Green, RectangleShape, true)
                .clickable {
                    isGooeyShow = !isGooeyShow
                }
                .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "Show GooeyEffect!!")
        }
    }
}

@Composable
fun Sample(modifier: Modifier = Modifier) {
    val progress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutFastInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    GooeyBox(modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .graphicsLayer(
                    translationX = 100f,
                    translationY = 100f,
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .gooey(
                    shape = CircleShape,
                    color = Color.Magenta
                )
        )

        Box(
            modifier = Modifier
                .size(30.dp)
                .graphicsLayer(
                    translationX = 275f,
                    translationY = 275f,
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .gooey(
                    shape = CircleShape,
                    color = Color.Magenta
                )
        )

        Box(
            modifier = Modifier
                .size(45.dp)
                .graphicsLayer(
                    translationX = 140f,
                    translationY = 180f + progress * 220f,
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .gooey(
                    shape = CircleShape,
                    color = Color.Magenta
                )
        )
    }
}

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
    var current by remember { mutableStateOf(Offset(250f, 250f)) }
    val blobs = remember { mutableStateListOf<Blob>() }
    var key = remember { 0 }

    BoxWithConstraints(Modifier.background(Color(0xff102132))) {
        val density = LocalDensity.current

        val width = with(density) { maxWidth.toPx() }
        val height = with(density) { maxHeight.toPx() }

        LaunchedEffect(key1 = Unit, block = {
            while (true) {
                withInfiniteAnimationFrameMillis {
//                    if (Random.nextLong(5) == (it % 5L)) {
                    blobs.add(Blob(current.x, current.y, key++))
//                    }
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
            val density = LocalDensity.current

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
                        RectangleShape
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

@Composable
fun GooeyFab(modifier: Modifier = Modifier) {
    GooeyBox(modifier) {
        var isShow by remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.Center)
                .offset(
                    x = animateDpAsState(
                        targetValue = if (isShow) -40.dp else 0.dp,
                        tween(durationMillis = 400, easing = SlowOutFastInEasing)
                    ).value,
                    y = animateDpAsState(
                        targetValue = if (isShow) -40.dp else 0.dp,
                        tween(durationMillis = 400, easing = SlowOutFastInEasing)
                    ).value
                )
                .gooey(Color.Green, CircleShape),
            backgroundColor = Color.Green,
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
        }

        FloatingActionButton(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.Center)
                .offset(
                    y = animateDpAsState(
                        targetValue = if (isShow) -60.dp else 0.dp,
                        tween(durationMillis = 400, delayMillis = 400, easing = SlowOutFastInEasing)
                    ).value
                )
                .gooey(Color.Green, CircleShape),
            backgroundColor = Color.Green,
            onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Add, null)
        }

        FloatingActionButton(
            modifier = Modifier.gooey(Color.Green, CircleShape),
            backgroundColor = Color.Green,
            onClick = { isShow = !isShow }) {
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

        Box(
            modifier = Modifier
                .size(2.dp)
                .graphicsLayer(
                    translationX = 200f,
                    translationY = 200f
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .background(
                    shape = CircleShape,
                    color = Color.Green
                )
        )

        Box(
            modifier = Modifier
                .size(5.dp)
                .graphicsLayer(
                    translationX = 200f,
                    translationY = 400f
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .background(
                    shape = CircleShape,
                    color = Color.Green
                )
        )

        Box(
            modifier = Modifier
                .size(2.dp)
                .graphicsLayer(
                    translationX = 600f,
                    translationY = 200f
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .gooey(
                    shape = CircleShape,
                    color = Color.Green,
                )
        )

        Box(
            modifier = Modifier
                .size(5.dp)
                .graphicsLayer(
                    translationX = 600f,
                    translationY = 400f
//                    scaleX = progress * 0.5f + 0.5f,
//                    scaleY = progress * 0.5f + 0.5f,
                )
                .gooey(
                    shape = CircleShape,
                    color = Color.Green,
                )
        )

//        (0..1).forEach {
//            val startX = remember { Random.nextInt(100).toFloat() }
//            val endX = remember { Random.nextInt(600).toFloat() + startX }
//
//            val startY = remember { Random.nextInt(100).toFloat() }
//            val endY = remember { Random.nextInt(600).toFloat() + startY }
//
//            val color = remember {
//                Color(
//                    Random.nextFloat(),
//                    Random.nextFloat(),
//                    Random.nextFloat(),
//                    1f
//                )
//            }
//            Box(
//                modifier = Modifier
//                    .size(0.dp)
//                    .graphicsLayer(
//                        translationX = startX + progress * endX,
//                        translationY = startY + progress * endY,
////                    scaleX = progress * 0.5f + 0.5f,
////                    scaleY = progress * 0.5f + 0.5f,
//                    )
//                    .gooey(
//                        shape = CircleShape,
//                        color = Color.Green
//                    )
//            )
//        }

//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .graphicsLayer(
//                    translationX = 100f,
////                    scaleX = progress * 0.5f + 0.5f,
////                    scaleY = progress * 0.5f + 0.5f,
//                )
//                .gooey(
//                    shape = CircleShape,
//                    color = Color.Blue
//                )
//        )
//
//        Box(
//            modifier = Modifier
//                .size(40.dp)
//                .graphicsLayer(
////                    scaleX = progress * 0.5f + 0.5f,
////                    scaleY = progress * 0.5f + 0.5f,
//                )
//                .background(Color.Red, CircleShape)
//        )
    }
}