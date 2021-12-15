package com.doool.gooey

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

@LayoutScopeMarker
@Immutable
interface GooeyScope {

    @Stable
    fun Modifier.gooey(
        tension: Float,
        color: Color,
        shape: Shape
    ): Modifier

    @Stable
    fun Modifier.gooeyFixedSize(
        tension: Float,
        color: Color,
        shape: Shape
    ): Modifier
}

internal class GooeyScopeImpl(var activeGooey: Boolean) : GooeyScope {

    @Stable
    override fun Modifier.gooey(tension: Float, color: Color, shape: Shape): Modifier =
        composed {
            if (!activeGooey) return@composed background(color, shape)

            var width by remember { mutableStateOf(0) }
            var height by remember { mutableStateOf(0) }

            val modifier = this.onSizeChanged {
                width = it.width
                height = it.height
            }

            val blurPaint = remember {
                Paint().apply {
                    asFrameworkPaint().maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
                    this.color = color
                }
            }

            return@composed when (shape) {
                RectangleShape -> {
                    modifier.drawWithContent {
                        drawIntoCanvas {
                            it.drawRect(Rect(0f, 0f, width.toFloat(), height.toFloat()), blurPaint)
                        }
                        drawContent()
                    }
                }
                is RoundedCornerShape -> {
                    val size = androidx.compose.ui.geometry.Size(width.toFloat(), height.toFloat())
                    val density = LocalDensity.current
                    modifier.drawWithContent {
                        drawIntoCanvas {
                            val path = Path()
                            path.addRoundRect(
                                RoundRect(
                                    0f,
                                    0f,
                                    width.toFloat(),
                                    height.toFloat(),
                                    CornerRadius(
                                        shape.topStart.toPx(size, density),
                                        shape.topStart.toPx(size, density)
                                    ),
                                    CornerRadius(
                                        shape.topEnd.toPx(size, density),
                                        shape.topEnd.toPx(size, density)
                                    ),
                                    CornerRadius(
                                        shape.bottomEnd.toPx(size, density),
                                        shape.bottomEnd.toPx(size, density)
                                    ),
                                    CornerRadius(
                                        shape.bottomStart.toPx(size, density),
                                        shape.bottomStart.toPx(size, density)
                                    )
                                )
                            )

                            it.drawPath(path, blurPaint)
                        }
                        drawContent()
                    }
                }
                CircleShape -> {
                    modifier.drawWithContent {
                        drawIntoCanvas {
                            it.drawCircle(Offset(width / 2f, height / 2f), width / 2f, blurPaint)
                        }
                        drawContent()
                    }
                }
                else -> this
            }
        }

    @Stable
    override fun Modifier.gooeyFixedSize(
        tension: Float,
        color: Color,
        shape: Shape
    ): Modifier {
        if (!activeGooey) return background(color, shape)

        val scaledTension = (1f / (1f + tension))
        return when (shape) {
            CircleShape -> scale(1f + tension)
            RectangleShape -> this
            is RoundedCornerShape -> this
            else -> this
        }
            .gooey(1f - scaledTension, color, shape)
            .scale(1f)
    }
}