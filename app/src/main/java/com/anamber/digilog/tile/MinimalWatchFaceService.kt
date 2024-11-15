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
import android.view.SurfaceHolder
import com.anamber.digilog.presentation.MinimalConfigData
import com.anamber.digilog.tile.MathHelper.getComplicationRect

class MinimalWatchFaceService : BaseWatchFaceService() {

    override val sharedPrefFileKey: String
        get() = getString(MinimalConfigData.PREFERENCE_FILE_KEY)

    override fun onCreateEngine(): BaseWatchFaceService.Engine {
        return CustomEngine()
    }

    internal inner class CustomEngine : BaseWatchFaceService.Engine() {

        private val mWatchFaceDrawer = MinimalWatchFaceDrawer()
        private var mCompilationDistanceFromBorder: Float = 0.toFloat()

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)

            mCompilationDistanceFromBorder = mCenterX * 0.55f
            mWatchFaceDrawer.calculateSizes(width, height, mCenterX, mCenterY)
        }

        override fun drawWatchFace(canvas: Canvas?, secondsRotation: Float, minutes: Int, hoursRotation: Float, showSecondHand: Boolean) {
            /*
             * Save the canvas state before we can begin to rotate it.
             */
            canvas!!.save()


            mWatchFaceDrawer.drawWatchFace(
                canvas, secondsRotation, minutes, hoursRotation, showSecondHand,
                mHourPaint, mFontMinutePaint, mMinuteFontVerticalOffset.toFloat(), mSecondPaint, mAmbient
            )


            //################################################################################
            //# Complications position
            //################################################################################

            mComplicationDrawableSparseArray.get(BaseWatchFaceService.RIGHT_COMPLICATION_ID).bounds =
                getComplicationRect(mCenterX, mCenterY, mCompilationDistanceFromBorder, mComplicationSize, hoursRotation + 105)

            mComplicationDrawableSparseArray.get(BaseWatchFaceService.LEFT_COMPLICATION_ID).bounds =
                getComplicationRect(mCenterX, mCenterY, mCompilationDistanceFromBorder, mComplicationSize, hoursRotation + 255)

            /* Restore the canvas' original orientation. */
            canvas.restore()
        }
    }
}