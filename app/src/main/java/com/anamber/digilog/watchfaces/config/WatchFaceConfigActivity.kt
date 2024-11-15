/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anamber.digilog.watchfaces.config

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState

class WatchFaceConfigActivity : ComponentActivity() {
    private val configItems = listOf(
        HeaderConfigItem("Clock Face"),
        ToggleConfigItem("Clock Face Type", R.drawable.ic_clock_face_type, true),
        ColorConfigItem("Ticks Color", R.ic_ticks_color, R.color.default_ticks_color),
        NumberConfigItem("Clock Face Numbers", R.ic_clock_face_numbers, 12),
        FontConfigItem("Clock Face Font", R.font.default_font),
        ColorConfigItem("Background Color", R.ic_background_color, R.color.default_background_color),
        ColorConfigItem("Hour Hand Color", R.drawable.ic_hour_hand_color, R.color.default_hour_hand_color),
        ToggleConfigItem("Hour Hand Fill", R.drawable.ic_hour_hand_fill, true),
        ColorConfigItem("Minute Number Color", R.drawable.ic_minute_number_color, R.color.default_minute_number_color),
        FontConfigItem("Minute Number Font", R.font.default_minute_number_font),
        ToggleConfigItem("Show Second Hand", R.drawable.ic_second_hand, true),
        ColorConfigItem("Second Hand Color", R.drawable.ic_second_hand_color, R.color.default_second_hand_color),
        ToggleConfigItem("Second Hand Fill", R.drawable.ic_second_hand_fill, true),
        ToggleConfigItem("Unread Notifications", R.drawable.ic_unread_notifications, true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(configItems.size) { index ->
                    ConfigItemView(item = configItems[index])
                }
            }
        }
    }

    @Composable
    private fun ConfigItemView(item: ConfigItem) {
        when (item) {
            is HeaderConfigItem -> {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is ToggleConfigItem -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle toggle click */ }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = item.isChecked,
                        onCheckedChange = { /* Handle switch state change */ },
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }
            is ColorConfigItem -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle color click */ }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = MaterialTheme.colors.secondary, shape = CircleShape)
                    )
                }
            }
            is NumberConfigItem -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle number click */ }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = item.value.toString(),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            is FontConfigItem -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle font click */ }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_font),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Sample",
                        style = MaterialTheme.typography.body1,
                        fontFamily = item.fontResId
                    )
                }
            }
        }
    }
}

sealed class ConfigItem

data class HeaderConfigItem(val title: String) : ConfigItem()

data class ToggleConfigItem(
    val title: String,
    val iconResId: Int,
    val isChecked: Boolean
) : ConfigItem()

data class ColorConfigItem(
    val title: String,
    val iconResId: Int,
    val colorResId: Int
) : ConfigItem()

data class NumberConfigItem(
    val title: String,
    val iconResId: Int,
    val value: Int
) : ConfigItem()

data class FontConfigItem(
    val title: String,
    val fontResId: Int
) : ConfigItem()