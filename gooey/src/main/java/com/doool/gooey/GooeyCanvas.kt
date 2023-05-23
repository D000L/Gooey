package com.doool.gooey

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Density

@Composable
fun GooeyCanvas(modifier: Modifier = Modifier, onDraw: GooeyCanvasScope.() -> Unit) {
    val intensity = LocalGooeyIntensity.current.intensity
    val paint = remember { createBlurPaint(intensity) }

    Canvas(modifier = modifier, onDraw = {
        val scope = GooeyCanvasScopeImpl(this, paint)
        scope.onDraw()
    })
}

@LayoutScopeMarker
@Immutable
interface GooeyCanvasScope : DrawScope {
    fun DrawScope.drawGooey(size: Size, color: Color, shape: Shape, offset: Offset)
}

internal class GooeyCanvasScopeImpl(
    drawScope: DrawScope, private val paint: Paint
) : GooeyCanvasScope, DrawScope by drawScope {

    override fun DrawScope.drawGooey(size: Size, color: Color, shape: Shape, offset: Offset) {
        val path = Path().apply {
            addOutline(shape.createOutline(size, layoutDirection, Density(density)))
            translate(offset)
        }

        drawIntoCanvas {
            it.drawPath(path, this@GooeyCanvasScopeImpl.paint.apply {
                this.color = color
            })
        }
    }
}
