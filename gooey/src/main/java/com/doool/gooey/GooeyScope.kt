package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.toSize

@LayoutScopeMarker
@Immutable
interface GooeyScope {

    fun Modifier.gooey(
        color: Color,
        shape: GooeyShape
    ): Modifier
}

internal class GooeyScopeImpl : GooeyScope {

    override fun Modifier.gooey(
        color: Color,
        shape: GooeyShape
    ): Modifier = composed {
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current

        var size by remember { mutableStateOf(Size.Zero) }
        val path = remember(size) { shape.createPath(size, layoutDirection, density) }

        val sizeAndClickModifier =
            remember {
                Modifier
                    .onSizeChanged { size = it.toSize() }
                    .clickable(false) { } }
        val gooeyModifier = remember(path, color) { GooeyModifier(path, color) }

        this
            .then(sizeAndClickModifier)
            .then(gooeyModifier)
            .clip(shape.graphicShape)
    }
}

internal val BlurPaint = Paint().apply {
    asFrameworkPaint().maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
}

internal class GooeyModifier(
    private val path: Path,
    private val color: Color
) : DrawModifier {

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.drawPath(path, BlurPaint.apply {
                this.color = this@GooeyModifier.color
            })
            drawContent()
        }
    }
}