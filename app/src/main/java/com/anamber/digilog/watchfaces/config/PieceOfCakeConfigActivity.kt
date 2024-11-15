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
import com.anamber.digilog.model.BaseConfigData
import com.anamber.digilog.model.PieceOfCakeConfigData

import java.util.ArrayList

class PieceOfCakeConfigActivity : BaseConfigActivity() {

    override val watchFaceServiceClass: Class<*>
        get() = PieceOfCakeConfigData.watchFaceServiceClass

    override val sharedPrefFileKeyResId: Int
        get() = PieceOfCakeConfigData.PREFERENCE_FILE_KEY

    override fun getDataToPopulateAdapter(context: Context): ArrayList<BaseConfigData.ConfigItemType> {
        return PieceOfCakeConfigData.getDataToPopulateAdapter(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hier kommen weitere Initialisierungen...
    }
}