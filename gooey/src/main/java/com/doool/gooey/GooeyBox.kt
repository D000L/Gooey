package com.doool.gooey

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

internal val Gooey2ColorFilter = run {
    val contrast = 48f
    val brightness = 10000f

    ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, contrast, -brightness
            )
        )
    )
}

@Composable
fun GooeyBox(modifier: Modifier = Modifier, content: @Composable GooeyScope.() -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Box(modifier.then(GooeyBoxModifier())) {
            val scope = remember { GooeyScopeImpl(this) }
            content(scope)
        }
    }
}

internal class GooeyBoxModifier : DrawModifier {
    private val paint = Paint().apply {
        colorFilter = Gooey2ColorFilter
    }

    override fun ContentDrawScope.draw() {
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(Rect(Offset.Zero, size), paint) {
                drawContent()
            }
        }
    }
}