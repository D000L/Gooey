package com.doool.gooey

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.viewinterop.AndroidView

private const val Contrast = 48f
private const val Brightness = 10000f

private val ColorMatrix = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, Contrast, -Brightness
    )
)

private val GooeyColorFilter = ColorFilter.colorMatrix(ColorMatrix)

@Composable
fun GooeyBox(
    modifier: Modifier = Modifier,
    frame: Int = 60,
    activeGooey: Boolean = true,
    content: @Composable GooeyScope.() -> Unit
) {
    val scope = remember(activeGooey) { GooeyScopeImpl(activeGooey) }

    var scanView: ScanView? by remember { mutableStateOf(null) }
    val image = scanView?.getScanState()

    val modifier = if (activeGooey) modifier.drawWithContent {
        image?.value?.let {
            drawImage(it, Offset.Zero, colorFilter = GooeyColorFilter)
        }
    } else modifier

    Box(modifier) {
        if (activeGooey) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = {
                ScanView(it).apply {
                    setTarget(
                        frame = frame,
                        target = { scope.content() }
                    )

                    scanView = this
                }
            })
        } else {
            scope.content()
        }
    }
}