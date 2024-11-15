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

import android.graphics.Canvas
import android.graphics.Paint

open class BaseWatchFaceDrawer {

    internal var mSecondHandLength: Float = 0.toFloat()
    internal var mCenterX: Float = 0.toFloat()
    internal var mCenterY: Float = 0.toFloat()

    enum class WatchFaceType {
        NONE, MINIMAL, PENTAGON, PIECE_OF_CAKE, ROUND_CENTER
    }

    open fun calculateSizes(width: Int, height: Int, centerX: Float, centerY: Float) {
        mSecondHandLength = width / 8.0f
        mCenterX = centerX
        mCenterY = centerY
    }

    open fun drawWatchFace(
        canvas: Canvas, 
        secondsRotation: Float, 
        minutes: Int, 
        hoursRotation: Float, 
        showSecondHand: Boolean,
        mHourPaint: Paint, 
        mFontMinutePaint: Paint, 
        mMinuteFontVerticalOffset: Float,
        mSecondPaint: Paint, 
        mAmbient: Boolean
    ) {
        // This method will be implemented in subclasses
    }
}

