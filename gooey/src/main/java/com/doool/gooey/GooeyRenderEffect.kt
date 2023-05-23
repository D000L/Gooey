package com.doool.gooey

import androidx.compose.ui.Modifier

fun Modifier.gooeyEffect(intensity: GooeyIntensity = GooeyIntensity.Medium): Modifier {
    return this.then(GooeyColorFilterDrawModifier(intensity))
}
