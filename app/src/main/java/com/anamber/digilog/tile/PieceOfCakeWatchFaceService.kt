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
import com.anamber.digilog.presentation.PieceOfCakeConfigData

class PieceOfCakeWatchFaceService : BaseWatchFaceService() {

    internal override val sharedPrefFileKey: String
        get() = getString(PieceOfCakeConfigData.PREFERENCE_FILE_KEY)

    override fun onCreateEngine(): BaseWatchFaceService.Engine {
        return CustomEngine()
    }

    internal inner class CustomEngine : BaseWatchFaceService.Engine() {

        private var mCompilationDistanceFromBorder: Float = 0.toFloat()
        private val mWatchFaceDrawer = PieceOfCakeWatchFaceDrawer()

        override fun initializeWatchFace(){
            super.initializeWatchFace()
            mHourPaint.strokeWidth = 7f
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)

            mCompilationDistanceFromBorder = mCenterX * 0.4f
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
                    MathHelper.getComplicationRect(mCenterX, mCenterY, mCompilationDistanceFromBorder, mComplicationSize, hoursRotation + 75)

            mComplicationDrawableSparseArray.get(BaseWatchFaceService.LEFT_COMPLICATION_ID).bounds =
                    MathHelper.getComplicationRect(mCenterX, mCenterY, mCompilationDistanceFromBorder, mComplicationSize, hoursRotation + 285)


            /* Restore the canvas' original orientation. */
            canvas.restore()
        }
    }
}