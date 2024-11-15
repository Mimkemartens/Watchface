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
package com.anamber.digilog.presentation

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import androidx.preference.PreferenceManager
import com.anamber.digilog.R
import com.anamber.digilog.watchfaces.config.*
import com.anamber.digilog.tile.BaseWatchFaceService
import com.anamber.digilog.tile.FontHelper
import com.anamber.digilog.tile.TickDrawer

import java.util.ArrayList

/**
 * Data represents different views for configuring the
 * [BaseWatchFaceService] watch face's appearance and complications
 * via [BaseConfigActivity].
 */

class BaseConfigData {

    /**
     * Interface all ConfigItems must implement so the [RecyclerView]'s Adapter associated
     * with the configuration activity knows what type of ViewHolder to inflate.
     */

    interface ConfigItemType {
        val configType: Int
    }


    data class PreviewConfigItem(val defaultComplicationResourceId: Int) : ConfigItemType {
        override val configType = ConfigRecyclerViewAdapter.TYPE_PREVIEW_AND_COMPLICATIONS_CONFIG
    }

    /**
     * Data for "more options" item in RecyclerView.
     */

data class MoreOptionsConfigItem(val iconResourceId: Int) : ConfigItemType {
        override val configType = ConfigRecyclerViewAdapter.TYPE_MORE_OPTIONS
    }

    /**
     * Data for color picker item in RecyclerView.
     */

    data class ColorConfigItem(
        val name: String,
        val iconResourceId: Int,
        val sharedPrefFileString: String,
        val sharedPrefString: String,
        val activityToChoosePreference: Class<ColorSelectionActivity>
    ) : ConfigItemType {
        override val configType = ConfigRecyclerViewAdapter.TYPE_COLOR_CONFIG
    }

    /**
     * Data for tick type picker item in RecyclerView.
     */

    class TickTypeConfigItem internal constructor(
        val name: String,
        val iconResourceId: Int,
        val sharedPrefFileString: String,
        val sharedPrefString: String,
        val activityToChoosePreference: Class<TickTypeSelectionActivity>
    ) : ConfigItemType {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_CLOCK_FACE_TICK_CONFIG
    }

    /**
     * Data for tick numbers picker item in RecyclerView.
     */

    class TickNumbersConfigItem internal constructor(
        val name: String,
        val iconResourceId: Int,
        val sharedPrefFileString: String,
        val sharedPrefString: String,
        val activityToChoosePreference: Class<TickNumbersSelectionActivity>
    ) : ConfigItemType {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_CLOCK_FACE_NUMBERS_CONFIG
    }

    /**
     * Data for font picker item in RecyclerView.
     */

    class FontConfigItem internal constructor(
        val name: String,
        val iconResourceId: Int,
        val sharedPrefFileString: String,
        val sharedPrefString: String,
        val activityToChoosePreference: Class<FontSelectionActivity>
    ) : ConfigItemType {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_FONT_CONFIG
    }

    class UnreadNotificationConfigItem internal constructor(
        val name: String,
        val iconEnabledResourceId: Int,
        val iconDisabledResourceId: Int,
        val sharedPrefId: Int
    ) : ConfigItemType {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_UNREAD_NOTIFICATION_CONFIG
    }


    open class ToggleConfigItem internal constructor(
        val name: String,
        val iconEnabledResourceId: Int,
        val iconDisabledResourceId: Int,
        val sharedPrefId: Int
    ) : ConfigItemType {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_TOGGLE_CONFIG
    }

    class ToggleExtendedConfigItem internal constructor(
        name: String,
        iconEnabledResourceId: Int,
        iconDisabledResourceId: Int,
        sharedPrefId: Int
    ) : ToggleConfigItem(name, iconEnabledResourceId, iconDisabledResourceId, sharedPrefId) {

        override val configType: Int
            get() = ConfigRecyclerViewAdapter.TYPE_TOGGLE_EXTENDED_CONFIG
    }

companion object {
    val colorOptionsDataSet: ArrayList<Int> = arrayListOf(
		// hell
		Color.parseColor("#FFFFFF"), // weiß
		Color.parseColor("#fffacd"), // hell gelb
		Color.parseColor("#fff8b9"), // Sand
		Color.parseColor("#ffe1c4"), // hell orange
		Color.parseColor("#ffd6d7"), // rosa
		Color.parseColor("#edd6ff"), // hell lila
		Color.parseColor("#d9d7ff"), // hell flieder
		Color.parseColor("#c7c4ff"), // hell blau	
		Color.parseColor("#deffc3"), // hell grün
		Color.parseColor("#c8ff9c"), // weniger hell grün
		Color.parseColor("#f5f5f5"), // hell grau
		
		// normal		
		Color.parseColor("#fff492"), // helles Gelb
		Color.parseColor("#fff06b"), // sandiges Gelb
		Color.parseColor("#ffec44"), // Gelb
		Color.parseColor("#f4dc00"), // tiefes Gelb
		Color.parseColor("#baa800"), // dunkel gelb
		Color.parseColor("#ffcd9d"), // zartroange
		Color.parseColor("#ffaf62"), // hell orange
		Color.parseColor("#ff9b3b"), // weniger hell orange
		Color.parseColor("#ff7d00"), // Orange
		Color.parseColor("#c46000"), // dunkel orange
		Color.parseColor("#b00002"), // dunkel rot
		Color.parseColor("#fe0003"), // Rot
		Color.parseColor("#ff4d50"), // hell rot
		Color.parseColor("#ff888a"), // zart rot
		Color.parseColor("#ffb5c5"), // Rosa
		Color.parseColor("#ff5cf5"), // hell pink
		Color.parseColor("#ff22f0"), // Pink
		Color.parseColor("#e600d7"), // dunkel pink
		Color.parseColor("#ba61ff"), // Lila
		Color.parseColor("#d39bff"), // hell lila
		Color.parseColor("#8f89ff"), // blau flieder
		Color.parseColor("#3227ff"), // sattes blau
		Color.parseColor("#0b00d7"), // dunkles Blau
		Color.parseColor("#584fff"), // blau lila
		Color.parseColor("#74c6f9"), // helles blau
		Color.parseColor("#9cdbff"), // hell blau
		Color.parseColor("#b2ff75"), // helles Grün
		Color.parseColor("#91ff3a"), // giftiges Grün
		Color.parseColor("#70fe00"), // Grün
		Color.parseColor("#5fd700"), // sattes Grün
		Color.parseColor("#459c00"), // weniger dunkles Grün
		Color.parseColor("#347500"), // dunkles Grün
				
                // dunkel        
                Color.parseColor("#C2C2C2"), // weniger helles grau
                Color.parseColor("#969696"), // grau
		Color.parseColor("#6b6100"), // Militär grün
		Color.parseColor("#753900"), // braun
		Color.parseColor("#623000"), // dunkles braun
		Color.parseColor("#750001"), // dunkeles rot
		Color.parseColor("#08009c"), // dunkles blau
                Color.parseColor("#525252"), // dunkles grau
                Color.parseColor("#000000"), // schwarz
)

	fun getWallpaperOptionsDataSet(): ArrayList<Int> = arrayListOf(
        R.drawable.pic1,
        R.drawable.pic2,
        R.drawable.pic3,
        R.drawable.pic4,
        R.drawable.pic5,
        R.drawable.pic6,
        R.drawable.pic7,
        R.drawable.pic8,
        R.drawable.pic9
    )

        fun getFontOptionsDataSet(assetManager: AssetManager): ArrayList<Typeface> =
            Constants.FONT_FAMILIES.map { FontHelper.getTypeFace(it.first, assetManager) }

        fun getDataToPopulateAdapter(context: Context, preferenceFileKey: Int): ArrayList<ConfigItemType> {

            val sharedPrefFileString = context.getString(preferenceFileKey)
            val sharedPref = context.getSharedPreferences(sharedPrefFileString, Context.MODE_PRIVATE)
            val tickType = TickDrawer.TickType.valueOf(sharedPref.getString(context.getString(R.string.saved_tick_type), Constants.TICK_TYPE_DEFAULT.name))
            val tickNNumbers = TickDrawer.TickNumbers.valueOf(sharedPref.getString(context.getString(R.string.saved_tick_numbers), Constants.TICK_NUMBERS_DEFAULT.name))

            val showSecondHand = sharedPref.getBoolean(context.getString(R.string.saved_second_show), true)

            val settingsConfigData = ArrayList<BaseConfigData.ConfigItemType>()

            val complicationConfigItem = BaseConfigData.PreviewConfigItem(R.drawable.add_complication)
            settingsConfigData.add(complicationConfigItem)

            val moreOptionsConfigItem = BaseConfigData.MoreOptionsConfigItem(R.drawable.ic_expand_more_white_18dp)
            settingsConfigData.add(moreOptionsConfigItem)

            val tickTypeConfigItem = BaseConfigData.TickTypeConfigItem(
                context.getString(R.string.config_tick_type_label),
                R.drawable.ic_watch,
                sharedPrefFileString,
                context.getString(R.string.saved_tick_type),
                TickTypeSelectionActivity::class.java
            )
            settingsConfigData.add(tickTypeConfigItem)

            val tickColorConfigItem = BaseConfigData.ColorConfigItem(
                context.getString(R.string.config_tick_color_label),
                R.drawable.icn_styles,
                sharedPrefFileString,
                context.getString(R.string.saved_tick_color),
                ColorSelectionActivity::class.java
            )
            settingsConfigData.add(tickColorConfigItem)

            if (tickType != TickDrawer.TickType.ROMAN) {
                val numbersTypeConfigItem = BaseConfigData.TickNumbersConfigItem(
                    context.getString(R.string.config_tick_numbers_label),
                    R.drawable.ic_numbers,
                    sharedPrefFileString,
                    context.getString(R.string.saved_tick_numbers),
                    TickNumbersSelectionActivity::class.java
                )
                settingsConfigData.add(numbersTypeConfigItem)

                if (tickNNumbers != TickDrawer.TickNumbers.NONE) {
                    val tickFontConfigItem = BaseConfigData.FontConfigItem(
                        context.getString(R.string.config_tick_font_label),
                        R.drawable.ic_font,
                        sharedPrefFileString,
                        context.getString(R.string.saved_tick_font),
                        FontSelectionActivity::class.java
                    )
                    settingsConfigData.add(tickFontConfigItem)
                }
            }

            val backgroundColorConfigItem = BaseConfigData.ColorConfigItem(
                context.getString(R.string.config_background_color_label),
                R.drawable.icn_styles,
                sharedPrefFileString,
                context.getString(R.string.saved_background_color),
                ColorSelectionActivity::class.java
            )
            settingsConfigData.add(backgroundColorConfigItem)

            val hourColorConfigItem = BaseConfigData.ColorConfigItem(
                context.getString(R.string.config_hour_color_label),
                R.drawable.icn_styles,
                sharedPrefFileString,
                context.getString(R.string.saved_hour_color),
                ColorSelectionActivity::class.java
            )
            settingsConfigData.add(hourColorConfigItem)

            val hourFillConfigItem = BaseConfigData.ToggleConfigItem(
                context.getString(R.string.config_hour_fill_label),
                R.drawable.ic_fill,
                R.drawable.ic_fill,
                R.string.saved_hour_fill
            )
            settingsConfigData.add(hourFillConfigItem)

            val minuteColorConfigItem = BaseConfigData.ColorConfigItem(
                context.getString(R.string.config_minute_color_label),
                R.drawable.icn_styles,
                sharedPrefFileString,
                context.getString(R.string.saved_minute_color),
                ColorSelectionActivity::class.java
            )
            settingsConfigData.add(minuteColorConfigItem)

            val minuteFontConfigItem = BaseConfigData.FontConfigItem(
                context.getString(R.string.config_minute_font_label),
                R.drawable.ic_font,
                sharedPrefFileString,
                context.getString(R.string.saved_minute_font),
                FontSelectionActivity::class.java
            )
            settingsConfigData.add(minuteFontConfigItem)

            val secondShowConfigItem = BaseConfigData.ToggleExtendedConfigItem(
                context.getString(R.string.config_second_hand_label),
                R.drawable.ic_visibility,
                R.drawable.ic_visibility_off,
                R.string.saved_second_show
            )
            settingsConfigData.add(secondShowConfigItem)

            if (showSecondHand) {
                val secondColorConfigItem = BaseConfigData.ColorConfigItem(
                    context.getString(R.string.config_second_color_label),
                    R.drawable.icn_styles,
                    sharedPrefFileString,
                    context.getString(R.string.saved_second_color),
                    ColorSelectionActivity::class.java
                )
                settingsConfigData.add(secondColorConfigItem)

                val secondFillConfigItem = BaseConfigData.ToggleConfigItem(
                    context.getString(R.string.config_second_fill_label),
                    R.drawable.ic_fill,
                    R.drawable.ic_fill,
                    R.string.saved_second_fill
                )
                settingsConfigData.add(secondFillConfigItem)
            }

            val unreadNotificationsConfigItem = BaseConfigData.UnreadNotificationConfigItem(
                context.getString(R.string.config_unread_notifications_label),
                R.drawable.ic_notifications_white_24dp,
                R.drawable.ic_notifications_off_white_24dp,
                R.string.saved_unread_notifications_pref
            )
            settingsConfigData.add(unreadNotificationsConfigItem)

            return settingsConfigData
        }
    }
}

