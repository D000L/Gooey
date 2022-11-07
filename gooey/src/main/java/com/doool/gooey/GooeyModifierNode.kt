package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.LayoutAwareModifierNode
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.gooeyBackground(color: Color, shape: Shape, solidShape: Boolean = true) =
    this.composed {
        val intensity = LocalGooeyIntensity.current.intensity
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current

        modifierElementOf(params = Pair(color, shape), create = {
            GooeyModifierNode(
                shape, layoutDirection, density, color, intensity, solidShape
            )
        }, update = {
            it.gooeyColor = color
        }, definitions = {
            name = "gooeyBackgroundNode"
            properties["color"] = color
            properties["shape"] = shape
            properties["solidShape"] = solidShape
        })
    }

@OptIn(ExperimentalComposeUiApi::class)
class GooeyModifierNode(
    private val shape: Shape,
    private val layoutDirection: LayoutDirection,
    private val density: Density,
    var gooeyColor: Color,
    intensity: Float,
    solidShape: Boolean = false
) : DrawModifierNode, LayoutAwareModifierNode, Modifier.Node() {
    private var path = Path()
    private var blurPaint = createBlurPaint(
        intensity, if (solidShape) BlurMaskFilter.Blur.SOLID else BlurMaskFilter.Blur.NORMAL
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
                color = gooeyColor
            })
            drawContent()
        }
    }
}
