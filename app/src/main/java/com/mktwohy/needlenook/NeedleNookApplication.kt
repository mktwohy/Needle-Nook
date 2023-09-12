package com.mktwohy.needlenook

import android.app.Application

class NeedleNookApplication : Application() {
    val repository: Repository by lazy { Repository(this) }
}