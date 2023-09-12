package com.mktwohy.knitkit

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.mktwohy.knitkit.extensions.delegate

class Repository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Key.SHARED_PREFERENCES, Context.MODE_PRIVATE)

    var count: Int by sharedPreferences.delegate.int()

    fun clear() {
        sharedPreferences.edit {
            clear()
            commit()
        }
    }

    private object Key {
        const val SHARED_PREFERENCES = "SharedPreferences"
    }
}