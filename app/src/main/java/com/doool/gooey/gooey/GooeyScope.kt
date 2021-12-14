package com.doool.gooey.gooey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged

@LayoutScopeMarker
@Immutable
interface GooeyScope {

    @Stable
    fun Modifier.gooey(
        tension: Float,
        color: Color,
        direction: GooeyDirection = GooeyDirection.Circle
    ): Modifier

    @Stable
    fun Modifier.gooeyFixedSize(
        tension: Float,
        color: Color,
        direction: GooeyDirection = GooeyDirection.Circle
    ): Modifier
}

internal class GooeyScopeImpl : GooeyScope {

    var visibleGooey: Boolean = false

    @Stable
    override fun Modifier.gooey(tension: Float, color: Color, direction: GooeyDirection): Modifier =
        composed {
            if (!visibleGooey) {
                return@composed when (direction) {
                    GooeyDirection.VerticalRectangle -> background(color, RectangleShape)
                    GooeyDirection.HorizontalRectangle -> background(color, RectangleShape)
                    GooeyDirection.Circle -> background(color, CircleShape)
                }
            }

            var width by remember { mutableStateOf(0) }
            var height by remember { mutableStateOf(0) }

            val modifier = this.onSizeChanged {
                width = it.width
                height = it.height
            }

            val colorList = mutableListOf<Pair<Float, Color>>()
            colorList.add(Pair(0f, color))
            colorList.add(Pair(1f - tension, color))
            colorList.add(Pair(1f - tension, color.copy(0.8f)))
            colorList.add(Pair(1f, color.copy(alpha = 0f)))

            return@composed when (direction) {
                GooeyDirection.HorizontalRectangle -> {
                    modifier
                        .background(
                            Brush.horizontalGradient(
                                *colorList.toTypedArray(),
                                startX = width / 2f,
                                endX = 0f
                            ), StartHalfRectangleShape
                        )
                        .background(
                            Brush.horizontalGradient(
                                *colorList.toTypedArray(),
                                startX = width / 2f,
                                endX = width.toFloat()
                            ), EndHalfRectangleShape
                        )

                }
                GooeyDirection.VerticalRectangle -> {
                    modifier
                        .background(
                            Brush.verticalGradient(
                                *colorList.toTypedArray(),
                                startY = height / 2f,
                                endY = 0f
                            ), TopHalfRectangleShape
                        )
                        .background(
                            Brush.verticalGradient(
                                *colorList.toTypedArray(),
                                startY = height / 2f,
                                endY = height.toFloat()
                            ), BottomHalfRectangleShape
                        )
                }
                GooeyDirection.Circle -> {
                    modifier.background(
                        Brush.radialGradient(*colorList.toTypedArray()),
                        CircleShape
                    )
                }
            }
        }

    @Stable
    override fun Modifier.gooeyFixedSize(
        tension: Float,
        color: Color,
        direction: GooeyDirection
    ): Modifier {
        if (!visibleGooey) {
            return when (direction) {
                GooeyDirection.VerticalRectangle -> background(color, RectangleShape)
                GooeyDirection.HorizontalRectangle -> background(color, RectangleShape)
                GooeyDirection.Circle -> background(color, CircleShape)
            }
        }

        val scaledTension = (1f / (1f + tension))
        return when (direction) {
            GooeyDirection.VerticalRectangle -> scale(scaleX = 1f, scaleY = 1f + tension)
            GooeyDirection.HorizontalRectangle -> scale(scaleX = 1f + tension, scaleY = 1f)
            GooeyDirection.Circle -> scale(1f + tension)
        }
            .gooey(1f - scaledTension, color, direction)
            .scale(1f)
    }
}