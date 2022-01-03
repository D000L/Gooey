package com.doool.gooey.samples

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.doool.gooey.GooeyBox
import com.doool.gooey.GooeyIntensity
import com.doool.gooey.SlowOutFastInEasing

/*
 * design source from : https://codepen.io/Grsmto/full/RPQPPB
 */
@Composable
fun GooeyEffectButton(modifier: Modifier = Modifier) {
    var isGooeyShow by remember { mutableStateOf(false) }

    val animatedDp by animateDpAsState(
        targetValue = if (isGooeyShow) -32.dp else 0.dp,
        tween(durationMillis = if (isGooeyShow) 600 else 0, easing = SlowOutFastInEasing)
    ) {
        isGooeyShow = false
    }

    val animatedSize by animateDpAsState(
        targetValue = if (isGooeyShow) 30.dp else 4.dp,
        tween(durationMillis = if (isGooeyShow) 100 else 0, easing = LinearEasing)
    )

    GooeyBox(
        modifier,
        GooeyIntensity.Custom(20f, 40f, -5000f)
    ) {
        if (isGooeyShow) {
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .size(animatedSize)
                    .offset(
                        x = animatedDp,
                        y = animatedDp
                    )
                    .gooey(Color.Green, CircleShape)
            )
            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(animatedSize)
                    .offset(
                        x = -animatedDp,
                        y = -animatedDp
                    )
                    .gooey(Color.Green, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .height(height = 56.dp)
                .gooey(Color.Green, RectangleShape, true)
                .clickable {
                    isGooeyShow = !isGooeyShow
                }
                .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
        ) {
            Text(text = "Show GooeyEffect!!")
        }
    }
}