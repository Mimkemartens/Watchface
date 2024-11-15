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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.anamber.digilog.R
import com.anamber.digilog.watchfaces.config.*

class ClockFaceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()

    private val mTickPaint: Paint by lazy { Paint() }
    private val mThickTickPaint: Paint by lazy { Paint() }
    private val mFontTickPaint: Paint by lazy { Paint() }
    private val tickDrawer = TickDrawer()
    private var mTickType = Constants.TICK_TYPE_DEFAULT
    private var mTickNumbers: TickDrawer.TickNumbers = TickDrawer.TickNumbers.NONE
    private var mTickFontFamilyIndex = Constants.TICK_FAMILY_INDEX_DEFAULT

    private var mWatchFaceType: BaseWatchFaceDrawer.WatchFaceType = BaseWatchFaceDrawer.WatchFaceType.NONE
    private var mWatchFaceDrawer: BaseWatchFaceDrawer? = null
    private val mBackgroundPaint: Paint by lazy { Paint() }

    internal val mHourPaint: Paint by lazy { Paint() }
    internal val mFontMinutePaint: Paint by lazy { Paint() }
    internal val mSecondPaint: Paint by lazy { Paint() }

    //saved color values
    internal var mBackgroundColor: Int = 0
    internal var mTickColor: Int = 0
    internal var mHourColor: Int = 0
    internal var mMinuteColor: Int = 0
    internal var mSecondColor: Int = 0
    internal var mHourFill = true
    internal var mSecondFill = true

    //minute
    internal var mMinuteFontVerticalOffset: Int = 0
    internal var mMinuteFontFamilyIndex: Int = 0

    //second
    internal var mShowSecondHand = false

    internal var seconds = 0
    internal var secondsRotation = seconds * 6f
    internal var minutes = 28
    internal var hours = -1
    internal var hoursRotation = (hours * 30).toFloat()

    init {
        initTick()
    }

    internal fun initWatchFace() {

        //hour
        mHourPaint.color = mHourColor
        mHourPaint.strokeWidth = 3f
        mHourPaint.isAntiAlias = true
        mHourPaint.strokeCap = Paint.Cap.SQUARE
        mHourPaint.style = if (mHourFill) Paint.Style.FILL else Paint.Style.STROKE

        //minute number
        mFontMinutePaint.color = mMinuteColor
        mFontMinutePaint.strokeWidth = 3f
        mFontMinutePaint.isAntiAlias = true
        mFontMinutePaint.strokeCap = Paint.Cap.ROUND
        mFontMinutePaint.textAlign = Paint.Align.CENTER
        mMinuteFontVerticalOffset = FontHelper.setFontPaint(mFontMinutePaint, mMinuteFontFamilyIndex, context.assets)

        //second
        mSecondPaint.color = mSecondColor
        mSecondPaint.strokeWidth = 3f
        mSecondPaint.isAntiAlias = true
        mSecondPaint.strokeCap = Paint.Cap.ROUND
        mSecondPaint.style = if (mSecondFill) Paint.Style.FILL else Paint.Style.STROKE
    }

    internal fun initTick() {
        mTickColor = Color.parseColor("#FFFFFF")
        mBackgroundColor = Color.parseColor("#444444")

        //background
        mBackgroundPaint.color = mBackgroundColor
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.style = Paint.Style.FILL_AND_STROKE

        //tick
        mTickPaint.color = mTickColor
        mTickPaint.strokeWidth = 2f
        mTickPaint.isAntiAlias = true
        mTickPaint.style = Paint.Style.STROKE

        //only used for roman ticks face
        mThickTickPaint.color = mTickColor
        mThickTickPaint.strokeWidth = 6f
        mThickTickPaint.isAntiAlias = true
        mThickTickPaint.style = Paint.Style.STROKE

        //tick numbers (only in Detail and Standard)
        mFontTickPaint.color = mTickColor
        mFontTickPaint.strokeWidth = 2f
        mFontTickPaint.isAntiAlias = true
        mFontTickPaint.style = Paint.Style.FILL
        mFontTickPaint.textAlign = Paint.Align.CENTER
        tickDrawer.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, context.assets))
    }

    fun setTickColors(tickColor: Int) {
        mTickColor = tickColor

        mTickPaint.color = mTickColor
        mThickTickPaint.color = mTickColor
        mFontTickPaint.color = mTickColor

        this.invalidate()
    }

    fun setWatchFaceColors(hourColor: Int, minuteColor: Int, secondColor: Int) {
        mHourColor = hourColor
        mMinuteColor = minuteColor
        mSecondColor = secondColor

        mHourPaint.color = mHourColor
        mFontMinutePaint.color = mMinuteColor
        mSecondPaint.color = mSecondColor

        this.invalidate()
    }

    fun setWatchBackgroundColor(backgroundColor: Int) {
        mBackgroundColor = backgroundColor

        mBackgroundPaint.color = mBackgroundColor

        this.invalidate()
    }

    fun setWatchFaceType(watchFaceType: BaseWatchFaceDrawer.WatchFaceType) {
        if (watchFaceType != BaseWatchFaceDrawer.WatchFaceType.NONE) {
            initWatchFace()

            when (watchFaceType) {
                BaseWatchFaceDrawer.WatchFaceType.MINIMAL -> mWatchFaceDrawer = MinimalWatchFaceDrawer()
                BaseWatchFaceDrawer.WatchFaceType.PENTAGON -> mWatchFaceDrawer = PentagonWatchFaceDrawer()
                BaseWatchFaceDrawer.WatchFaceType.PIECE_OF_CAKE -> mWatchFaceDrawer = PieceOfCakeWatchFaceDrawer()
                BaseWatchFaceDrawer.WatchFaceType.ROUND_CENTER -> mWatchFaceDrawer = RoundCenterWatchFaceDrawer()
            }

            mWatchFaceDrawer!!.calculateSizes(mWidth.toInt(), mHeight.toInt(), mCenterX, mCenterY)
        }
        mWatchFaceType = watchFaceType

        this.invalidate()
    }

    fun setHourFill(fill: Boolean) {
        mHourFill = fill
        mHourPaint.style = if (mHourFill) Paint.Style.FILL else Paint.Style.STROKE

        this.invalidate()
    }

    fun setSecondFill(fill: Boolean) {
        mSecondFill = fill
        mSecondPaint.style = if (mSecondFill) Paint.Style.FILL else Paint.Style.STROKE

        this.invalidate()
    }

    fun setMinuteFontFamilyIndex(minuteFontFamilyIndex: Int) {
        mMinuteFontFamilyIndex = minuteFontFamilyIndex
        mMinuteFontVerticalOffset = FontHelper.setFontPaint(mFontMinutePaint, mMinuteFontFamilyIndex, context.assets)

        this.invalidate()
    }

    fun setTickType(tickType: TickDrawer.TickType) {
        mTickType = tickType
        tickDrawer.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, context.assets))

        this.invalidate()
    }

    fun setTickFontFamilyIndex(tickFontFamilyIndex: Int) {
        mTickFontFamilyIndex = tickFontFamilyIndex
        tickDrawer.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, context.assets))

        this.invalidate()
    }

    fun setTickNumbers(tickNumbers: TickDrawer.TickNumbers) {
        mTickNumbers = tickNumbers

        this.invalidate()
    }

    fun setShowSecondHand(showSecondHand: Boolean) {
        mShowSecondHand = showSecondHand

        this.invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = w * 0.95f
        mHeight = h * 0.95f
        mCenterX = w / 2f
        mCenterY = h / 2f


        if (mWatchFaceType != BaseWatchFaceDrawer.WatchFaceType.NONE && mWatchFaceDrawer != null) {
            //TODO: change
            mWidth = w.toFloat()
            mHeight = h.toFloat()
            seconds = 51
            secondsRotation = seconds * 6f
            hours = 2
            hoursRotation = (hours * 30).toFloat()

            mWatchFaceDrawer!!.calculateSizes(mWidth.toInt(), mHeight.toInt(), mCenterX, mCenterY)
        }

        mFontMinutePaint.textSize = FontHelper.getFontSize(mWidth * Constants.RATIO_80_TO_HEIGHT * 0.85f, mTickFontFamilyIndex)
        mMinuteFontVerticalOffset = FontHelper.setFontPaint(mFontMinutePaint, mMinuteFontFamilyIndex, context.assets)

        mFontTickPaint.textSize = FontHelper.getFontSize(mWidth * Constants.RATIO_26_TO_HEIGHT * 0.85f, mTickFontFamilyIndex)
        tickDrawer.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, context.assets))
        tickDrawer.updateSizes(mWidth, mHeight, mCenterX, mCenterY)
        tickDrawer.setTickFontVerticalOffset(MathHelper.calculateVerticalOffset(mFontTickPaint, 1.0f))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawTicks(canvas)

        if (mWatchFaceType != BaseWatchFaceDrawer.WatchFaceType.NONE && mWatchFaceDrawer != null) {
            drawWatchFace(canvas)
        }
    }

    private fun drawWatchFace(canvas: Canvas) {

        canvas.save()


        mWatchFaceDrawer!!.drawWatchFace(
            canvas, secondsRotation, minutes, hoursRotation, mShowSecondHand,
            mHourPaint, mFontMinutePaint, mMinuteFontVerticalOffset.toFloat(), mSecondPaint, false
        )

        canvas.restore()
    }

    private fun drawTicks(canvas: Canvas) {
        //background
        canvas.drawCircle(tickDrawer.centerX, tickDrawer.centerY, tickDrawer.height / 2f, mBackgroundPaint)

        canvas.drawCircle(tickDrawer.centerX, tickDrawer.centerY, tickDrawer.height / 2f, mTickPaint)

        when (mTickType) {
            TickDrawer.TickType.ROMAN -> tickDrawer.drawRomanTicks(canvas, hours, mTickPaint, mThickTickPaint, mFontTickPaint)

            TickDrawer.TickType.DETAIL -> tickDrawer.drawDetailTicks(canvas, hours, mTickNumbers, mTickPaint, mFontTickPaint)

            TickDrawer.TickType.MINIMAL -> tickDrawer.drawMinimalTicks(canvas, seconds, hours, mTickNumbers, mShowSecondHand, mTickPaint, mFontTickPaint)

            TickDrawer.TickType.STANDARD -> tickDrawer.drawStandardTicks(canvas, seconds, hours, mTickNumbers, mShowSecondHand, mTickPaint, mFontTickPaint)
        }
    }
}