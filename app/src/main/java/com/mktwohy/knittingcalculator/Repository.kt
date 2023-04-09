package com.mktwohy.knittingcalculator

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Repository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Key.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    var count: Int
        get() = sharedPreferences.getInt(Key.COUNT, 0)
        set(value) {
            sharedPreferences.edit {
                putInt(Key.COUNT, value)
            }
        }
    private object Key {
        const val SHARED_PREFERENCES = "SharedPreferences"
        const val COUNT = "Count"
    }
}