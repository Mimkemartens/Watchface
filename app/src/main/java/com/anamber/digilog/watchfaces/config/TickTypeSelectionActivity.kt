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
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anamber.digilog.R
import com.anamber.digilog.watchfaces.watchface.TickDrawer
import java.util.ArrayList

/**
 * Allows user to select color for something on the watch face (background, highlight,etc.) and
 * saves it to [android.content.SharedPreferences] in
 * [androidx.recyclerview.widget.RecyclerView.Adapter].
 */
class TickTypeSelectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_item_selection)

        val tickTypes = ArrayList<TickDrawer.TickType>()
        tickTypes.add(TickDrawer.TickType.STANDARD)
        tickTypes.add(TickDrawer.TickType.DETAIL)
        tickTypes.add(TickDrawer.TickType.MINIMAL)
        tickTypes.add(TickDrawer.TickType.ROMAN)

        val mTickTypeSelectionRecyclerViewAdapter = TickTypeSelectionRecyclerViewAdapter(
            intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF_FILE),
            intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF),
            tickTypes
        )


        val mConfigAppearanceWearableRecyclerView = findViewById<RecyclerView>(R.id.wearable_recycler_view)

        // Aligns the first and last items on the list vertically centered on the screen.
        mConfigAppearanceWearableRecyclerView.isEdgeItemsCenteringEnabled = true

        mConfigAppearanceWearableRecyclerView.layoutManager = LinearLayoutManager(this)

        // Improves performance because we know changes in content do not change the layout size of
        // the RecyclerView.
        mConfigAppearanceWearableRecyclerView.setHasFixedSize(true)

        mConfigAppearanceWearableRecyclerView.adapter = mTickTypeSelectionRecyclerViewAdapter
    }
}