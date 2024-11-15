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

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class ConfigAdapter(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.analog_watch_face_preferences_file_key),
        Context.MODE_PRIVATE
    )

    fun getColorTintList(): List<ColorTintConfig> {
        return listOf(
            ColorTintConfig(
                R.string.color_black_label,
                R.color.black,
                isSelected = isColorSelected(R.color.black)
            ),
            ColorTintConfig(
                R.string.color_white_label,
                R.color.white,
                isSelected = isColorSelected(R.color.white)
            ),
            ColorTintConfig(
                R.string.color_grey_label,
                R.color.grey,
                isSelected = isColorSelected(R.color.grey)
            ),
            // Weitere Farben hier hinzufügen
        )
    }

    fun saveColorSelection(colorResId: Int) {
        sharedPreferences.edit()
            .putInt(SELECTED_COLOR_TINT_KEY, colorResId)
            .apply()
    }

    private fun isColorSelected(colorResId: Int): Boolean {
        val savedColor = sharedPreferences.getInt(SELECTED_COLOR_TINT_KEY, R.color.default_color)
        return savedColor == colorResId
    }

    companion object {
        private const val SELECTED_COLOR_TINT_KEY = "selected_color_tint"
    }
}

data class ColorTintConfig(
    @StringRes val labelResId: Int,
    @ColorRes val colorResId: Int,
    val isSelected: Boolean
)