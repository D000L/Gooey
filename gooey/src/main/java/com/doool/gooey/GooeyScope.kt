package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.OnRemeasuredModifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

val LocalGooeyIntensity = compositionLocalOf { GooeyIntensity.Medium }

fun Modifier.gooeyBackground(
    color: Color,
    shape: Shape,
    solidShape: Boolean = true
): Modifier = composed {
    val intensity = LocalGooeyIntensity.current.intensity
    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current

    val gooeyModifier = remember(intensity, solidShape, layoutDirection, density) {
        GooeyModifier(
            color = color,
            shape = shape,
            layoutDirection = layoutDirection,
            density = density,
            intensity = intensity,
            solidShape = solidShape
        )
    }

    LaunchedEffect(key1 = color, block = {
        gooeyModifier.updateColor(color)
    })

    then(gooeyModifier)
}

internal class GooeyModifier(
    private val shape: Shape,
    private val layoutDirection: LayoutDirection,
    private val density: Density,
    color: Color,
    intensity: Float,
    solidShape: Boolean = false
) : DrawModifier, OnRemeasuredModifier {

    private var currentColor: Color = color

    private var path = Path()
    private var blurPaint = createBlurPaint(
        intensity,
        if (solidShape) BlurMaskFilter.Blur.SOLID else BlurMaskFilter.Blur.NORMAL
    )

    override fun onRemeasured(size: IntSize) {
        this.path = Path().apply {
            val size = Size(width = size.width.toFloat(), height = size.height.toFloat())
            addOutline(shape.createOutline(size, layoutDirection, density))
        }
    }

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.drawPath(path, blurPaint.apply {
                color = currentColor
            })
            drawContent()
        }
    }

    fun updateColor(color: Color) {
        currentColor = color
    }
}
