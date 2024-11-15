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

import com.anamber.digilog.tile.MathHelper.drawPolygon
import com.anamber.digilog.tile.MathHelper.getPointRotatedByDegrees


class PentagonWatchFaceDrawer : BaseWatchFaceDrawer() {

    private var mBigPentagonSize: Float = 0.toFloat()
    private val pentagonPartDegree = 72 //360 / 5
    private val bigPentagonPoints: Array<Point?> by lazy { arrayOfNulls<Point>(5) }
    private val smallPentagonPoints: Array<Point?> by lazy { arrayOfNulls<Point>(5) }

    override fun calculateSizes(width: Int, height: Int, mCenterX: Float, mCenterY: Float) {
        super.calculateSizes(width, height, mCenterX, mCenterY)

        mBigPentagonSize = width / 2f

        val bigPentagonCenterY = mBigPentagonSize / 2.0f
        bigPentagonPoints[0] = Point(mCenterX.toInt(), 0)
        for (i in 1 until bigPentagonPoints.size) {
            bigPentagonPoints[i] = getPointRotatedByDegrees(mCenterX, bigPentagonCenterY, mCenterX, 0f, (pentagonPartDegree * i).toDouble())
        }

        val smallPentagonCenterY = mSecondHandLength / 2.0f
        smallPentagonPoints[0] = Point(mCenterX.toInt(), 0)
        for (i in 1 until smallPentagonPoints.size) {
            smallPentagonPoints[i] = getPointRotatedByDegrees(mCenterX, smallPentagonCenterY, mCenterX, 0f, (pentagonPartDegree * i).toDouble())
        }
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
        drawPolygon(bigPentagonPoints, drawingPath)

        canvas.drawPath(drawingPath, mHourPaint)

        //reset canvas after drawing hour hand
        canvas.rotate(-hoursRotation, mCenterX, mCenterY)


        //################################################################################
        //# minute
        //################################################################################

        if (minutes != 0) {
            val minuteCenter = getPointRotatedByDegrees(mCenterX, mCenterY, mCenterX, mCenterY * 0.5f, hoursRotation.toDouble())
            canvas.drawText(Integer.toString(minutes), minuteCenter.x.toFloat(), minuteCenter.y + mMinuteFontVerticalOffset, mFontMinutePaint)
        } else {
        // Draw a centered dot for zero minutes
        val dotRadius = 5f // Adjust the dot size as needed
        canvas.drawCircle(mCenterX, mCenterY - dotRadius / 2, dotRadius, mFontMinutePaint)
    	}

        //################################################################################
        //# second
        //################################################################################

        /*
         * Ensure the "seconds" hand is drawn only when we are in interactive mode.
         * Otherwise, we only update the watch face once a minute.
         */
        if (!mAmbient && showSecondHand) {
            canvas.rotate(secondsRotation, mCenterX, mCenterY)

            drawingPath.reset()
            drawPolygon(smallPentagonPoints, drawingPath)

            canvas.drawPath(drawingPath, mSecondPaint)
        }
    }

}