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

import java.util.ArrayList

class TickNumbersSelectionRecyclerViewAdapter(
    private val mSharedPrefFileString: String,
    private val mSharedPrefString: String?,
    private val mTickNumbersDataSet: ArrayList<TickDrawer.TickNumbers>,
    tickType: TickDrawer.TickType
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mTickType = TickDrawer.TickType.STANDARD

    init {
        mTickType = tickType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClockFaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.config_item_list_clock_face_subitem, parent, false), mTickType)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val colorViewHolder = viewHolder as ClockFaceViewHolder
        colorViewHolder.setTickNumbers(mTickNumbersDataSet[position])
    }

    override fun getItemCount(): Int {
        return mTickNumbersDataSet.size
    }

    inner class ClockFaceViewHolder(view: View, tickType: TickDrawer.TickType) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val mClockFaceView: ClockFaceView

        init {
            mClockFaceView = view.findViewById(R.id.clock_face)
            mClockFaceView.setTickType(tickType)
            view.setOnClickListener(this)
        }

        fun setTickNumbers(tickNumbers: TickDrawer.TickNumbers) {
            mClockFaceView.setTickNumbers(tickNumbers)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            val tickNumbers = mTickNumbersDataSet[position]

            val activity = view.context as Activity

            if (mSharedPrefString != null && !mSharedPrefString.isEmpty()) {
                val sharedPref = activity.getSharedPreferences(
                    mSharedPrefFileString,
                    Context.MODE_PRIVATE
                )

                val editor = sharedPref.edit()
                editor.putString(mSharedPrefString, tickNumbers.name)
                editor.commit()

                // Let's Complication Config Activity know there was an update to colors.
                activity.setResult(Activity.RESULT_OK)
            }
            activity.finish()
        }
    }

    companion object {
        private val TAG = TickNumbersSelectionRecyclerViewAdapter::class.java.simpleName
    }
}