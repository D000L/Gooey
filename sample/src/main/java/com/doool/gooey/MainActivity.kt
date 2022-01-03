package com.doool.gooey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.doool.gooey.samples.CircleRotation
import com.doool.gooey.samples.GooeyEffectButton
import com.doool.gooey.samples.RandomGooeyBubbleCanvas

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box {
                RandomGooeyBubbleCanvas(Modifier.fillMaxSize())
                CircleRotation(Modifier.align(Alignment.Center))
                GooeyEffectButton(Modifier.align(Alignment.Center))
            }
        }
    }
}

