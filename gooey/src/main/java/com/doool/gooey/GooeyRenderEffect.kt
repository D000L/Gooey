package com.doool.gooey

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.*
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asAndroidColorFilter
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@RequiresApi(Build.VERSION_CODES.S)
private fun createColorFilterEffect(intensity: GooeyIntensity = GooeyIntensity.Medium): RenderEffect {
    return android.graphics.RenderEffect.createColorFilterEffect(
        createGooeyColorFilter(intensity).asAndroidColorFilter()
    ).asComposeRenderEffect()
}

fun Modifier.gooeyEffect(intensity: GooeyIntensity = GooeyIntensity.Medium): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.graphicsLayer(renderEffect = createColorFilterEffect(intensity))
    } else {
        this.then(GooeyColorFilterDrawModifier(intensity))
    }
}
