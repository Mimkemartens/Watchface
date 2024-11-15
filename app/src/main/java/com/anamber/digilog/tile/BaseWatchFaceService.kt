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

import android.content.*
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.wear.complications.ComplicationData
import androidx.wear.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.CanvasWatchFaceService
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceStyle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import com.anamber.digilog.watchfaces.R
import com.anamber.digilog.watchfaces.config.ConfigRecyclerViewAdapter
import com.anamber.digilog.watchfaces.config.Constants
import java.lang.ref.WeakReference
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

/**
 * Demonstrates two simple complications in a watch face.
 */
abstract class BaseWatchFaceService : CanvasWatchFaceService() {

    //#########################################################################################
    //# Watch Face Engine
    //#########################################################################################


    internal abstract val sharedPrefFileKey: String

    private class EngineHandler internal constructor(reference: Engine) : Handler() {
        private val mWeakReference: WeakReference<Engine> = WeakReference(reference)

        override fun handleMessage(msg: Message) {

            val engine = mWeakReference.get()
            if (engine != null && msg.what == MSG_UPDATE_TIME) {
                engine.handleUpdateTimeMessage()
            }
        }
    }


    abstract inner class Engine : CanvasWatchFaceService.Engine() {

        val MSG_UPDATE_TIME = 0

        val mCalendar: Calendar by lazy { Calendar.getInstance() }
        private var mRegisteredTimeZoneReceiver = false
        private var mMuteMode: Boolean = false

        var mWidth: Int = 0
        var mHeight: Int = 0

        var mCenterX: Float = 0.toFloat()
        var mCenterY: Float = 0.toFloat()

        var mComplicationSize: Float = 0.toFloat()
        var mSecondHandLength: Float = 0.toFloat()

        //saved color values
        var mBackgroundColor: Int = 0
        var mTickColor: Int = 0
        var mHourColor: Int = 0
        var mMinuteColor: Int = 0
        var mSecondColor: Int = 0
        var mHourFill = true
        var mSecondFill = true

        //colors
        private val mBackgroundPaint: Paint by lazy { Paint() }
        val mHourPaint: Paint by lazy { Paint() }
        val mGradientPaint: Paint by lazy { Paint() }
        val mFontMinutePaint: Paint by lazy { Paint() }
        val mSecondPaint: Paint by lazy { Paint() }

        //tick colors
        val mTickPaint: Paint by lazy { Paint() }
        val mThickTickPaint: Paint by lazy { Paint() }
        val mFontTickPaint: Paint by lazy { Paint() }

        //ticks
        private var mTickDrawer: TickDrawer? = null
        private var mTickType: TickDrawer.TickType = TickDrawer.TickType.STANDARD
        private var mTickFontFamilyIndex: Int = 0
        private var mTickNumbers: TickDrawer.TickNumbers = TickDrawer.TickNumbers.NONE

        //minute
        var mMinuteFontVerticalOffset: Int = 0
        var mMinuteFontFamilyIndex: Int = 0

        //second
        var mShowSecondHand: Boolean = false

        /* Maps active complication ids to the data for that complication. Note: Data will only be
         * present if the user has chosen a provider via the settings activity for the watch face.
         */
        private var mActiveComplicationDataSparseArray: SparseArray<ComplicationData>? = null

        /* Maps complication ids to corresponding ComplicationDrawable that renders the
         * the complication data on the watch face.
         */
        val mComplicationDrawableSparseArray: SparseArray<ComplicationDrawable> by lazy { SparseArray<ComplicationDrawable>(complicationIds.size) }

        var mAmbient: Boolean = false
        private var mLowBitAmbient: Boolean = false
        private var mBurnInProtection: Boolean = false

        // Used to pull user's preferences for background color, highlight color, and visual
        // indicating there are unread notifications.
        private val mSharedPref: SharedPreferences by lazy { applicationContext.getSharedPreferences(sharedPrefFileKey, Context.MODE_PRIVATE) }

        // User's preference for if they want visual shown to indicate unread notifications.
        private var mUnreadNotificationsPreference: Boolean = false
        private var mNumberOfUnreadNotifications = 0

        private val mTimeZoneReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                mCalendar.timeZone = TimeZone.getDefault()
                invalidate()
            }
        }

        // Handler to update the time once a second in interactive mode.
        private val mUpdateTimeHandler = EngineHandler(this)

        /**
         * Handle updating the time periodically in interactive mode.
         */
        fun handleUpdateTimeMessage() {
            invalidate()
            if (shouldTimerBeRunning()) {
                val timeMs = System.currentTimeMillis()
                val delayMs = INTERACTIVE_UPDATE_RATE_MS - timeMs % INTERACTIVE_UPDATE_RATE_MS
                mUpdateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs)
            }
        }

        override fun onCreate(holder: SurfaceHolder) {
            Log.d(TAG, "onCreate")

            super.onCreate(holder)

            setWatchFaceStyle(
                WatchFaceStyle.Builder(this@BaseWatchFaceService)
                    .setAcceptsTapEvents(true)
                    .setHideNotificationIndicator(true)
                    .build()
            )

            mTickDrawer = TickDrawer()
            loadSavedPreferences()
            initializeComplicationsAndBackground()
            initializeWatchFace()
        }

        // Pulls all user's preferences for watch face appearance.
        private fun loadSavedPreferences() {
            mBackgroundColor = mSharedPref.getInt(applicationContext.getString(R.string.saved_background_color), Color.BLACK)
            mTickColor = mSharedPref.getInt(applicationContext.getString(R.string.saved_tick_color), Color.WHITE)
            mHourColor = mSharedPref.getInt(applicationContext.getString(R.string.saved_hour_color), Color.parseColor("#DCDCDC"))
            mHourFill = mSharedPref.getBoolean(applicationContext.getString(R.string.saved_hour_fill), true)
            mMinuteColor = mSharedPref.getInt(applicationContext.getString(R.string.saved_minute_color), Color.parseColor("#969696"))
            mSecondColor = mSharedPref.getInt(applicationContext.getString(R.string.saved_second_color), Color.parseColor("#ECECEC"))
            mSecondFill = mSharedPref.getBoolean(applicationContext.getString(R.string.saved_second_fill), true)

            mMinuteFontFamilyIndex = mSharedPref.getInt(applicationContext.getString(R.string.saved_minute_font), Constants.TICK_FAMILY_INDEX_DEFAULT)
            mShowSecondHand = mSharedPref.getBoolean(applicationContext.getString(R.string.saved_second_show), true)

            mTickType = TickDrawer.TickType.valueOf(mSharedPref.getString(applicationContext.getString(R.string.saved_tick_type), Constants.TICK_TYPE_DEFAULT.name))
            mTickFontFamilyIndex = mSharedPref.getInt(applicationContext.getString(R.string.saved_tick_font), Constants.TICK_FAMILY_INDEX_DEFAULT)
            mTickNumbers = TickDrawer.TickNumbers.valueOf(mSharedPref.getString(applicationContext.getString(R.string.saved_tick_numbers), Constants.TICK_NUMBERS_DEFAULT.name))

            mUnreadNotificationsPreference = mSharedPref.getBoolean(applicationContext.getString(R.string.saved_unread_notifications_pref), true)
        }

        private fun initializeComplicationsAndBackground() {
            Log.d(TAG, "initializeComplications()")

            // Initialize background color (in case background complication is inactive).
            mBackgroundPaint.color = mBackgroundColor

            mActiveComplicationDataSparseArray = SparseArray(complicationIds.size)

            // Creates a ComplicationDrawable for each location where the user can render a
            // complication on the watch face. In this watch face, we create one for left, right,
            // and background, but you could add many more.
            val leftComplicationDrawable = ComplicationDrawable(applicationContext)
            val rightComplicationDrawable = ComplicationDrawable(applicationContext)
            val backgroundComplicationDrawable = ComplicationDrawable(applicationContext)

            // Adds new complications to a SparseArray to simplify setting styles and ambient
            // properties for all complications, i.e., iterate over them all.

            mComplicationDrawableSparseArray.put(LEFT_COMPLICATION_ID, leftComplicationDrawable)
            mComplicationDrawableSparseArray.put(RIGHT_COMPLICATION_ID, rightComplicationDrawable)
            mComplicationDrawableSparseArray.put(BACKGROUND_COMPLICATION_ID, backgroundComplicationDrawable)

            setComplicationsActiveAndAmbientColors(mSecondColor)
            setActiveComplications(*complicationIds)
        }

        open fun initializeWatchFace() {

            //hour
            mHourPaint.color = mHourColor
            mHourPaint.strokeWidth = 5f
            mHourPaint.isAntiAlias = true
            mHourPaint.strokeCap = Paint.Cap.SQUARE
            mHourPaint.style = if (mHourFill) Paint.Style.FILL else Paint.Style.STROKE

            //hour gradient (only used for piece of cake)
            mGradientPaint.strokeWidth = 3f
            mGradientPaint.isAntiAlias = true
            mGradientPaint.style = Paint.Style.FILL

            //minute number
            mFontMinutePaint.color = mMinuteColor
            mFontMinutePaint.strokeWidth = 3f
            mFontMinutePaint.isAntiAlias = true
            mFontMinutePaint.strokeCap = Paint.Cap.ROUND
            mFontMinutePaint.textAlign = Paint.Align.CENTER
            mFontMinutePaint.textSize = FontHelper.getFontSize(mHeight * Constants.RATIO_80_TO_HEIGHT, mMinuteFontFamilyIndex)
            mMinuteFontVerticalOffset = FontHelper.setFontPaint(mFontMinutePaint, mMinuteFontFamilyIndex, assets)

            //second
            mSecondPaint.color = mSecondColor
            mSecondPaint.strokeWidth = 3f
            mSecondPaint.isAntiAlias = true
            mSecondPaint.strokeCap = Paint.Cap.ROUND
            mSecondPaint.style = if (mSecondFill) Paint.Style.FILL else Paint.Style.STROKE

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
            mFontTickPaint.textSize = FontHelper.getFontSize(mHeight * Constants.RATIO_26_TO_HEIGHT, mTickFontFamilyIndex)
            mTickDrawer?.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, assets))
        }


        /* Sets active/ambient mode colors for all complications.
         *
         * Note: With the rest of the watch face, we update the paint colors based on
         * ambient/active mode callbacks, but because the ComplicationDrawable handles
         * the active/ambient colors, we only set the colors twice. Once at initialization and
         * again if the user changes the highlight color via BaseConfigActivity.
         */
        private fun setComplicationsActiveAndAmbientColors(primaryComplicationColor: Int) {
            var complicationId: Int
            var complicationDrawable: ComplicationDrawable

            for (i in complicationIds.indices) {
                complicationId = complicationIds[i]
                complicationDrawable = mComplicationDrawableSparseArray.get(complicationId)

                if (complicationId == BACKGROUND_COMPLICATION_ID) {
                    // It helps for the background color to be black in case the image used for the
                    // watch face's background takes some time to load.
                    complicationDrawable.setBackgroundColorActive(Color.BLACK)
                } else {
                    // Active mode colors.
                    complicationDrawable.setBorderColorActive(primaryComplicationColor)
                    complicationDrawable.setRangedValuePrimaryColorActive(primaryComplicationColor)
                    complicationDrawable.setTextColorActive(primaryComplicationColor)
                    complicationDrawable.setIconColorActive(primaryComplicationColor)
                    complicationDrawable.setTitleColorActive(primaryComplicationColor)

                    // Ambient mode colors.
                    complicationDrawable.setBorderColorAmbient(Color.WHITE)
                    complicationDrawable.setRangedValuePrimaryColorAmbient(Color.WHITE)
                    complicationDrawable.setTextColorAmbient(Color.WHITE)
                    complicationDrawable.setIconColorAmbient(Color.WHITE)
                    complicationDrawable.setTitleColorAmbient(Color.WHITE)
                }
            }
        }

        override fun onDestroy() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            super.onDestroy()
        }

        override fun onPropertiesChanged(properties: Bundle?) {
            super.onPropertiesChanged(properties)
            Log.d(TAG, "onPropertiesChanged: low-bit ambient = $mLowBitAmbient")

            mLowBitAmbient = properties!!.getBoolean(WatchFaceService.PROPERTY_LOW_BIT_AMBIENT, false)
            mBurnInProtection = properties.getBoolean(WatchFaceService.PROPERTY_BURN_IN_PROTECTION, false)

            // Updates complications to properly render in ambient mode based on the
            // screen's capabilities.
            var complicationDrawable: ComplicationDrawable

            for (i in complicationIds.indices) {
                complicationDrawable = mComplicationDrawableSparseArray.get(complicationIds[i])

                complicationDrawable.setLowBitAmbient(mLowBitAmbient)
                complicationDrawable.setBurnInProtection(mBurnInProtection)
            }
        }

        /*
         * Called when there is updated data for a complication id.
         */
        override fun onComplicationDataUpdate(
            complicationId: Int, complicationData: ComplicationData?
        ) {
            Log.d(TAG, "onComplicationDataUpdate() id: $complicationId")

            // Adds/updates active complication data in the array.
            mActiveComplicationDataSparseArray!!.put(complicationId, complicationData)

            // Updates correct ComplicationDrawable with updated data.
            val complicationDrawable = mComplicationDrawableSparseArray.get(complicationId)
            complicationDrawable.setComplicationData(complicationData)

            invalidate()
        }

        override fun onTapCommand(tapType: Int, x: Int, y: Int, eventTime: Long) {
            Log.d(TAG, "OnTapCommand()")
            when (tapType) {
                WatchFaceService.TAP_TYPE_TAP ->

                    // If your background complication is the first item in your array, you need
                    // to walk backward through the array to make sure the tap isn't for a
                    // complication above the background complication.
                    for (i in complicationIds.indices.reversed()) {
                        val complicationId = complicationIds[i]
                        val complicationDrawable = mComplicationDrawableSparseArray.get(complicationId)

                        val successfulTap = complicationDrawable.onTap(x, y)

                        if (successfulTap) {
                            return
                        }
                    }
            }
        }

        override fun onTimeTick() {
            super.onTimeTick()
            invalidate()
        }

        override fun onAmbientModeChanged(inAmbientMode: Boolean) {
            super.onAmbientModeChanged(inAmbientMode)
            Log.d(TAG, "onAmbientModeChanged: $inAmbientMode")

            mAmbient = inAmbientMode

            updateWatchPaintStyles()

            // Update drawable complications' ambient state.
            // Note: ComplicationDrawable handles switching between active/ambient colors, we just
            // have to inform it to enter ambient mode.
            var complicationDrawable: ComplicationDrawable

            for (i in complicationIds.indices) {
                complicationDrawable = mComplicationDrawableSparseArray.get(complicationIds[i])
                complicationDrawable.setInAmbientMode(mAmbient)
            }

            // Check and trigger whether or not timer should be running (only in active mode).
            updateTimer()
        }

        private fun updateWatchPaintStyles() {
            //update fonts
            mMinuteFontVerticalOffset = FontHelper.setFontPaint(mFontMinutePaint, mMinuteFontFamilyIndex, assets)
            mTickDrawer!!.setTickFontVerticalOffset(FontHelper.updateFontPaint(mFontTickPaint, mTickType, mTickFontFamilyIndex, assets))

            //update hour and second hand
            mHourPaint.style = if (mHourFill) Paint.Style.FILL else Paint.Style.STROKE
            mSecondPaint.style = if (mSecondFill) Paint.Style.FILL else Paint.Style.STROKE

            //update colors
            if (mAmbient) {
                mBackgroundPaint.color = Color.BLACK
                mHourPaint.color = Color.parseColor("#DCDCDC")
                mGradientPaint.color = Color.WHITE
                mFontMinutePaint.color = Color.parseColor("#969696")
                mSecondPaint.color = Color.parseColor("#ECECEC")
                mTickPaint.color = Color.WHITE
                mThickTickPaint.color = Color.WHITE
                mFontTickPaint.color = Color.WHITE

                mHourPaint.isAntiAlias = false
                mGradientPaint.isAntiAlias = false
                mFontMinutePaint.isAntiAlias = false
                mSecondPaint.isAntiAlias = false
                mTickPaint.isAntiAlias = false
                mThickTickPaint.isAntiAlias = false
                mFontTickPaint.isAntiAlias = false

                mHourPaint.style = Paint.Style.STROKE
            } else {
                mBackgroundPaint.color = mBackgroundColor
                mHourPaint.color = mHourColor
                mGradientPaint.color = mHourColor    //TODO
                mFontMinutePaint.color = mMinuteColor
                mSecondPaint.color = mSecondColor
                mTickPaint.color = mTickColor
                mThickTickPaint.color = mTickColor
                mFontTickPaint.color = mTickColor

                mHourPaint.isAntiAlias = true
                mGradientPaint.isAntiAlias = true
                mFontMinutePaint.isAntiAlias = true
                mSecondPaint.isAntiAlias = true
                mTickPaint.isAntiAlias = true
                mThickTickPaint.isAntiAlias = true
                mFontTickPaint.isAntiAlias = true
            }
        }

        override fun onInterruptionFilterChanged(interruptionFilter: Int) {
            super.onInterruptionFilterChanged(interruptionFilter)
            val inMuteMode = interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE

            /* Dim display in mute mode. */
            if (mMuteMode != inMuteMode) {
                mMuteMode = inMuteMode
                mHourPaint.alpha = if (inMuteMode) 100 else 255
                mFontMinutePaint.alpha = if (inMuteMode) 100 else 255
                mSecondPaint.alpha = if (inMuteMode) 80 else 255
                invalidate()
            }
        }

//   ++++++++++++++++++++              ++++++++++++++++++++++++++               +++++++++++++++++++

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)

            mWidth = width
            mHeight = height

            /*
             * Find the coordinates of the center point on the screen, and ignore the window
             * insets, so that, on round watches with a "chin", the watch face is centered on the
             * entire screen, not just the usable portion.
             */
            mCenterX = width / 2f
            mCenterY = height / 2f

            /*
             * We suggest using at least 1/4 of the screen width for circular (or squared)
             * complications and 2/3 of the screen width for wide rectangular complications for
             * better readability.
             */
            mComplicationSize = width / 4.0f

            /*
             * Calculate lengths of different hands based on watch screen size.
             */
            mSecondHandLength = mWidth / 8.0f

            mComplicationDrawableSparseArray.get(BACKGROUND_COMPLICATION_ID).bounds = Rect(0, 0, width, height)

            mFontTickPaint.textSize = FontHelper.getFontSize(mHeight * Constants.RATIO_26_TO_HEIGHT, mTickFontFamilyIndex)
            mFontMinutePaint.textSize = FontHelper.getFontSize(mHeight * Constants.RATIO_80_TO_HEIGHT, mMinuteFontFamilyIndex)

            mTickDrawer!!.updateSizes(width.toFloat(), height.toFloat(), mCenterX, mCenterY)
        }

        override fun onDraw(canvas: Canvas, bounds: Rect) {
            val now = System.currentTimeMillis()
            mCalendar.timeInMillis = now

            /*
             * These calculations reflect the rotation in degrees per unit of time, e.g.,
             * 360 / 60 = 6 and 360 / 12 = 30.
             */
            val seconds = mCalendar.get(Calendar.SECOND).toFloat() //+ mCalendar.get(Calendar.MILLISECOND) / 1000f;
            val secondsRotation = seconds * 6f
            val minutes = mCalendar.get(Calendar.MINUTE)
            val hours = mCalendar.get(Calendar.HOUR)
            val hoursRotation = (hours * 30).toFloat()

            drawBackground(canvas)
            drawComplications(canvas, now)
            drawUnreadNotificationIcon(canvas)
            drawTicks(canvas, seconds.toInt(), hours, mTickNumbers, mShowSecondHand && !mAmbient)
            drawWatchFace(canvas, secondsRotation, minutes, hoursRotation, mShowSecondHand)
        }

        private fun drawUnreadNotificationIcon(canvas: Canvas?) {

            if (mUnreadNotificationsPreference && mNumberOfUnreadNotifications > 0) {

                val width = canvas!!.width
                val height = canvas.height

                canvas.drawCircle((width / 2).toFloat(), (height - 40).toFloat(), 10f, mTickPaint)

                /*
                 * Ensure center highlight circle is only drawn in interactive mode. This ensures
                 * we don't burn the screen with a solid circle in ambient mode.
                 */
                if (!mAmbient) {
                    canvas.drawCircle((width / 2).toFloat(), (height - 40).toFloat(), 4f, mSecondPaint)
                }
            }
        }

        private fun drawBackground(canvas: Canvas?) {
            if (mAmbient && (mLowBitAmbient || mBurnInProtection)) {
                canvas!!.drawColor(Color.BLACK)
            } else {
                canvas!!.drawColor(mBackgroundColor)
            }
        }

        private fun drawComplications(canvas: Canvas?, currentTimeMillis: Long) {
            var complicationId: Int
            var complicationDrawable: ComplicationDrawable

            for (i in complicationIds.indices) {
                complicationId = complicationIds[i]
                complicationDrawable = mComplicationDrawableSparseArray.get(complicationId)

                complicationDrawable.draw(canvas, currentTimeMillis)
            }
        }

        /**
         * Draw ticks. Usually you will want to bake this directly into the photo, but in
         * cases where you want to allow users to select their own photos, this dynamically
         * creates them on top of the photo.
         */
        private fun drawTicks(canvas: Canvas, second: Int, hour: Int, tickNumbers: TickDrawer.TickNumbers, showSecondHand: Boolean) {
            when (mTickType) {
                TickDrawer.TickType.ROMAN -> mTickDrawer!!.drawRomanTicks(canvas, hour, mTickPaint, mThickTickPaint, mFontTickPaint)

                TickDrawer.TickType.DETAIL -> mTickDrawer!!.drawDetailTicks(canvas, hour, tickNumbers, mTickPaint, mFontTickPaint)

                TickDrawer.TickType.MINIMAL -> mTickDrawer!!.drawMinimalTicks(canvas, second, hour, tickNumbers, showSecondHand, mTickPaint, mFontTickPaint)

                TickDrawer.TickType.STANDARD -> mTickDrawer!!.drawStandardTicks(canvas, second, hour, tickNumbers, showSecondHand, mTickPaint, mFontTickPaint)
            }
        }

        internal abstract fun drawWatchFace(canvas: Canvas?, secondsRotation: Float, minutes: Int, hoursRotation: Float, showSecondHand: Boolean)

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)

            if (visible) {

                // Preferences might have changed since last time watch face was visible.
                loadSavedPreferences()

                // With the rest of the watch face, we update the paint colors based on
                // ambient/active mode callbacks, but because the ComplicationDrawable handles
                // the active/ambient colors, we only need to update the complications' colors when
                // the user actually makes a change to the highlight color, not when the watch goes
                // in and out of ambient mode.
                setComplicationsActiveAndAmbientColors(mSecondColor)
                updateWatchPaintStyles()

                registerReceiver()
                // Update time zone in case it changed while we weren't visible.
                mCalendar.timeZone = TimeZone.getDefault()
                invalidate()
            } else {
                unregisterReceiver()
            }

            /* Check and trigger whether or not timer should be running (only in active mode). */
            updateTimer()
        }

        override fun onUnreadCountChanged(count: Int) {
            Log.d(TAG, "onUnreadCountChanged(): $count")

            if (mUnreadNotificationsPreference) {

                if (mNumberOfUnreadNotifications != count) {
                    mNumberOfUnreadNotifications = count
                    invalidate()
                }
            }
        }

        private fun registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return
            }
            mRegisteredTimeZoneReceiver = true
            val filter = IntentFilter(Intent.ACTION_TIMEZONE_CHANGED)
            this@BaseWatchFaceService.registerReceiver(mTimeZoneReceiver, filter)
        }

        private fun unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return
            }
            mRegisteredTimeZoneReceiver = false
            this@BaseWatchFaceService.unregisterReceiver(mTimeZoneReceiver)
        }

        /**
         * Starts/stops the [.mUpdateTimeHandler] timer based on the state of the watch face.
         */
        private fun updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME)
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME)
            }
        }

        /**
         * Returns whether the [.mUpdateTimeHandler] timer should be running. The timer should
         * only run in active mode.
         */
        private fun shouldTimerBeRunning(): Boolean {
            return isVisible && !mAmbient
        }
    }

    companion object {
        private val TAG = "BaseWatchFaceService"

        const val MSG_UPDATE_TIME = 0

        //#########################################################################################
        //# Complications
        //#########################################################################################


        // Unique IDs for each complication. The settings activity that supports allowing users
        // to select their complication data provider requires numbers to be >= 0.
        private val BACKGROUND_COMPLICATION_ID = 0

        internal val LEFT_COMPLICATION_ID = 100
        internal val RIGHT_COMPLICATION_ID = 101

        // Background, Left and right complication IDs as array for Complication API.
        // Used by {@link ConfigRecyclerViewAdapter} to retrieve all complication ids.
        val complicationIds = intArrayOf(BACKGROUND_COMPLICATION_ID, LEFT_COMPLICATION_ID, RIGHT_COMPLICATION_ID)

        // Left and right dial supported types.
        private val COMPLICATION_SUPPORTED_TYPES = arrayOf(
            intArrayOf(ComplicationData.TYPE_LARGE_IMAGE),
            intArrayOf(ComplicationData.TYPE_RANGED_VALUE, ComplicationData.TYPE_ICON, ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_SMALL_IMAGE),
            intArrayOf(ComplicationData.TYPE_RANGED_VALUE, ComplicationData.TYPE_ICON, ComplicationData.TYPE_SHORT_TEXT, ComplicationData.TYPE_SMALL_IMAGE)
        )

        // Used by {@link ConfigRecyclerViewAdapter} to check if complication location
        // is supported in settings config activity.
        fun getComplicationId(
            complicationLocation: ConfigRecyclerViewAdapter.ComplicationLocation
        ): Int {
            // Add any other supported locations here.
            when (complicationLocation) {
                ConfigRecyclerViewAdapter.ComplicationLocation.BACKGROUND -> return BACKGROUND_COMPLICATION_ID
                ConfigRecyclerViewAdapter.ComplicationLocation.LEFT -> return LEFT_COMPLICATION_ID
                ConfigRecyclerViewAdapter.ComplicationLocation.RIGHT -> return RIGHT_COMPLICATION_ID
                else -> return -1
            }
        }

        // Used by {@link ConfigRecyclerViewAdapter} to see which complication types
        // are supported in the settings config activity.
        fun getSupportedComplicationTypes(
            complicationLocation: ConfigRecyclerViewAdapter.ComplicationLocation
        ): IntArray {
            // Add any other supported locations here.
            when (complicationLocation) {
                ConfigRecyclerViewAdapter.ComplicationLocation.BACKGROUND -> return COMPLICATION_SUPPORTED_TYPES[0]
                ConfigRecyclerViewAdapter.ComplicationLocation.LEFT -> return COMPLICATION_SUPPORTED_TYPES[1]
                ConfigRecyclerViewAdapter.ComplicationLocation.RIGHT -> return COMPLICATION_SUPPORTED_TYPES[2]
                else -> return intArrayOf()
            }
        }

        /*
     * Update rate in milliseconds for interactive mode. We update once a second to advance the
     * second hand.
     */
        private val INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1)
    }

}