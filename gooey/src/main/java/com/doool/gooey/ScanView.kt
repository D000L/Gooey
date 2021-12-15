package com.doool.gooey

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AbstractComposeView

internal class ScanView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbstractComposeView(context, attrs) {

    private var targetComposable: (@Composable () -> Unit)? = null
    private var frame by mutableStateOf(24)

    private var screenHeight by mutableStateOf(0)
    private var screenWight by mutableStateOf(0)

    fun setTarget(
        target: @Composable () -> Unit,
        frame: Int
    ) {
        targetComposable = target
        this.frame = frame
    }

    @Composable
    override fun Content() {
        Box(Modifier.onSizeChanged {
            screenHeight = it.height
            screenWight = it.width
        }) {
            targetComposable?.invoke()
        }
    }

    @Composable
    fun getScanState(): State<ImageBitmap?> {
        val imageBitmap = remember(screenWight, screenHeight) {
            if (screenWight > 0 && screenHeight > 0) {
                ImageBitmap(screenWight, screenHeight)
            } else null
        }
        val canvas = remember(imageBitmap) { imageBitmap?.let { Canvas(it) } }

        return infinityAnimateValueAsState(canvas, frame) {
            imageBitmap?.reuse()
            draw(canvas?.nativeCanvas)
            imageBitmap
        }
    }
}

@Composable
private fun <T> infinityAnimateValueAsState(
    key: Any?,
    frame: Int,
    onFrame: () -> T?
): State<T?> {
    var start = 0L
    val delay = 1000L / frame

    return produceState<T?>(key1 = key, key2 = frame, initialValue = null) {
        while (true) {
            withFrameMillis { frame ->
                if (start + delay < frame) {
                    start = frame

                    this.value = onFrame()
                }
            }
        }
    }
}

internal fun ImageBitmap.reuse() =
    asAndroidBitmap().eraseColor(android.graphics.Color.TRANSPARENT)
