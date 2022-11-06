package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer

internal class GooeyColorFilterDrawModifier(intensity: GooeyIntensity) : DrawModifier {
    private val paint = Paint().apply {
        colorFilter = createGooeyColorFilter(intensity)
    }

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(Rect(Offset.Zero, size), paint) {
                drawContent()
            }
        }
    }
}

internal fun createBlurPaint(
    intensity: Float,
    blurType: BlurMaskFilter.Blur = BlurMaskFilter.Blur.NORMAL
) = Paint().apply {
    asFrameworkPaint().maskFilter = BlurMaskFilter(intensity, blurType)
}
