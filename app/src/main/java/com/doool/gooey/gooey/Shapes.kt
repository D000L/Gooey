package com.doool.gooey.gooey

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Stable
internal val TopHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rectangle {
        return Outline.Rectangle(
            Rect(
                Offset(0f, 0f),
                Offset(size.width, size.height / 2f)
            )
        )
    }


    override fun toString(): String = "TopHalfRectangleShape"
}

@Stable
internal val BottomHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rectangle {
        return Outline.Rectangle(
            Rect(
                Offset(0f, size.height / 2f),
                Offset(size.width, size.height)
            )
        )
    }


    override fun toString(): String = "BottomHalfRectangleShape"
}

@Stable
internal val StartHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rectangle {
        return Outline.Rectangle(
            Rect(
                Offset(0f, 0f),
                Offset(size.width / 2, size.height)
            )
        )
    }

    override fun toString(): String = "StartHalfRectangleShape"
}

@Stable
internal val EndHalfRectangleShape: Shape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline.Rectangle {
        return Outline.Rectangle(
            Rect(
                Offset(size.width / 2f, 0f),
                Offset(size.width, size.height)
            )
        )
    }

    override fun toString(): String = "EndHalfRectangleShape"
}