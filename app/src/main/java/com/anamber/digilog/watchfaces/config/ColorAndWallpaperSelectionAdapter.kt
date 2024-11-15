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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.anamber.digilog.R // Adjust import for your R file

class ColorAndWallpaperAdapter(
    private val colorList: List<Int>,
    private val wallpaperList: List<Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_COLOR = 0
        const val VIEW_TYPE_WALLPAPER = 1
    }

    // ViewHolder for color items
    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorView: View = itemView.findViewById(R.id.colorView) // Replace with your actual ID
    }

    // ViewHolder for wallpaper items
    class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wallpaperPreview: ImageView = itemView.findViewById(R.id.wallpaperPreview) // Replace with your actual ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_COLOR -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.config_item_list_color_subitem, parent, false) // Adjust layout name
                ColorViewHolder(view)
            }
            VIEW_TYPE_WALLPAPER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.wallpaper_picker_item, parent, false) // Adjust layout name
                WallpaperViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_COLOR -> {
                val colorViewHolder = holder as ColorViewHolder
                colorViewHolder.colorView.setBackgroundColor(colorList[position])
            }
            VIEW_TYPE_WALLPAPER -> {
                val wallpaperViewHolder = holder as WallpaperViewHolder
                val wallpaperId = wallpaperList[position - colorList.size] // Adjust index
                Glide.with(holder.itemView.context).load(wallpaperId).into(wallpaperViewHolder.wallpaperPreview)
            }
        }
    }

    override fun getItemCount(): Int {
        return colorList.size + wallpaperList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < colorList.size) {
            VIEW_TYPE_COLOR
        } else {
            VIEW_TYPE_WALLPAPER 
        }
    }
}