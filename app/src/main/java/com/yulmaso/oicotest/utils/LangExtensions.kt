package com.yulmaso.oicotest.utils

import androidx.lifecycle.LiveData

/**
 *  Created by yulmaso
 *  Date: 30.11.20
 */
fun LiveData<Boolean>.get(): Boolean {
    return when(this.value) {
        true -> true
        else -> false
    }
}