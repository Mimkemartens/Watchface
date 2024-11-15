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

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect

object MathHelper {

    private val sqrtOfThree = 1.732050808f
    private val twoDevidedBysqrtOfThree = 1.154700538f

    /**
     * get height of text to calculate vertical offset
     */
    private val verticalOffsetRect = Rect()

    /**
     * Get new coordinates of pointToRotate after rotating around rotationCenter
     *
     *
     * used formula (x0,y0 is rotationCenter):
     * x=x0+(x−x0)cos(φ)−(y−y0)sin(φ)
     * y=y0+(x−x0)sin(φ)+(y−y0)cos(φ).
     *
     * @param rotationCenterX   x coordinate of rotation center
     * @param rotationCenterY   y coordinate of rotation center
     * @param pointToRotateX    x coordinate of start point for rotating rotationInDegrees around rotationCenter
     * @param pointToRotateY    y coordinate of start point for rotating rotationInDegrees around rotationCenter
     * @param rotationInDegrees angle to rotate in degrees
     */
    fun getPointRotatedByDegrees(rotationCenterX: Float, rotationCenterY: Float, pointToRotateX: Float, pointToRotateY: Float, rotationInDegrees: Double): Point {
        return getPointRotatedByRadians(rotationCenterX, rotationCenterY, pointToRotateX, pointToRotateY, Math.toRadians(rotationInDegrees))
    }

    fun getPointRotatedByRadians(rotationCenterX: Float, rotationCenterY: Float, pointToRotateX: Float, pointToRotateY: Float, rotationInRadians: Double): Point {
        val minuteX = (rotationCenterX + (pointToRotateX - rotationCenterX) * Math.cos(rotationInRadians) - (pointToRotateY - rotationCenterY) * Math.sin(rotationInRadians)).toInt()
        val minuteY = (rotationCenterY.toDouble() + (pointToRotateX - rotationCenterX) * Math.sin(rotationInRadians) + (pointToRotateY - rotationCenterY) * Math.cos(rotationInRadians)).toInt()

        return Point(minuteX, minuteY)
    }

    /**
     * Seitenlänge a=b=c eines gleichseitigen Dreiecks, gegeben ist die Höhe des Dreiecks.
     * h = √3 / 2 * a
     * a = h * 2 / √3
     *
     * @param height height of equilateral triangle
     * @return side length of equilateral triangle (side a=b=c)
     */
    fun getSideLengthOfEquilateralTriangle(height: Float): Float {
        return height * twoDevidedBysqrtOfThree
    }

    fun getBottomLeftPointOfEquilateralTriangle(topPointX: Float, topPointY: Float, triangleHeight: Float): Point {
        return Point((topPointX - getSideLengthOfEquilateralTriangle(triangleHeight) / 2).toInt(), (topPointY + triangleHeight).toInt())
    }

    fun getBottomRightPointOfEquilateralTriangle(topPointX: Float, topPointY: Float, triangleHeight: Float): Point {
        return Point((topPointX + getSideLengthOfEquilateralTriangle(triangleHeight) / 2).toInt(), (topPointY + triangleHeight).toInt())
    }

    fun getComplicationRect(centerX: Float, centerY: Float, distanceFromBorder: Float, size: Float, rotationInDegrees: Float): Rect {
        val halfSize = size / 2.0f
        val complicationRight = getPointRotatedByDegrees(centerX, centerY, centerX, distanceFromBorder, rotationInDegrees.toDouble())

        return Rect(
            (complicationRight.x - halfSize).toInt(), //left
            (complicationRight.y - halfSize).toInt(), //top
            (complicationRight.x + halfSize).toInt(), //right
            (complicationRight.y + halfSize).toInt()
        )   //bottom
    }

    fun drawPolygon(points: Array<Point?>, drawingPath: Path) {
        drawingPath.moveTo(points[0]?.x?.toFloat() ?: 0f, points[0]?.y?.toFloat() ?: 0f)
        for (i in 1 until points.size) {
            drawingPath.lineTo(points[i]?.x?.toFloat() ?: 0f, points[i]?.y?.toFloat() ?: 0f)
        }
        drawingPath.lineTo(points[0]?.x?.toFloat() ?: 0f, points[0]?.y?.toFloat() ?: 0f)
    }

    fun calculateVerticalOffset(paint: Paint, verticalOrigin: Float): Int {
        paint.getTextBounds("1234567890", 0, 10, verticalOffsetRect)
        return (((verticalOffsetRect.bottom - verticalOffsetRect.top) / 2) * verticalOrigin).toInt()
    }
}