package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection

@LayoutScopeMarker
@Immutable
interface GooeyScope : BoxScope {

    val intensity: Float

    fun Modifier.gooey(
        color: Color,
        shape: Shape,
        solidShape: Boolean = false
    ): Modifier
}

internal class GooeyScopeImpl(boxScope: BoxScope, override val intensity: Float) : GooeyScope,
    BoxScope by boxScope {

    override fun Modifier.gooey(
        color: Color,
        shape: Shape,
        solidShape: Boolean
    ): Modifier = composed {
        val layoutDirection = LocalLayoutDirection.current
        val density = LocalDensity.current

        val gooeyModifier =
            remember(color, intensity, solidShape) { GooeyModifier(color, intensity, solidShape) }

        val sizeAndClickModifier =
            remember {
                Modifier
                    .onSizeChanged { size ->
                        gooeyModifier.updatePath(Path().apply {
                            val size =
                                Size(width = size.width.toFloat(), height = size.height.toFloat())

                            addOutline(shape.createOutline(size, layoutDirection, density))
                        })
                    }
                    .clickable(false) { }
            }

        this
            .then(sizeAndClickModifier)
            .then(gooeyModifier)
            .clip(shape)
    }
}

internal class GooeyModifier(
    private val color: Color,
    intensity: Float,
    solidShape: Boolean = false
) : DrawModifier {

    private var path = Path()
    private val blurPaint = createBlurPaint(
        intensity,
        if (solidShape) BlurMaskFilter.Blur.SOLID else BlurMaskFilter.Blur.NORMAL
    ).apply {
        this.color = this@GooeyModifier.color
    }

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.drawPath(path, blurPaint)
            drawContent()
        }
    }

    fun updatePath(path: Path) {
        this.path = path
    }
}
