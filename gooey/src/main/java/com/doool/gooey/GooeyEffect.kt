package com.doool.gooey

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer

fun Modifier.gooeyEffect(intensity: GooeyIntensity = GooeyIntensity.Medium): Modifier {
    return this.then(GooeyColorFilterDrawModifier(intensity))
}

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


internal fun createGooeyColorFilter(intensity: GooeyIntensity) = ColorFilter.colorMatrix(
    ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f, 0f,
            0f, 0f, 1f, 0f, 0f,
            0f, 0f, 0f, intensity.alpha, intensity.shift
        )
    )
)
