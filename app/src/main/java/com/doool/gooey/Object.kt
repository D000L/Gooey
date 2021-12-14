package com.doool.gooey

import android.content.Context
import android.graphics.BlurMaskFilter
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Immutable
abstract class Object {
    var translationX: Float by mutableStateOf(0f)
    var translationY: Float by mutableStateOf(80f)
    var center: Offset by mutableStateOf(Offset(0f, 0f))
    var scaleX: Float by mutableStateOf(1f)
    var scaleY: Float by mutableStateOf(1f)
    var alpha: Float by mutableStateOf(1f)

    abstract fun createPath(): Path
}

class IconCircle(override var radius: Float, @DrawableRes val iconResId: Int) : Circle(radius) {
    override fun createPath(): Path {
        return Path().apply {
            asAndroidPath().addCircle(
                translationX + center.x,
                translationY + center.y,
                radius,
                android.graphics.Path.Direction.CW
            )
        }
    }
}

open class Circle(open var radius: Float) : Object() {
    override fun createPath(): Path {
        return Path().apply {
            asAndroidPath().addCircle(
                translationX + center.x,
                translationY + center.y,
                radius,
                android.graphics.Path.Direction.CW
            )
        }
    }
}

@LayoutScopeMarker
@Immutable
interface GooeyScope {

    @Stable
    fun Modifier.gooey(tension: Float, color: Color, shape: Shape = RectangleShape): Modifier

    @Stable
    fun Modifier.gooeyFixedSize(tension: Float, color: Color): Modifier
}

@Stable
val TopHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline.Rectangle {
        return Outline.Rectangle(Rect(Offset(0f + 20f,0f ),Offset(size.width - 20f,size.height/2f)))
    }


    override fun toString(): String = "RectangleShape"
}

@Stable
val BottomHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline.Rectangle {
        return Outline.Rectangle(Rect(Offset(0f + 20f,size.height/2f),Offset(size.width - 20f,size.height)))
    }


    override fun toString(): String = "RectangleShape"
}

@Stable
val StartHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline.Rectangle {
        return Outline.Rectangle(Rect(Offset(0f ,0f + 6f),Offset(size.width/2,size.height - 6f)))
    }


    override fun toString(): String = "RectangleShape"
}

@Stable
val EndHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline.Rectangle {
        return Outline.Rectangle(Rect(Offset(size.width/2f,0f  + 6f),Offset(size.width,size.height -6f)))
    }


    override fun toString(): String = "RectangleShape"
}


class GooeyScopeImpl : GooeyScope {

    var visibleGooey: Boolean = false

    @Stable
    override fun Modifier.gooey(tension: Float, color: Color, shape: Shape): Modifier = composed{
        if (!visibleGooey) {
            return@composed  background(color)
        }

        var width by remember{ mutableStateOf(0)}
        var height by  remember{ mutableStateOf(0)}

        val modifier = this.onSizeChanged {
            width = it.width
            height = it.height
        }

        val colorList = mutableListOf<Pair<Float, Color>>()
        colorList.add(Pair(0f, color))
        colorList.add(Pair(1f - tension, color))
        colorList.add(Pair(1f, color.copy(alpha = 0f)))

         return@composed when (shape) {
            RectangleShape -> {
                modifier
                    .background(color)
//                    .shadow(30.dp,shape,true)
//                    .background(
//                        Brush.verticalGradient(
//                            *colorList.toTypedArray(),
//                            startY = height / 2f,
//                            endY = 0f
//                        ), TopHalfRectangleShape
//                    )
//                    .background(
//                        Brush.verticalGradient(
//                            *colorList.toTypedArray(),
//                            startY = height / 2f,
//                            endY = height.toFloat()
//                        ), BottomHalfRectangleShape
//                    )
//                    .background(
//                        Brush.horizontalGradient(
//                            *colorList.toTypedArray(),
//                            startX= width / 2f,
//                            endX= 0f
//                        ), StartHalfRectangleShape
//                    )
//                    .background(
//                        Brush.horizontalGradient(
//                            *colorList.toTypedArray(),
//                            startX = width / 2f,
//                            endX = width.toFloat()
//                        ), EndHalfRectangleShape
//                    )

            }
            CircleShape -> {
                modifier.background(Brush.radialGradient(*colorList.toTypedArray()), shape)
            }
            else -> modifier
        }
    }

    @Stable
    override fun Modifier.gooeyFixedSize(tension: Float, color: Color): Modifier {
        if (!visibleGooey) return background(color)

        val colorList = mutableListOf<Pair<Float, Color>>()
        colorList.add(Pair(0f, color))
        colorList.add(Pair(1f / (1f + tension), color))
        colorList.add(Pair(1f / (1f + tension), color.copy(0.8f)))
        colorList.add(Pair(1f, color.copy(alpha = 0f)))

        return scale(1f + tension)
            .background(Brush.radialGradient(*colorList.toTypedArray()))
            .scale(1f)
    }
}

class ScanView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbstractComposeView(context, attrs) {

    var composable: (@Composable () -> Unit)? = null
    var updatedView: (ImageBitmap) -> Unit = {}
    var frame by mutableStateOf(24)

    fun setScanTarget(composable: @Composable () -> Unit) {
        this.composable = composable
    }

    fun setListener(updatedView: (ImageBitmap) -> Unit) {
        this.updatedView = updatedView
    }

    init {
        visibility = View.GONE
    }

    @Composable
    override fun Content() {
        var height by remember { mutableStateOf(0) }
        var width by remember { mutableStateOf(0) }

        Box(Modifier.onSizeChanged {
            height = it.height
            width = it.width
        }) {
            composable?.invoke()
        }

        val imageBitmap = remember(width, height) {
            if (width > 0 && height > 0) {
                ImageBitmap(width, height)
            } else null
        }
        val canvas = remember(imageBitmap) { imageBitmap?.let { Canvas(it) } }

        LaunchedEffect(imageBitmap) {
            var start = 0L
            val delay = 1000L / frame
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (start + delay < it) {
                        start = it
                        if (width > 0 && height > 0) {
                            imageBitmap?.let {
                                it.reuse()
                                this@ScanView.draw(canvas?.nativeCanvas)
                                updatedView(it)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun GooeyBox(
    modifier: Modifier = Modifier,
    gooeyVisibility: Boolean,
    frame: Int = 24,
    content: @Composable GooeyScope.() -> Unit
) {
    var image by remember { mutableStateOf(ImageBitmap(1600, 500)) }

    val scope = remember(gooeyVisibility) {
        GooeyScopeImpl().apply {
            visibleGooey = gooeyVisibility
        }
    }
    val colorFilterPaint = remember {
        Paint().apply {
            val contrast = 48f
            val brightness = 10000f

            val cm = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, brightness,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, contrast, -brightness
                )
            )

            colorFilter = ColorFilter.colorMatrix(cm)
        }
    }

    if (gooeyVisibility) {
        AndroidView(modifier = modifier, factory = {
            ScanView(it).apply {
                setScanTarget { scope.content() }
                setListener {
                    image = it
                }
                this.frame = frame
            }
        })
    }

    val modifier = if (gooeyVisibility) modifier.drawWithContent {
        drawIntoCanvas {
            drawContent()
//            it.drawImage(image, Offset.Zero, colorFilterPaint)
        }
    } else modifier

    Box(modifier) {
        scope.content()
    }
}

@Composable
fun Test() {
    Column {
        var title by remember { mutableStateOf("") }
        var isAnimation by remember { mutableStateOf(true) }

        GooeyBox(
            Modifier
                .width(300.dp)
                .background(Color.Black), isAnimation, 60
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
                    .padding(80.dp)
                    .size(width = 200.dp, height = 20.dp)
                    .gooey(0.8f, Color.Gray)
                    .clickable { title = "1" })
            Box(
                Modifier
                    .size(40.dp)
                    .graphicsLayer(translationX = 200f)
                    .background(Color.Blue, CircleShape)
                    .clickable { title = "2" })
            Box(
                Modifier
                    .size(40.dp)
                    .graphicsLayer(translationX = 400f)
                    .gooey(0.8f, Color.Red)
                    .clickable { title = "3" })

            Box(
                Modifier
                    .padding(start = 100.dp)
                    .size(40.dp)
                    .graphicsLayer(translationY = progress * 400f)
                    .gooey(0.8f, Color.Green, CircleShape)
                    .clickable { title = "4" })
        }
        Text(text = title)
        Button(onClick = { isAnimation = !isAnimation }) {

        }
    }

//    GooeyEffect()
}

@Composable
fun GooeyEffect() {
    val circle1 = remember { IconCircle(30f, R.drawable.ic_launcher_background) }
    val circle2 = remember { IconCircle(30f, R.drawable.ic_launcher_background) }
    val circle3 = remember { IconCircle(30f, R.drawable.ic_launcher_background) }
    val circle4 = remember {
        IconCircle(60f, R.drawable.ic_launcher_foreground).apply {
            translationX = 650f
        }
    }

    val progress by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    circle4.radius = 60f

    circle1.translationX = progress * 600 + 200f
    circle2.translationX = progress * 600 + 350f
    circle3.translationX = progress * 600 + 500f

    val image = remember { ImageBitmap(1600, 500) }

    val blurPaint = remember {
        Paint().apply {
            asFrameworkPaint().maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
            color = Color.White
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            image.asAndroidBitmap().recycle()
        }
    }

    val colorFilterPaint = remember {
        Paint().apply {
            val contrast = 48f
            val brightness = 10000f

            val cm = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, brightness,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, contrast, -brightness
                )
            )

            colorFilter = ColorFilter.colorMatrix(cm)
        }
    }

    Canvas(image).apply {
        image.reuse()

        val path1 = circle1.createPath()
        val path2 = circle2.createPath()
        val path3 = circle3.createPath()
        val path4 = circle4.createPath()

        drawPath(path1, blurPaint)
        drawPath(path2, blurPaint)
        drawPath(path3, blurPaint)
        drawPath(path4, blurPaint)
    }

    val icon = painterResource(id = circle4.iconResId)
    val density = LocalDensity.current
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
            .padding(20.dp)
            .drawWithContent {
                drawIntoCanvas {
                    it.drawImage(image, Offset.Zero, colorFilterPaint)
                    drawContent()
                }
            }
    ) {
        Icon(
            icon,
            contentDescription = null,
            Modifier
                .size(with(density) { circle4.radius.toDp() })
                .graphicsLayer(
                    translationX = circle4.translationX - circle4.radius / 2,
                    translationY = circle4.translationY - circle4.radius / 2
                )
        )
    }
}

internal fun ImageBitmap.reuse() = asAndroidBitmap().eraseColor(android.graphics.Color.TRANSPARENT)

object AnimationUtils {

    fun to1f0f(progress: Float, duration: Float = 1f) = when (progress) {
        in 0f..duration -> 1f - progress / duration
        else -> 0f
    }

    fun to1f0f1f(progress: Float, duration: Float = 1f) = when (progress) {
        in 0f..duration / 2f -> progress / duration
        in duration / 2f..duration -> 1f - (progress / duration)
        else -> 0f
    }
}