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

class MinimalWatchFaceDrawer : BaseWatchFaceDrawer() {

    private lateinit var mBigStartPoint: PointF
    private lateinit var mBigStickTopRect: RectF
    private lateinit var mBigStickBottomRect: RectF

    private lateinit var mSmallStartPoint: PointF
    private lateinit var mSmallStickTopRect: RectF
    private lateinit var mSmallStickBottomRect: RectF

    private val nullMinuteDotSize = 5f // Standardgröße für den Null-Minuten-Punkt

    override fun calculateSizes(width: Int, height: Int, mCenterX: Float, mCenterY: Float) {
        super.calculateSizes(width, height, mCenterX, mCenterY)

        val bigStickWidth = width / 20.0f
        val bigStickHalfWidth = bigStickWidth / 2.0f
        val centerOffset = width / 12.0f

        mBigStartPoint = PointF(mCenterX - bigStickHalfWidth, bigStickWidth / 2.0f)
        mBigStickTopRect = RectF(
            mCenterX - bigStickHalfWidth,
            0f,
            mCenterX + bigStickHalfWidth,
            bigStickWidth
        )

        mBigStickBottomRect = RectF(
            mCenterX - bigStickHalfWidth,
            mCenterY - bigStickWidth - centerOffset,
            mCenterX + bigStickHalfWidth,
            mCenterY - centerOffset
        )

        val smallStickWidth = width / 40.0f
        val smallStickHalfWidth = smallStickWidth / 2.0f

        mSmallStartPoint = PointF(mCenterX - smallStickHalfWidth, smallStickWidth / 2.0f)
        mSmallStickTopRect = RectF(
            mCenterX - smallStickHalfWidth,
            0f,
            mCenterX + smallStickHalfWidth,
            smallStickWidth
        )

        mSmallStickBottomRect = RectF(
            mCenterX - smallStickHalfWidth,
            mSecondHandLength - smallStickWidth,
            mCenterX + smallStickHalfWidth,
            mSecondHandLength
        )
    }

    override fun drawWatchFace(
        canvas: Canvas, secondsRotation: Float, minutes: Int, hoursRotation: Float, showSecondHand: Boolean,
        mHourPaint: Paint, mFontMinutePaint: Paint, mMinuteFontVerticalOffset: Float,
        mSecondPaint: Paint, mAmbient: Boolean
    ) {

        // Hour
        canvas.rotate(hoursRotation, mCenterX, mCenterY)
        drawHourHand(canvas, mHourPaint)
        canvas.rotate(-hoursRotation, mCenterX, mCenterY)

        // Minute
        if (minutes != 0) {
            drawMinuteText(canvas, minutes, mFontMinutePaint, hoursRotation)
        } else {
            drawZeroMinuteDot(canvas, mFontMinutePaint)
        }
	// Draw Zero Minute Dot
	private fun drawZeroMinuteDot(canvas: Canvas, paint: Paint) {
    		val dotRadius = 5f // Adjust the dot size as needed
    		canvas.drawCircle(mCenterX, mCenterY - dotRadius / 2, dotRadius, paint)
	}

        // Second
        if (!mAmbient && showSecondHand) {
            canvas.rotate(secondsRotation, mCenterX, mCenterY)
            drawSecondHand(canvas, mSecondPaint)
        }
    }

    private fun drawHourHand(canvas: Canvas, paint: Paint) {
        val drawingPath = Path()
        drawingPath.arcTo(
            mBigStickTopRect.left,
            mBigStickTopRect.top,
            mBigStickTopRect.right,
            mBigStickTopRect.bottom,
            180f,
            180f,
            false
        )
        drawingPath.arcTo(
            mBigStickBottomRect.left,
            mBigStickBottomRect.top,
            mBigStickBottomRect.right,
            mBigStickBottomRect.bottom,
            0f,
            180f,
            false
        )
        drawingPath.lineTo(mBigStartPoint.x, mBigStartPoint.y)
        canvas.drawPath(drawingPath, paint)
    }

    private fun drawMinuteText(canvas: Canvas, minutes: Int, paint: Paint, hoursRotation: Float) {
        val minuteCenter = getPointRotatedByDegrees(
            mCenterX,
            mCenterY,
            mCenterX,
            mCenterY * 0.8f,
            (hoursRotation + 180).toDouble()
        )
        canvas.drawText(
            minutes.toString(),
            minuteCenter.x.toFloat(),
            minuteCenter.y + mMinuteFontVerticalOffset,
            paint
        )
    }

    private fun drawZeroMinuteDot(canvas: Canvas, paint: Paint) {
        val dotRadius = nullMinuteDotSize
        canvas.drawCircle(mCenterX, mCenterY - dotRadius / 2, dotRadius, paint)
    }

    private fun drawSecondHand(canvas: Canvas, paint: Paint) {
        val drawingPath = Path()
        drawingPath.arcTo(
            mSmallStickTopRect.left,
            mSmallStickTopRect.top,
            mSmallStickTopRect.right,
            mSmallStickTopRect.bottom,
            180f,
            180f,
            false
        )
        drawingPath.arcTo(
            mSmallStickBottomRect.left,
            mSmallStickBottomRect.top,
            mSmallStickBottomRect.right,
            mSmallStickBottomRect.bottom,
            0f,
            180f,
            false
        )
        drawingPath.lineTo(mSmallStartPoint.x, mSmallStartPoint.y)
        canvas.drawPath(drawingPath, paint)
    }
}