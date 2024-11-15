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

package com.anamber.digilog.presentation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anamber.digilog.R
import com.anamber.digilog.presentation.BaseConfigData
import com.anamber.digilog.watchfaces.config.ConfigRecyclerViewAdapter
import com.anamber.digilog.watchfaces.config.TickNumbersSelectionActivity
import com.anamber.digilog.watchfaces.config.TickTypeSelectionActivity
import com.anamber.digilog.watchfaces.config.ColorSelectionActivity
import com.anamber.digilog.tile.MinimalWatchFaceService
import java.util.ArrayList

class MinimalConfigData : BaseConfigData() {
    companion object {
        val PREFERENCE_FILE_KEY = R.string.watch_face_minimal_preference_file_key

        /**
         * Returns Watch Face Service class associated with configuration Activity.
         */
        val watchFaceServiceClass: Class<*>
            get() = MinimalWatchFaceService::class.java

        fun getDataToPopulateAdapter(context: Context): ArrayList<BaseConfigData.ConfigItemType> {
            return BaseConfigData.getDataToPopulateAdapter(context, PREFERENCE_FILE_KEY)
        }
    }
}
