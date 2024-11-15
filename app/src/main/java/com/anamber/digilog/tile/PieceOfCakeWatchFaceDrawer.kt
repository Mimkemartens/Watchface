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

import android.graphics.*
import com.anamber.digilog.tile.MathHelper.getPointRotatedByDegrees

class PieceOfCakeWatchFaceDrawer : BaseWatchFaceDrawer() {

    private var mSmallCircleRect: RectF? = null
    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()

    override fun calculateSizes(width: Int, height: Int, mCenterX: Float, mCenterY: Float) {
        super.calculateSizes(width, height, mCenterX, mCenterY)

        mSecondHandLength *= 0.8f

        mSmallCircleRect = RectF(
            mCenterX - mSecondHandLength,
            -mSecondHandLength,
            mCenterX + mSecondHandLength,
            mSecondHandLength
        )

        mWidth = width.toFloat()
        mHeight = height.toFloat()
    }

    override fun drawWatchFace(
        canvas: Canvas, secondsRotation: Float, minutes: Int, hoursRotation: Float, showSecondHand: Boolean,
        mHourPaint: Paint, mFontMinutePaint: Paint, mMinuteFontVerticalOffset: Float,
        mSecondPaint: Paint, mAmbient: Boolean
    ) {

        // Hour
        canvas.rotate(hoursRotation, mCenterX, mCenterY)

        val drawingPath = Path()
        drawingPath.moveTo(mCenterX, 0f)
        drawingPath.arcTo(0f, 0f, mWidth, mHeight, 60f, 60f, false)
        drawingPath.lineTo(mCenterX, 0f)

        canvas.drawPath(drawingPath, mHourPaint)

        // Reset canvas after drawing hour hand
        canvas.rotate(-hoursRotation, mCenterX, mCenterY)

        // Minute
        if (minutes != 0) {
            val minuteCenter = getPointRotatedByDegrees(mCenterX, mCenterY, mCenterX, mCenterY * 0.6f, (hoursRotation + 180).toDouble())
            canvas.drawText(Integer.toString(minutes), minuteCenter.x.toFloat(), minuteCenter.y + mMinuteFontVerticalOffset, mFontMinutePaint)
        } else {
            // Draw a centered dot for zero minutes
            val dotRadius = 5f // Adjust the dot size as needed
            canvas.drawCircle(mCenterX, mCenterY - dotRadius / 2, dotRadius, mFontMinutePaint)
        }

        // Second
        if (!mAmbient && showSecondHand) {
            canvas.rotate(secondsRotation, mCenterX, mCenterY)

            drawingPath.reset()
            drawingPath.moveTo(mCenterX, 0f)
            drawingPath.arcTo(
                mSmallCircleRect!!.left,
                mSmallCircleRect!!.top,
                mSmallCircleRect!!.right,
                mSmallCircleRect!!.bottom,
                65f,
                50f,
                false
            )
            drawingPath.lineTo(mCenterX, 0f)

            canvas.drawPath(drawingPath, mSecondPaint)
        }
    }
}