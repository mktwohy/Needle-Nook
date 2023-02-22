package com.mktwohy.knittingcalculator

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Repository {
    private lateinit var sharedPreferences: SharedPreferences
    private object Key {
        const val SHARED_PREFERENCES = "SharedPreferences"
        const val COUNT = "Count"
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(Key.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    var count: Int
        get() = sharedPreferences.getInt(Key.COUNT, 0)
        set(value) {
            sharedPreferences.edit {
                putInt(Key.COUNT, value)
            }
        }
}