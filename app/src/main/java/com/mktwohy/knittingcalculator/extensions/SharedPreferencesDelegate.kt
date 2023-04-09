package com.mktwohy.knittingcalculator.extensions

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val SharedPreferences.delegate get() = SharedPreferencesDelegate(this)

class SharedPreferencesDelegate(private val preferences: SharedPreferences) {
    fun boolean(key: String? = null, default: Boolean = false): ReadWriteProperty<Any, Boolean> =
        createDelegate(
            default = default,
            key = key,
            getter = preferences::getBoolean,
            setter = preferences.edit()::putBoolean
        )

    fun int(key: String? = null, default: Int = 0): ReadWriteProperty<Any, Int> =
        createDelegate(
            default = default,
            key = key,
            getter = preferences::getInt,
            setter = preferences.edit()::putInt
        )

    fun float(key: String? = null, default: Float = 0f): ReadWriteProperty<Any, Float> =
        createDelegate(
            default = default,
            key = key,
            getter = preferences::getFloat,
            setter = preferences.edit()::putFloat
        )

    fun string(key: String? = null, default: String = ""): ReadWriteProperty<Any, String> =
        createDelegate(
            default = default,
            key = key,
            getter = { k, d -> preferences.getString(k, d)!! },
            setter = preferences.edit()::putString
        )

    fun stringSet(key: String? = null, default: Set<String> = emptySet()): ReadWriteProperty<Any, Set<String>> =
        createDelegate(
            default = default,
            key = key,
            getter = { k, d -> preferences.getStringSet(k, d)!! },
            setter = preferences.edit()::putStringSet
        )

    private fun <T> createDelegate(
        default: T,
        key: String? = null,
        getter: (key: String, default: T) -> T,
        setter: (key: String, value: T) -> SharedPreferences.Editor
    ) = object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, default)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setter(key ?: property.name, value).apply()
        }
    }
}
