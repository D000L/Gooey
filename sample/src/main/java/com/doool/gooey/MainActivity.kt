package com.doool.gooey

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doool.gooey.samples.CircleRotation
import com.doool.gooey.samples.GooeyLoading
import com.doool.gooey.samples.RandomGooeyBubbleCanvas

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var select by remember { mutableStateOf(0) }
            Column {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(3) {
                        Box(
                            Modifier
                                .height(height = 24.dp)
                                .background(Color.Green, RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp)
                                .clickable {
                                    select = it
                                }) {
                            Text("Sample $it")
                        }
                    }
                }
                Box(Modifier.background(Color(0xff102132))) {
                    when (select) {
                        0 -> RandomGooeyBubbleCanvas(Modifier.align(Alignment.Center))
                        1 -> CircleRotation(Modifier.align(Alignment.Center))
                        2 -> GooeyLoading(Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}
