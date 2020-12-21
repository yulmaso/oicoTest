package com.yulmaso.oicotest.utils

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import com.yulmaso.oicotest.R

/**
 *  Created by yulmaso
 *  Date: 10.12.20
 */
class PerkFlow(context: Context, attrs: AttributeSet?) : Flow(context, attrs) {

    fun setup(
        parentView: ViewGroup,
        perks: List<String>
    ) {
        val referencedIds = IntArray(perks.size)
        for (i in perks.indices) {
            val tv = createView(context)
            tv.text = perks[i]
            tv.id = i
            parentView.addView(tv)
            referencedIds[i] = tv.id
        }
        this.referencedIds = referencedIds
    }

    private fun createView(context: Context): TextView {
        val tv = TextView(context)
        tv.setTextAppearance(context, R.style.TagTextViewStyle)
        return tv
    }
}