package com.mktwohy.knitkit

import android.app.Application

class KnittingApplication : Application() {
    val repository: Repository by lazy { Repository(this) }
}