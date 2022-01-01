package com.doool.gooey

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap

internal fun ImageBitmap.reuse() =
    asAndroidBitmap().eraseColor(android.graphics.Color.TRANSPARENT)