package com.doool.gooey

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class GooeyShape(val graphicShape: Shape) {

    fun createPath(size: Size, layoutDirection: LayoutDirection, density: Density): Path {
        return Path().apply {
            addOutline(
                graphicShape.createOutline(size, layoutDirection, density)
            )
        }
    }
}

val RectangleGooeyShape = GooeyShape(RectangleShape)

val CircleGooeyShape = RoundedRectangleGooeyShape(50)

fun RoundedRectangleGooeyShape(percent: Int) =
    RoundedRectangleGooeyShape(CornerSize(percent))

fun RoundedRectangleGooeyShape(corner: CornerSize) =
    RoundedRectangleGooeyShape(corner, corner, corner, corner)

fun RoundedRectangleGooeyShape(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp
) = RoundedRectangleGooeyShape(
    CornerSize(topStart),
    CornerSize(topEnd),
    CornerSize(bottomEnd),
    CornerSize(bottomStart)
)

fun RoundedRectangleGooeyShape(
    topStart: CornerSize = CornerSize(0),
    topEnd: CornerSize = CornerSize(0),
    bottomEnd: CornerSize = CornerSize(0),
    bottomStart: CornerSize = CornerSize(0)
) = GooeyShape(RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart))


