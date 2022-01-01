package com.doool.gooey

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(){
                var count by remember { mutableStateOf(0)}
                Button(onClick = { count++ }) {
                    Text(count.toString())
                }
                Testt()
//                Box(Modifier.drawBehind {
//                    drawPath(Path().apply {asAndroidPath().addCircle(0f,0f,100f,android.graphics.Path.Direction.CW)
//                    }, Color.Blue)
////            this@CircleGooey.setPath(key, )
//                }) {
////
//        content()
                }
//                CircleRotation()
            }
        }
//    }
}

