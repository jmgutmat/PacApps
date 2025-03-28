/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmgtumat.pacapps.util

import java.util.Calendar

/**
 * Returns the start of today in milliseconds
 */
fun getDefaultDateInMillis(): Long {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val date = cal.get(Calendar.DATE)
    cal.clear()
    cal.set(year, month, date)
    return cal.timeInMillis
}

fun Calendar.getDayOfWeekString(): String {
    return when (this.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SUNDAY -> "domingo"
        Calendar.MONDAY -> "lunes"
        Calendar.TUESDAY -> "martes"
        Calendar.WEDNESDAY -> "miércoles"
        Calendar.THURSDAY -> "jueves"
        Calendar.FRIDAY -> "viernes"
        Calendar.SATURDAY -> "sábado"
        else -> throw IllegalArgumentException("Invalid day of week")
    }
}
