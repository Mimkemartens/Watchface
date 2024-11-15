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
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.anamber.digilog.R
import com.anamber.digilog.watchfaces.watchface.TickDrawer
import java.util.ArrayList

/**
 * Allows user to select color for something on the watch face (background, highlight,etc.) and
 * saves it to [android.content.SharedPreferences] in
 * [androidx.recyclerview.widget.RecyclerView.Adapter].
 */
class TickNumbersSelectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_item_selection)

        val sharedPrefFileString = intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF_FILE)
        val sharedPref = applicationContext.getSharedPreferences(sharedPrefFileString, Context.MODE_PRIVATE)
        val tickType = TickDrawer.TickType.valueOf(sharedPref.getString(applicationContext.getString(R.string.saved_tick_type), Constants.TICK_TYPE_DEFAULT.name))

        val tickNumbers = ArrayList<TickDrawer.TickNumbers>()
        tickNumbers.add(TickDrawer.TickNumbers.NONE)
        tickNumbers.add(TickDrawer.TickNumbers.QUARTERLY)
        if (tickType != TickDrawer.TickType.MINIMAL) {
            tickNumbers.add(TickDrawer.TickNumbers.HOURLY)
        }

        val tickNumbersSelectionRecyclerViewAdapter = TickNumbersSelectionRecyclerViewAdapter(
            sharedPrefFileString,
            intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF),
            tickNumbers,
            tickType
        )

        val mConfigAppearanceWearableRecyclerView = findViewById<WearableRecyclerView>(R.id.wearable_recycler_view)
        mConfigAppearanceWearableRecyclerView.centerEdgeItems = true
        mConfigAppearanceWearableRecyclerView.layoutManager = LinearLayoutManager(this)
        mConfigAppearanceWearableRecyclerView.setHasFixedSize(true)
        mConfigAppearanceWearableRecyclerView.adapter = tickNumbersSelectionRecyclerViewAdapter
    }
}