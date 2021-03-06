package com.doool.gooey

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density


@Composable
fun GooeyScope.GooeyCanvas(modifier: Modifier = Modifier, onDraw: GooeyCanvasScope.() -> Unit) {
    val paint = remember { createBlurPaint(intensity) }

    Canvas(modifier = modifier, onDraw = {
        val scope = GooeyCanvasScopeImpl(this, paint)
        scope.onDraw()
    })
}

@LayoutScopeMarker
@Immutable
interface GooeyCanvasScope : DrawScope {

    fun Canvas.drawGooey(
        size: Size,
        color: Color,
        shape: Shape,
        offset: Offset
    )
}

internal class GooeyCanvasScopeImpl(
    drawScope: DrawScope,
    private val paint: Paint
) :
    GooeyCanvasScope, DrawScope by drawScope {

    override fun Canvas.drawGooey(
        size: Size,
        color: Color,
        shape: Shape,
        offset: Offset
    ) {
        val path = Path().apply {
            addOutline(shape.createOutline(size, layoutDirection, Density(density)))
            translate(offset)
        }

        drawPath(path, paint.apply {
            this.color = color
        })
    }
}