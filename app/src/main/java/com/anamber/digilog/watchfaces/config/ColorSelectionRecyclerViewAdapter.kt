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
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anamber.digilog.R // Corrected import

class ColorSelectionRecyclerViewAdapter(
    private val mSharedPrefFileString: String,
    private val mSharedPrefString: String?,
    private val mColorOptionsDataSet: List<Int> // Changed to List<Int>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder(): viewType: $viewType")
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.config_item_list_color_subitem, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        val color = mColorOptionsDataSet[position]
        val colorViewHolder = viewHolder as ColorViewHolder
        colorViewHolder.setColor(color)
    }

    override fun getItemCount(): Int {
        return mColorOptionsDataSet.size
    }

    inner class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val mColorCircleImageView: View = view.findViewById(R.id.color)

        init {
            view.setOnClickListener(this)
        }

        fun setColor(color: Int) {
            mColorCircleImageView.setBackgroundColor(color)
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) { // Check for valid position
                val color = mColorOptionsDataSet[position]

                Log.d(TAG, "Color: $color onClick() position: $position")

                val activity = view.context as Activity

                // Use a safe call operator and let in case mSharedPrefString is null
                mSharedPrefString?.let { sharedPrefKey -> 
                    val sharedPref = activity.getSharedPreferences(
                        mSharedPrefFileString,
                        Context.MODE_PRIVATE
                    )

                    val editor = sharedPref.edit()
                    editor.putInt(sharedPrefKey, color)
                    editor.apply()
                    // Let's Complication Config Activity know there was an update to colors.
                    activity.setResult(Activity.RESULT_OK)
                }
                activity.finish()
            }
        }
    }

    companion object {
        private const val TAG = "ColorSelectionAdapter"
    }
}

