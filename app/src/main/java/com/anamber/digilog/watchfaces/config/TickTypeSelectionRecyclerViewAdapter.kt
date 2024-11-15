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
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anamber.digilog.R
import com.anamber.digilog.tile.ClockFaceView
import com.anamber.digilog.watchfaces.TickDrawer

class TickTypeSelectionRecyclerViewAdapter(
    private val mSharedPrefFileString: String,
    private val mSharedPrefString: String?,
    private val mTickTypeDataSet: ArrayList<TickDrawer.TickType>
) : RecyclerView.Adapter<TickTypeSelectionRecyclerViewAdapter.ClockFaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockFaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.config_item_list_clock_face_subitem, parent, false)
        return ClockFaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClockFaceViewHolder, position: Int) {
        holder.bind(mTickTypeDataSet[position])
    }

    override fun getItemCount(): Int {
        return mTickTypeDataSet.size
    }

    inner class ClockFaceViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val mClockFaceView: ClockFaceView = view.findViewById(R.id.clock_face)

        init {
            view.setOnClickListener(this)
        }

        fun bind(tickType: TickDrawer.TickType) {
            mClockFaceView.setTickType(tickType)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val tickType = mTickTypeDataSet[position]
                val activity = view.context as Activity

                mSharedPrefString?.let { prefString ->
                    val sharedPref = activity.getSharedPreferences(mSharedPrefFileString, Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString(prefString, tickType.name)
                        commit()
                    }
                    activity.setResult(Activity.RESULT_OK)
                }
                activity.finish()
            }
        }
    }

    companion object {
        private const val TAG = "TickTypeSelectionRecyclerViewAdapter"
    }
}