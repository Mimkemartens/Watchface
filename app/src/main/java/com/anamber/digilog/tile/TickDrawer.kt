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
import android.graphics.Path
import com.example.tikki.watchfaces.config.Constants

class TickDrawer {
var width: Float = 0.toFloat()
    private set
private var halfWidth: Float = 0.toFloat()
var height: Float = 0.toFloat()
    private set
var centerX: Float = 0.toFloat()
    private set
var centerY: Float = 0.toFloat()
    private set
private var tickFontVerticalOffset: Int = 0

private var quarterHourTickLength = height * Constants.RATIO_20_TO_HEIGHT
private var hourTickLength = height * Constants.RATIO_15_TO_HEIGHT
private var minuteTickLength = height * Constants.RATIO_10_TO_HEIGHT
private var romanTickLength = height * Constants.RATIO_10_TO_HEIGHT
private var romanOffsetX = height * Constants.RATIO_100_TO_HEIGHT
private var romanOffsetY = height * Constants.RATIO_5_TO_HEIGHT
private var tickFontOffset = height * Constants.RATIO_35_TO_HEIGHT

private var hourInnerTickRadius: Float = 0.toFloat()
private var quarterInnerTickRadius: Float = 0.toFloat()
private var minuteInnerTickRadius: Float = 0.toFloat()
private var innerFontTickRadius: Float = 0.toFloat()
private var outerTickRadius: Float = 0.toFloat()

enum class TickType {
    STANDARD, DETAIL, MINIMAL, ROMAN
}

enum class TickNumbers {
    NONE, HOURLY, QUARTERLY
}

fun updateSizes(width: Float, height: Float, centerX: Float, centerY: Float) {
    this.width = width
    this.height = height
    this.centerX = centerX
    this.centerY = centerY

    quarterHourTickLength = height * Constants.RATIO_20_TO_HEIGHT
    hourTickLength = height * Constants.RATIO_15_TO_HEIGHT
    minuteTickLength = height * Constants.RATIO_10_TO_HEIGHT
    romanTickLength = height * Constants.RATIO_10_TO_HEIGHT
    romanOffsetX = height * Constants.RATIO_100_TO_HEIGHT
    romanOffsetY = height * Constants.RATIO_5_TO_HEIGHT
    tickFontOffset = height * Constants.RATIO_35_TO_HEIGHT

    halfWidth = width / 2f

    hourInnerTickRadius = halfWidth - hourTickLength
    quarterInnerTickRadius = halfWidth - quarterHourTickLength
    minuteInnerTickRadius = halfWidth - minuteTickLength
    innerFontTickRadius = halfWidth - tickFontOffset
    outerTickRadius = halfWidth
}

fun setTickFontVerticalOffset(tickFontVerticalOffset: Int) {
    this.tickFontVerticalOffset = tickFontVerticalOffset
}

fun drawStandardTicks(canvas: Canvas, second: Int, hour: Int, tickNumbers: TickNumbers, drawMovingSeconds: Boolean, tickPaint: Paint, fontTickPaint: Paint) {
    for (tickIndex in 0..11) {
        if (tickIndex == hour) {
            continue
        }

        val tickRot = (tickIndex.toDouble() * Math.PI * 2.0 / 12).toFloat()

        if (tickIndex % 3 == 0) {
            drawTick(canvas, tickRot, quarterInnerTickRadius, outerTickRadius, tickPaint)

            if (tickNumbers == TickNumbers.QUARTERLY) {
                drawTickNumber(canvas, tickRot, getTickNumberText(tickIndex), fontTickPaint)
            }
        } else {
            drawTick(canvas, tickRot, hourInnerTickRadius, outerTickRadius, tickPaint)
        }

        if (tickNumbers == TickNumbers.HOURLY) {
            drawTickNumber(canvas, tickRot, getTickNumberText(tickIndex), fontTickPaint)
        }
    }

    if (drawMovingSeconds) {
        drawMovingSecondTicks(canvas, second, tickPaint)
    }

}

fun drawDetailTicks(canvas: Canvas, hour: Int, tickNumbers: TickNumbers, tickPaint: Paint, fontTickPaint: Paint) {
    for (tickIndex in 0..59) {
        if (tickIndex / 5.0f == hour.toFloat()) {
            continue
        }

        val tickRot = (tickIndex.toDouble() * Math.PI * 2.0 / 60).toFloat()
        if (tickIndex % 15 == 0) {
            drawTick(canvas, tickRot, quarterInnerTickRadius, outerTickRadius, tickPaint)
        } else if (tickIndex % 5 == 0) {
            drawTick(canvas, tickRot, hourInnerTickRadius, outerTickRadius, tickPaint)
        } else {
            drawTick(canvas, tickRot, minuteInnerTickRadius, outerTickRadius, tickPaint)
        }

        if (tickNumbers == TickNumbers.HOURLY && tickIndex % 5 == 0 || tickNumbers == TickNumbers.QUARTERLY && tickIndex % 15 == 0) {

            drawTickNumber(canvas, tickRot, getTickNumberText(tickIndex / 5), fontTickPaint)
        }
    }
}

fun drawRomanTicks(canvas: Canvas, hour: Int, tickPaint: Paint, thickTickPaint: Paint, fontTickPaint: Paint) {
    val innerTickRadius = halfWidth - romanTickLength
    val p = Path()

    canvas.drawCircle(centerX, centerY, halfWidth, tickPaint)
    canvas.drawCircle(centerX, centerY, halfWidth - romanTickLength, tickPaint)

    for (tickIndex in 0..59) {
        if (tickIndex / 5.0f == hour.toFloat()) {
            continue
        }

        val tickRot = (tickIndex.toDouble() * Math.PI * 2.0 / 60).toFloat()

        if (tickIndex % 5 == 0) {
            val p1 = MathHelper.getPointRotatedByRadians(centerX, centerY, centerX - romanOffsetX, centerY - halfWidth + romanTickLength + romanOffsetY, tickRot.toDouble())
            val p2 = MathHelper.getPointRotatedByRadians(centerX, centerY, centerX + romanOffsetX, centerY - halfWidth + romanTickLength + romanOffsetY, tickRot.toDouble())

            p.reset()
            p.moveTo(p1.x.toFloat(), p1.y.toFloat())
            p.lineTo(p2.x.toFloat(), p2.y.toFloat())
            canvas.drawTextOnPath(getTickNumberText(tickIndex / 5), p, 0f, (tickFontVerticalOffset * 2).toFloat(), fontTickPaint)
        }

        drawTick(canvas, tickRot, innerTickRadius, outerTickRadius, if (tickIndex % 5 == 0) thickTickPaint else tickPaint)
    }
}

fun drawMinimalTicks(canvas: Canvas, second: Int, hour: Int, tickNumbers: TickNumbers, drawMovingSeconds: Boolean, tickPaint: Paint, fontTickPaint: Paint) {
    for (tickIndex in 0..3) {
        if (tickIndex * 3 == hour) {
            continue
        }

        val tickRot = (tickIndex.toDouble() * Math.PI * 2.0 / 4).toFloat()
        drawTick(canvas, tickRot, quarterInnerTickRadius, outerTickRadius, tickPaint)

        if (tickNumbers == TickNumbers.QUARTERLY) {
            drawTickNumber(canvas, tickRot, getTickNumberText(tickIndex * 3), fontTickPaint)
        }
    }

    if (drawMovingSeconds) {
        drawMovingSecondTicks(canvas, second, tickPaint)
    }
}

private fun drawMovingSecondTicks(canvas: Canvas, second: Int, tickPaint: Paint) {
    val hourInnerTickRadius = centerX - hourTickLength
    val minuteInnerTickRadius = centerX - minuteTickLength

    val tickArea = second / 5
    val areaStartMinute = tickArea * 5
    val areaEndMinute = areaStartMinute + 5

    val startSecond = areaStartMinute - 1
    val endSecond = areaEndMinute + 1

    for (tickIndex in startSecond..endSecond) {

        val tickRot = (tickIndex.toDouble() * Math.PI * 2.0 / 60).toFloat()

        if (tickIndex == areaStartMinute || tickIndex == areaEndMinute) {
            drawTick(canvas, tickRot, hourInnerTickRadius, outerTickRadius, tickPaint)
        } else {
            drawTick(canvas, tickRot, minuteInnerTickRadius, outerTickRadius, tickPaint)
        }
    }
}

private fun drawTickNumber(canvas: Canvas, tickRotation: Float, numberText: String, fontTickPaint: Paint) {
    val innerFontX = centerX + Math.sin(tickRotation.toDouble()).toFloat() * innerFontTickRadius
    val innerFontY = centerY + (-Math.cos(tickRotation.toDouble())).toFloat() * innerFontTickRadius
    canvas.drawText(numberText, innerFontX, innerFontY + tickFontVerticalOffset, fontTickPaint)
}

private fun getTickNumberText(num: Int): String {
    return if (num == 0) "12" else Integer.toString(num)
}

private fun drawTick(canvas: Canvas, tickRot: Float, innerTickRadius: Float, outerTickRadius: Float, tickPaint: Paint) {
    val innerX = Math.sin(tickRot.toDouble()).toFloat() * innerTickRadius
    val innerY = (-Math.cos(tickRot.toDouble())).toFloat() * innerTickRadius
    val outerX = Math.sin(tickRot.toDouble()).toFloat() * outerTickRadius
    val outerY = (-Math.cos(tickRot.toDouble())).toFloat() * outerTickRadius

    canvas.drawLine(
        centerX + innerX,
        centerY + innerY,
        centerX + outerX,
        centerY + outerY,
        tickPaint
    )
}

}