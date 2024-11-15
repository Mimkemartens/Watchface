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

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.wear.complications.ComplicationHelperActivity
import androidx.wear.complications.ProviderInfoRetriever
import com.anamber.digilog.R
import com.anamber.digilog.databinding.*
import com.anamber.digilog.watchfaces.model.*
import com.anamber.digilog.watchfaces.watchface.BaseWatchFaceService
import java.util.concurrent.Executors

class ConfigRecyclerViewAdapter(
    private val context: Context,
    private val watchFaceServiceClass: Class<*>,
    private val settingsDataSet: ArrayList<BaseConfigData.ConfigItemType>,
    sharedPrefFileKeyResId: Int,
    private val onConfigItemChangedListener: BaseConfigActivity.OnConfigItemChangedListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val watchFaceComponentName: ComponentName
    private val sharedPreferences: SharedPreferences
    private var selectedComplicationId: Int = 0
    private val leftComplicationId: Int
    private val rightComplicationId: Int
    private val providerInfoRetriever: ProviderInfoRetriever

    init {
        watchFaceComponentName = ComponentName(context, watchFaceServiceClass)
        selectedComplicationId = -1
        leftComplicationId = BaseWatchFaceService.getComplicationId(ComplicationLocation.LEFT)
        rightComplicationId = BaseWatchFaceService.getComplicationId(ComplicationLocation.RIGHT)
        sharedPreferences = context.getSharedPreferences(
            context.getString(sharedPrefFileKeyResId),
            Context.MODE_PRIVATE
        )
        providerInfoRetriever = ProviderInfoRetriever(context, Executors.newCachedThreadPool())
        providerInfoRetriever.init()
    }

    override fun getItemViewType(position: Int): Int {
        return when (settingsDataSet[position]) {
            is BaseConfigData.PreviewConfigItem -> TYPE_PREVIEW_AND_COMPLICATIONS_CONFIG
            is BaseConfigData.MoreOptionsConfigItem -> TYPE_MORE_OPTIONS
            is BaseConfigData.ColorConfigItem -> TYPE_COLOR_CONFIG
            is BaseConfigData.TickTypeConfigItem -> TYPE_CLOCK_FACE_TICK_CONFIG
            is BaseConfigData.TickNumbersConfigItem -> TYPE_CLOCK_FACE_NUMBERS_CONFIG
            is BaseConfigData.FontConfigItem -> TYPE_FONT_CONFIG
            is BaseConfigData.ToggleConfigItem -> TYPE_TOGGLE_CONFIG
            is BaseConfigData.UnreadNotificationConfigItem -> TYPE_UNREAD_NOTIFICATION_CONFIG
            is BaseConfigData.BackgroundComplicationConfigItem -> TYPE_BACKGROUND_COMPLICATION_IMAGE_CONFIG
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PREVIEW_AND_COMPLICATIONS_CONFIG -> {
                val binding = ConfigListPreviewAndComplicationsItemBinding.inflate(inflater, parent, false)
                PreviewViewHolder(binding.root)
            }
            TYPE_MORE_OPTIONS -> {
                val binding = ConfigListMoreOptionsItemBinding.inflate(inflater, parent, false)
                MoreOptionsViewHolder(binding.root)
            }
            TYPE_COLOR_CONFIG -> {
                val binding = ConfigListColorItemBinding.inflate(inflater, parent, false)
                ColorPickerViewHolder(binding.root)
            }
            TYPE_CLOCK_FACE_TICK_CONFIG -> {
                val binding = ConfigListTickTypeItemBinding.inflate(inflater, parent, false)
                TickTypePickerViewHolder(binding.root)
            }
            TYPE_CLOCK_FACE_NUMBERS_CONFIG -> {
                val binding = ConfigListTickNumbersItemBinding.inflate(inflater, parent, false)
                TickNumbersPickerViewHolder(binding.root)
            }
            TYPE_FONT_CONFIG -> {
                val binding = ConfigListFontItemBinding.inflate(inflater, parent, false)
                FontPickerViewHolder(binding.root)
            }
            TYPE_TOGGLE_CONFIG -> {
                val binding = ConfigListToggleItemBinding.inflate(inflater, parent, false)
                ToggleViewHolder(binding.root)
            }
            TYPE_UNREAD_NOTIFICATION_CONFIG -> {
                val binding = ConfigListUnreadNotificationItemBinding.inflate(inflater, parent, false)
                UnreadNotificationViewHolder(binding.root)
            }
            TYPE_BACKGROUND_COMPLICATION_IMAGE_CONFIG -> {
                val binding = ConfigListBackgroundComplicationItemBinding.inflate(inflater, parent, false)
                BackgroundComplicationViewHolder(binding.root)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = settingsDataSet[position]
        when (holder) {
            is PreviewViewHolder -> holder.bind(item as BaseConfigData.PreviewConfigItem)
            is MoreOptionsViewHolder -> holder.bind(item as BaseConfigData.MoreOptionsConfigItem)
            is ColorPickerViewHolder -> holder.bind(item as BaseConfigData.ColorConfigItem)
            is TickTypePickerViewHolder -> holder.bind(item as BaseConfigData.TickTypeConfigItem)
            is TickNumbersPickerViewHolder -> holder.bind(item as BaseConfigData.TickNumbersConfigItem)
            is FontPickerViewHolder -> holder.bind(item as BaseConfigData.FontConfigItem)
            is ToggleViewHolder -> holder.bind(item as BaseConfigData.ToggleConfigItem)
            is UnreadNotificationViewHolder -> holder.bind(item as BaseConfigData.UnreadNotificationConfigItem)
            is BackgroundComplicationViewHolder -> holder.bind(item as BaseConfigData.BackgroundComplicationConfigItem)
        }
    }

    override fun getItemCount(): Int = settingsDataSet.size

    inner class PreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: BaseConfigData.PreviewConfigItem) {
            // Implement binding logic
        }
    }

    inner class MoreOptionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: BaseConfigData.MoreOptionsConfigItem) {
            // Implement binding logic
        }
    }

    inner class ColorPickerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.ColorConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class TickTypePickerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.TickTypeConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class TickNumbersPickerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.TickNumbersConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class FontPickerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.FontConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class ToggleViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.ToggleConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class UnreadNotificationViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.UnreadNotificationConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    inner class BackgroundComplicationViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun bind(item: BaseConfigData.BackgroundComplicationConfigItem) {
            // Implement binding logic
        }

        override fun onClick(view: View) {
            // Implement onClick logic
        }
    }

    companion object {
        private const val TAG = "CompConfigAdapter"

        const val TYPE_PREVIEW_AND_COMPLICATIONS_CONFIG = 0
        const val TYPE_MORE_OPTIONS = 1
        const val TYPE_COLOR_CONFIG = 2
        const val TYPE_UNREAD_NOTIFICATION_CONFIG = 3
        const val TYPE_BACKGROUND_COMPLICATION_IMAGE_CONFIG = 4
        const val TYPE_CLOCK_FACE_TICK_CONFIG = 5
        const val TYPE_CLOCK_FACE_NUMBERS_CONFIG = 6
        const val TYPE_FONT_CONFIG = 7
        const val TYPE_TOGGLE_CONFIG = 8
    }
}