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
package com.anamber.digilog.tile

import android.content.res.AssetManager
import android.graphics.Paint
import android.graphics.Typeface
import com.anamber.digilog.watchfaces.config.Constants

object FontHelper {

    const val FONT_DIRECTORY = "fonts/"

    fun updateFontPaint(fontPaint: Paint, tickType: TickDrawer.TickType, fontFamilyIndex: Int, assetManager: AssetManager): Int {
        return if (tickType == TickDrawer.TickType.ROMAN)
            setFontPaint(fontPaint, Constants.ROMAN_FONT_FAMILY_PATH, 1.0f, assetManager)
        else
            setFontPaint(fontPaint, fontFamilyIndex, assetManager)
    }

    fun setFontPaint(fontPaint: Paint, fontFamilyIndex: Int, assetManager: AssetManager): Int {
        val fontFamily = getFontFamily(fontFamilyIndex)

        return setFontPaint(fontPaint, fontFamily.first, fontFamily.third, assetManager)
    }

    private fun setFontPaint(fontPaint: Paint, fontFamilyPath: String, fontFamilyOrigin: Float, assetManager: AssetManager): Int {
        fontPaint.typeface = getTypeFace(fontFamilyPath, assetManager)
        return MathHelper.calculateVerticalOffset(fontPaint, fontFamilyOrigin)
    }

    fun getTypeFace(fontFamilyPath: String, assetManager: AssetManager): Typeface {
        return Typeface.createFromAsset(assetManager, FONT_DIRECTORY + fontFamilyPath)
    }

    fun getFontSize(baseSize: Float, fontFamilyIndex: Int): Float {
        val fontFamily = getFontFamily(fontFamilyIndex)
        return baseSize * fontFamily.second
    }

    private fun getFontFamily(fontFamilyIndex: Int): Triple<String, Float, Float> {
        return if (fontFamilyIndex >= 0 && fontFamilyIndex < Constants.FONT_FAMILIES.size) {
            Constants.FONT_FAMILIES[fontFamilyIndex]
        } else {
            Constants.FONT_FAMILIES[Constants.TICK_FAMILY_INDEX_DEFAULT]
        }
    }
}

