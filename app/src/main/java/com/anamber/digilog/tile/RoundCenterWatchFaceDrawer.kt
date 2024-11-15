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

class RoundCenterWatchFaceDrawer : BaseWatchFaceDrawer() {

    private var mBigCircleRect: RectF? = null
    private var mSmallCircleRect: RectF? = null

    override fun calculateSizes(width: Int, height: Int, mCenterX: Float, mCenterY: Float) {
        super.calculateSizes(width, height, mCenterX, mCenterY)

        val mBigCircleRadius = width / 5.0f   //works with 70 degree circle touch point; startAngle -20; sweapAngle 220
        mBigCircleRect = RectF(
            mCenterX - mBigCircleRadius,
            mCenterY - mBigCircleRadius,
            mCenterX + mBigCircleRadius,
            mCenterY + mBigCircleRadius
        )

        val mSmallCircleRadius = mSecondHandLength / 3.0f
        mSmallCircleRect = RectF(
            mCenterX - mSmallCircleRadius,
            mSmallCircleRadius,
            mCenterX + mSmallCircleRadius,
            mSecondHandLength
        )
    }

override fun drawWatchFace(
    canvas: Canvas, secondsRotation: Float, minutes: Int, hoursRotation: Float, showSecondHand: Boolean,
    mHourPaint: Paint, mFontMinutePaint: Paint, mMinuteFontVerticalOffset: Float,
    mSecondPaint: Paint, mAmbient: Boolean
) {

    //################################################################################
    //# Hour
    //################################################################################

    canvas.rotate(hoursRotation, mCenterX, mCenterY)

    val drawingPath = Path()
    drawingPath.moveTo(mCenterX, 0f)
    drawingPath.arcTo(
        mBigCircleRect!!.left,
        mBigCircleRect!!.top,
        mBigCircleRect!!.right,
        mBigCircleRect!!.bottom,
        -20f,
        220f,
        false
    )
    drawingPath.lineTo(mCenterX, 0f)

    canvas.drawPath(drawingPath, mHourPaint)

    //reset canvas after drawing hour hand
    canvas.rotate(-hoursRotation, mCenterX, mCenterY)


    //################################################################################
    //# minute
    //################################################################################

    if (minutes != 0) {
        // Draw the minutes
        canvas.drawText(Integer.toString(minutes), mCenterX, mCenterY + mMinuteFontVerticalOffset, mFontMinutePaint)
    } else {
        // Draw a centered dot for zero minutes
        val dotRadius = 5f // Adjust the dot size as needed
        canvas.drawCircle(mCenterX, mCenterY - dotRadius / 2, dotRadius, mFontMinutePaint)
    }


    //################################################################################
    //# second
    //################################################################################

    if (!mAmbient && showSecondHand) {
        canvas.rotate(secondsRotation, mCenterX, mCenterY)

        drawingPath.reset()
        drawingPath.moveTo(mCenterX, 0f)
        drawingPath.arcTo(
            mSmallCircleRect!!.left,
            mSmallCircleRect!!.top,
            mSmallCircleRect!!.right,
            mSmallCircleRect!!.bottom,
            -30f,
            240f,
            false
        )
        drawingPath.lineTo(mCenterX, 0f)

        canvas.drawPath(drawingPath, mSecondPaint)
    }
}