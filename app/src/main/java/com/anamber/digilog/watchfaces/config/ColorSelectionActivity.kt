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
import androidx.wear.widget.WearableRecyclerView
import com.anamber.digilog.R
import com.anamber.digilog.presentation.ColorOptions

class ColorSelectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_item_selection)

        val sharedPrefFile = intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF_FILE)
        val sharedPrefKey = intent.getStringExtra(BaseConfigActivity.EXTRA_SHARED_PREF)

        val mColorSelectionRecyclerViewAdapter = ColorSelectionRecyclerViewAdapter(
            sharedPrefFile ?: "",
            sharedPrefKey,
            ColorOptions.colorList // Use your ColorOptions class
        )

        val mConfigAppearanceWearableRecyclerView = findViewById<WearableRecyclerView>(R.id.wearable_recycler_view)

        mConfigAppearanceWearableRecyclerView.isEdgeItemsCenteringEnabled = true

        mConfigAppearanceWearableRecyclerView.layoutManager = LinearLayoutManager(this)

        mConfigAppearanceWearableRecyclerView.setHasFixedSize(true)

        mConfigAppearanceWearableRecyclerView.adapter = mColorSelectionRecyclerViewAdapter
    }
}

