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
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anamber.digilog.R

class FontSelectionRecyclerViewAdapter(
    private val mSharedPrefFileString: String,
    private val mSharedPrefString: String?,
    private val mFontOptionsDataSet: ArrayList<Typeface>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.config_item_list_font_subitem, parent, false)
        return FontViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val fontViewHolder = viewHolder as FontViewHolder
        fontViewHolder.setTypeface(mFontOptionsDataSet[position])
    }

    override fun getItemCount(): Int {
        return mFontOptionsDataSet.size
    }

    inner class FontViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val textView: TextView = view.findViewById(R.id.text)

        init {
            view.setOnClickListener(this)
        }

        fun setTypeface(typeface: Typeface) {
            textView.typeface = typeface
        }

        override fun onClick(view: View) {
            val position = bindingAdapterPosition

            val activity = view.context as Activity

            if (!mSharedPrefString.isNullOrEmpty()) {
                val sharedPref = activity.getSharedPreferences(
                    mSharedPrefFileString,
                    Context.MODE_PRIVATE
                )

                val editor = sharedPref.edit()
                editor.putInt(mSharedPrefString, position)
                editor.apply()

                // Let's Complication Config Activity know there was an update to colors.
                activity.setResult(Activity.RESULT_OK)
            }
            activity.finish()
        }
    }
}