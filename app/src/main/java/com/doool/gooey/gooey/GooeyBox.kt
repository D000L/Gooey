package com.doool.gooey.gooey

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun GooeyBox(
    modifier: Modifier = Modifier,
    frame: Int = 60,
    gooeyVisibility: Boolean = true,
    content: @Composable GooeyScope.() -> Unit
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    val scope = remember(gooeyVisibility) {
        GooeyScopeImpl().apply {
            visibleGooey = gooeyVisibility
        }
    }
    val colorFilterPaint = remember {
        Paint().apply {
            val contrast = 48f
            val brightness = 10000f

            val cm = ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, contrast, -brightness
                )
            )

            colorFilter = ColorFilter.colorMatrix(cm)
        }
    }

    val modifier = if (gooeyVisibility) modifier.drawWithContent {
        drawIntoCanvas { canvas ->
            image?.let { canvas.drawImage(it, Offset.Zero, colorFilterPaint) }
        }
    } else modifier

    Box(modifier) {
        if (gooeyVisibility) {
            AndroidView(modifier = modifier, factory = {
                ScanView(it).apply {
                    setScanTarget { scope.content() }
                    setListener {
                        image = it
                    }
                    this.frame = frame
                }
            })
        } else {
            scope.content()
        }
    }
}