package com.mktwohy.needlenook

import android.app.Activity
import android.app.Application

class NeedleNookApplication : Application() {
    val repository: Repository by lazy { Repository(this) }
}

fun <T> Activity.application(body: NeedleNookApplication.() -> T): Lazy<T> = lazy {
    (application as NeedleNookApplication).body()
}