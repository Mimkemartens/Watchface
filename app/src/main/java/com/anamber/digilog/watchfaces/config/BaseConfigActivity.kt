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
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.anamber.digilog.R
import java.util.ArrayList

abstract class BaseConfigActivity : Activity() {

    private var mWearableRecyclerView: WearableRecyclerView? = null
    private var mAdapter: ConfigRecyclerViewAdapter? = null

    internal abstract val watchFaceServiceClass: Class<*>

    internal abstract val sharedPrefFileKeyResId: Int

    internal abstract fun getDataToPopulateAdapter(context: Context): ArrayList<ConfigItemType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_config)

        mAdapter = ConfigRecyclerViewAdapter(
            applicationContext,
            watchFaceServiceClass,
            getDataToPopulateAdapter(this),
            sharedPrefFileKeyResId,
            object : OnConfigItemChangedListener {
                override fun configItemChanged(requestCode: Int) {
                    updateConfigItems(requestCode)
                }
            }
        )

        mWearableRecyclerView = findViewById(R.id.wearable_recycler_view)

        // Aligns the first and last items on the list vertically centered on the screen.
        mWearableRecyclerView?.isEdgeItemsCenteringEnabled = true

        mWearableRecyclerView?.layoutManager = LinearLayoutManager(this)

        // Improves performance because we know changes in content do not change the layout size of
        // the RecyclerView.
        mWearableRecyclerView?.setHasFixedSize(true)

        mWearableRecyclerView?.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            updateConfigItems(requestCode, data)
        }
    }

    private fun updateConfigItems(requestCode: Int, data: Intent? = null) {
        when (requestCode) {
            COMPLICATION_CONFIG_REQUEST_CODE -> {
                // TODO: Handle complication config update
            }
            UPDATE_CLOCK_FACE_CONFIG_REQUEST_CODE -> {
                updateConfigItemsList()
            }
            UPDATE_COLORS_CONFIG_REQUEST_CODE -> {
                // TODO: Handle colors config update
            }
            UPDATE_FONT_CONFIG_REQUEST_CODE -> {
                // TODO: Handle font config update
            }
        }
    }

    private fun updateConfigItemsList() {
        val configItems = getDataToPopulateAdapter(this)

        mAdapter?.setItems(configItems)
        mAdapter?.notifyDataSetChanged()
    }

    internal interface OnConfigItemChangedListener {
        fun configItemChanged(requestCode: Int)
    }

    companion object {
        const val COMPLICATION_CONFIG_REQUEST_CODE = 1001
        const val UPDATE_COLORS_CONFIG_REQUEST_CODE = 1002
        const val UPDATE_CLOCK_FACE_CONFIG_REQUEST_CODE = 1003
        const val UPDATE_FONT_CONFIG_REQUEST_CODE = 1004

        const val EXTRA_SHARED_PREF_FILE = "com.example.anamber.extra.EXTRA_SHARED_PREF_FILE"
        const val EXTRA_SHARED_PREF = "com.example.anamber.extra.EXTRA_SHARED_PREF"
    }
}