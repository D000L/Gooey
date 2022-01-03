package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

sealed class GooeyIntensity(val intensity: Float, val alpha: Float = intensity * 4f, val shift: Float = -250f * intensity) {
    object Low : GooeyIntensity(10f)
    object Medium : GooeyIntensity(20f)
    object High : GooeyIntensity(40f)
    class Custom(intensity: Float, alpha: Float, shift: Float) : GooeyIntensity(intensity,alpha, shift)
}

@Composable
fun GooeyBox(
    modifier: Modifier = Modifier,
    intensity: GooeyIntensity = GooeyIntensity.Medium,
    content: @Composable GooeyScope.() -> Unit
) {
    val gooeyModifier =
        remember(intensity) { GooeyBoxModifier(intensity) }

    Box(
        Modifier
            .fillMaxSize()
            .then(gooeyModifier)
    ) {
        Box(modifier) {
            val scope =
                remember(intensity.intensity) { GooeyScopeImpl(this, intensity.intensity) }
            content(scope)
        }
    }
}

internal class GooeyBoxModifier(intensity: GooeyIntensity) : DrawModifier {
    private val paint = Paint().apply {
        colorFilter = ColorFilter.colorMatrix(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, intensity.alpha, intensity.shift
                )
            )
        )
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