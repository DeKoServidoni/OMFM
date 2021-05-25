package com.dekoservidoni.omfm.utils

import android.content.Context
import android.view.animation.AnimationUtils
import com.dekoservidoni.omfm.R


internal data class OneMoreFabUtils(private val context: Context) {

    enum class Direction {
        EXPANDED, COLLAPSED
    }

    // const ids
    val TAG_ID = R.id.omfm_tag
    val LABEL_ID = R.id.omfm_main_label_id

    // animations
    val expandInitialFabAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_main_expand)
    val collapseInitialFabAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_main_collapse)
    val upChildAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_child_expand)
    val downChildAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_child_collapse)
}