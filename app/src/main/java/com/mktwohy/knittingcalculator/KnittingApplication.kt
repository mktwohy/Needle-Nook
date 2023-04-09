package com.mktwohy.knittingcalculator

import android.app.Application

class KnittingApplication : Application() {
    val repository: Repository by lazy { Repository(this) }
}