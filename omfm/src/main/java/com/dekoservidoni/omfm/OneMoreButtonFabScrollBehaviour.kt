package com.dekoservidoni.omfm

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Created by antonio on 11/4/17.
 */
class OneMoreButtonFabScrollBehaviour(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<OneMoreFabMenu>() {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: OneMoreFabMenu,
                                     directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                nestedScrollAxes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: OneMoreFabMenu,
                                target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, type)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide()
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}