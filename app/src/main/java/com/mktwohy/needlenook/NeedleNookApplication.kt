package com.mktwohy.needlenook

import android.app.Activity
import android.app.Application
import com.mktwohy.needlenook.extensions.roomDatabase

class NeedleNookApplication : Application() {
    val repository: Repository by lazy { Repository(this) }
    val database by lazy { roomDatabase<ProjectDatabase>("projects.db").build() }
}

fun <T> Activity.application(body: NeedleNookApplication.() -> T): Lazy<T> = lazy {
    (application as NeedleNookApplication).body()
}