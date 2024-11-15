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

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.anamber.digilog.presentation.BaseConfigData
import com.anamber.digilog.presentation.PentagonConfigData

import java.util.ArrayList

class PentagonConfigActivity : BaseConfigActivity() {

    override val watchFaceServiceClass: Class<*>
        get() = PentagonConfigData.watchFaceServiceClass

    override val sharedPrefFileKeyResId: Int
        get() = PentagonConfigData.PREFERENCE_FILE_KEY

    override fun getDataToPopulateAdapter(context: Context): ArrayList<BaseConfigData.ConfigItemType> {
        return PentagonConfigData.getDataToPopulateAdapter(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hier kommen weitere Initialisierungen...
    }
}