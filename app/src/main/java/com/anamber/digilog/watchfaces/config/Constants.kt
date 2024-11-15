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

import androidx.recyclerview.widget.LinearLayoutManager
import com.anamber.digilog.tile.TickDrawer

object Constants {
    //fonts
    const val ROMAN_FONT_FAMILY_PATH = "fonts/bplatin_numerals.otf"

    //first: path, second: size, third: horizontal origin offset
    val FONT_FAMILIES = arrayOf(
        Triple("fonts/varela_regular.ttf", 1.0f, 1.0f),
        Triple("fonts/digital_dream_narrow.ttf", 1.0f, 0.8f),
        Triple("fonts/griffy_regular.ttf", 1.0f, 1.0f),
        Triple("fonts/peralta_regular.ttf", 1.0f, 1.0f),
        Triple("fonts/halo_3.ttf", 0.8f, 1.2f),
        Triple("fonts/bitwise.ttf", 1.0f, 1.0f),
        Triple("fonts/oregano_regular.ttf", 1.0f, 1.0f)
    )

    //dimens
    const val RATIO_80_TO_HEIGHT = 0.25f
    const val RATIO_26_TO_HEIGHT = 0.08125f
    const val RATIO_20_TO_HEIGHT = 0.0625f
    const val RATIO_15_TO_HEIGHT = 0.046875f
    const val RATIO_10_TO_HEIGHT = 0.03125f
    const val RATIO_100_TO_HEIGHT = 0.3125f
    const val RATIO_5_TO_HEIGHT = 0.015625f
    const val RATIO_35_TO_HEIGHT = 0.109375f

    //tick
    val TICK_TYPE_DEFAULT: TickDrawer.TickType = TickDrawer.TickType.STANDARD
    val TICK_NUMBERS_DEFAULT: TickDrawer.TickNumbers = TickDrawer.TickNumbers.HOURLY
    const val TICK_FAMILY_INDEX_DEFAULT = 0
}

