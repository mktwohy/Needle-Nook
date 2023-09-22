package com.mktwohy.needlenook.util.extensions

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

inline fun <reified T : RoomDatabase> Context.roomDatabase(name: String? = null): RoomDatabase.Builder<T> {
    return Room.databaseBuilder(this, T::class.java, name)
}
