package com.doool.gooey.gooey

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
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

    var composable: (@Composable () -> Unit)? = null
    var updatedView: (ImageBitmap) -> Unit = {}
    var frame by mutableStateOf(24)

    fun setScanTarget(composable: @Composable () -> Unit) {
        this.composable = composable
    }

    fun setListener(updatedView: (ImageBitmap) -> Unit) {
        this.updatedView = updatedView
    }

    init {
        visibility = View.GONE
    }

    @Composable
    override fun Content() {
        var height by remember { mutableStateOf(0) }
        var width by remember { mutableStateOf(0) }

        Box(Modifier.onSizeChanged {
            height = it.height
            width = it.width
        }) {
            composable?.invoke()
        }

        val imageBitmap = remember(width, height) {
            if (width > 0 && height > 0) {
                ImageBitmap(width, height)
            } else null
        }
        val canvas = remember(imageBitmap) { imageBitmap?.let { Canvas(it) } }

        LaunchedEffect(imageBitmap) {
            var start = 0L
            val delay = 1000L / frame
            while (true) {
                withInfiniteAnimationFrameMillis {
                    if (start + delay < it) {
                        start = it
                        if (width > 0 && height > 0) {
                            imageBitmap?.let {
                                it.reuse()
                                this@ScanView.draw(canvas?.nativeCanvas)
                                updatedView(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

internal fun ImageBitmap.reuse() =
    asAndroidBitmap().eraseColor(android.graphics.Color.TRANSPARENT)
