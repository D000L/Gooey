package com.doool.gooey

sealed class GooeyIntensity(
    val intensity: Float,
    val alpha: Float = intensity * 4f,
    val shift: Float = -250f * intensity
) {
    object Low : GooeyIntensity(10f)
    object Medium : GooeyIntensity(20f)
    object High : GooeyIntensity(40f)
    class Custom(intensity: Float, alpha: Float, shift: Float) :
        GooeyIntensity(intensity, alpha, shift)
}
