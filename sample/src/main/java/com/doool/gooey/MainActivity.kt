package com.doool.gooey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box {
//                var count by remember { mutableStateOf(0) }
//                Button(onClick = { count++ }) {
//                    Text(count.toString())
//                }
//                Testt()
                GooeyEffectButton()
//                RandomGooeyBubbleCanvas(Modifier.fillMaxSize())
//                Sample(Modifier.fillMaxSize())
//                GooeyFab(
//                    Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(bottom = 20.dp, end = 20.dp))
            }
        }
    }
}

