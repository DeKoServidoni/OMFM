package com.dekoservidoni.omfm

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.TextView

import com.dekoservidoni.omfm.utils.OneMoreFabUtils
import com.dekoservidoni.omfm.utils.OneMoreFabValues

class OneMoreFabMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr), View.OnClickListener {

    interface OptionsClick {
        fun onOptionClick(optionId: Int?)
    }

    private var initialFab = FloatingActionButton(context)
    private var clickCallback: OneMoreFabMenu.OptionsClick? = null

    private var values = OneMoreFabValues(context)
    private var utils = OneMoreFabUtils(context)

    // click listener
    private val fabClickListener = OnClickListener {
        clickCallback?.onOptionClick(it.id)

        if(values.closeOnClick) {
            collapse()
        }
    }

    init {
        // initialize the values from attributes
        // and create them
        values.initializeValues(attrs)
        addButtons()

        utils.downChildAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                // empty
            }

            override fun onAnimationEnd(animation: Animation) {
                requestLayout()
            }

            override fun onAnimationRepeat(animation: Animation) {
                // empty
            }
        })
    }

    /// Override methods

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        // calculating the initial button sizes for use as reference
        val horizontalCenter = right - left - (values.maxButtonWidth / 2)

        val initialFabTop = bottom - top - initialFab.measuredHeight
        val initialFabLeft = horizontalCenter - (initialFab.measuredWidth / 2)
        val initialFabRight = initialFabLeft + initialFab.measuredWidth
        val initialFabBottom = initialFabTop + initialFab.measuredHeight

        // calculate the main button and it's label (if exists)
        calculateMainButton(initialFabTop, initialFabLeft, initialFabRight, initialFabBottom)

        // setup the main button as needed
        setupMainButton()

        // calculate the options buttons and it's respective labels
        calculateOptionsButton(initialFabTop, horizontalCenter)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // initializing values
        var height = 0
        var maxLabelWidth = 0
        var width = Resources.getSystem().displayMetrics.widthPixels

        values.maxButtonWidth = 0
        values.maxButtonHeight = 0

        // calculating the size of every fab + label
        for(i in 0..(childCount-1)) {
            val view = getChildAt(i)

            if(view.id != utils.LABEL_ID && view.id != initialFab.id && view.visibility != View.GONE) {

                values.maxButtonWidth = Math.max(values.maxButtonWidth, view.measuredWidth)
                height += view.measuredHeight

                // calculating the width of the label
                val label = view.getTag(utils.TAG_ID) as? TextView
                if(label != null) {
                    maxLabelWidth = Math.max(maxLabelWidth, label.measuredWidth)
                }
            }
        }

        if(isExpanded()) {
            // when the view is expanded, the height and width need to be
            // the entire screen
            height = Resources.getSystem().displayMetrics.heightPixels
            setBackgroundColor(values.expandedBackgroundColor)
            setOnClickListener({ collapse() })
        } else {
            // calculating the total width and height of the component
            width = values.maxButtonWidth + values.initialFabSpacing
            height = initialFab.measuredHeight + values.initialFabSpacing
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            setOnClickListener(null)
        }

        setMeasuredDimension(width, height)
    }

    override fun onClick(view: View?) {
        if (isExpanded()) collapse() else expand()
    }

    /// Public methods

    fun isExpanded() = values.state == OneMoreFabUtils.Direction.EXPANDED

    fun collapse() {
        values.state = OneMoreFabUtils.Direction.COLLAPSED

        if(values.rotateMainButton) {
            initialFab.startAnimation(utils.collapseInitialFabAnimation)
        }

        animateChildren(utils.downChildAnimation)
    }

    fun expand() {
        values.state = OneMoreFabUtils.Direction.EXPANDED

        if(values.rotateMainButton) {
            initialFab.startAnimation(utils.expandInitialFabAnimation)
        }

        animateChildren(utils.upChildAnimation)
        requestLayout()
    }

    fun show() {
        visibility = View.VISIBLE
        initialFab.show()
    }

    fun hide() {
        if (isExpanded()) {
            utils.downChildAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) { }
                override fun onAnimationEnd(animation: Animation?) {
                    hideMenu()
                    utils.downChildAnimation?.setAnimationListener(null)
                }
                override fun onAnimationStart(animation: Animation?) { }
            })
            collapse()
        } else {
            hideMenu()
        }
    }

    fun setOptionsClick(callback: OptionsClick) {
        clickCallback = callback
    }

    /// Private methods

    private fun hideMenu() {
        initialFab.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onShown(fab)
                fab?.visibility = View.INVISIBLE
                visibility = View.INVISIBLE
            }
        })
    }

    /// Menu components setup

    private fun addButtons() {
        // add the other buttons from the options "menu"
        for (i in 0..(values.options.size() - 1)) {

            val item = values.options.getItem(i)

            // creating the floating action button
            val fab = buildFabButton(item, i == 0)

            if(i == 0) {
                // get the first position of the options array
                // to be the first fab button of the component
                initialFab = fab

                if(values.enableMainAsAction
                        && !item.title.isEmpty()
                        && item.title != null) {
                    val mainLabel = buildTextLabel(item, true)
                    initialFab.setTag(utils.TAG_ID, mainLabel)
                    addView(mainLabel)
                }

            } else {
                // creating the label for the button
                val label = buildTextLabel(item, false)
                fab.setTag(utils.TAG_ID, label)
                addView(label)
            }

            // add the views
            addView(fab)
        }
    }

    private fun animateChildren(animation: Animation) {
        for(i in 0..(childCount-1)) {
            val child = getChildAt(i)
            if(child.id != initialFab.id){
                child.startAnimation(animation)
            }
        }
    }

    private fun buildFabButton(item: MenuItem, isFirst: Boolean): FloatingActionButton {
        val fab = FloatingActionButton(context)
        fab.id = item.itemId
        fab.layoutParams = generateDefaultLayoutParams()
        fab.setImageDrawable(item.icon)

        val size = if(isFirst) values.mainFabSize else values.secondaryFabSize
        fab.size = size

        val buttonColor = if(isFirst) values.colorMainButton else values.colorSecondaryButtons
        fab.backgroundTintList = ColorStateList.valueOf(buttonColor)

        if (Build.VERSION.SDK_INT >= 21) {
            fab.elevation = values.childElevation
        }

        if(isFirst) {
            values.mainCollapsedDrawable = item.icon
        }

        return fab
    }

    private fun buildTextLabel(item: MenuItem, isFirst: Boolean): TextView {
        val label = TextView(context)
        label.text = item.title
        label.typeface = Typeface.DEFAULT_BOLD

        label.background = if(values.labelBackgroundColor != -1) ColorDrawable(values.labelBackgroundColor)
                            else ContextCompat.getDrawable(context, values.labelBackgroundDrawable)

        label.layoutParams = generateDefaultLayoutParams()
        label.setPadding(values.labelPadding, values.labelPadding, values.labelPadding, values.labelPadding)
        label.setTextColor(ColorStateList.valueOf(values.labelTextColor))

        if(isFirst) {
            label.id = utils.LABEL_ID
            label.alpha = 0f
        }

        if (Build.VERSION.SDK_INT >= 21) {
            label.elevation = values.childElevation
        }

        return label
    }

    /// Menu components calculations

    private fun setupMainButton() {
        // change the drawable of the main button
        // if it was set as action
        if(values.enableMainAsAction && values.mainExpandedDrawable != null) {
            initialFab.setImageDrawable(if(isExpanded()) values.mainExpandedDrawable else values.mainCollapsedDrawable)
        }

        // set the listener of the main button
        // if the main was enabled as action
        initialFab.setOnClickListener(if(!values.enableMainAsAction || !isExpanded()) this@OneMoreFabMenu else fabClickListener)

        // bring the initial fab to front so we can
        // call it onClick when the menu is collapsed
        bringChildToFront(initialFab)
    }

    private fun calculateMainButton(initialFabTop: Int, initialFabLeft: Int, initialFabRight: Int, initialFabBottom: Int) {
        initialFab.layout(initialFabLeft - values.initialFabRightMargin, initialFabTop - values.initialFabBottomMargin,
                initialFabRight - values.initialFabRightMargin, initialFabBottom - values.initialFabBottomMargin)

        // if this flag is true so we need to show the label of
        // the main button that are inside the content defined by user
        if(values.enableMainAsAction) {
            val label = initialFab.getTag(utils.TAG_ID) as? TextView

            if (label != null) {
                val labelRight = initialFab.left - values.labelSpacing
                val labelLeft = labelRight - label.measuredWidth
                val labelTop = initialFab.top + (initialFab.height / 4)

                label.layout(labelLeft, labelTop, labelRight, labelTop + label.measuredHeight)
                label.alpha = if (isExpanded()) 1f else 0f

                bringChildToFront(label)
            }
        }
    }

    private fun calculateOptionsButton(initialFabTop: Int, horizontalCenter: Int) {
        val labelsOffset = (values.maxButtonWidth / 2)

        var nextY = if(isExpanded()) initialFabTop - values.fabSpacing
        else initialFabTop + initialFab.measuredHeight + values.fabSpacing

        for(i in 0..(childCount-1)) {
            val view = getChildAt(i)

            // skipping gone views (because we don't need to calculate), the initial button and main label if exists
            if(view.id != initialFab.id && view.id != utils.LABEL_ID && view.visibility != View.GONE) {

                // positioning the fab button
                val childX = horizontalCenter - (view.measuredWidth / 2)
                val childY = if(values.state == OneMoreFabUtils.Direction.EXPANDED) nextY - view.measuredHeight else nextY

                view.layout(childX - values.initialFabRightMargin, childY,
                        childX + view.measuredWidth - values.initialFabRightMargin, childY + view.measuredHeight)

                view.translationY = if(isExpanded()) 0f else (initialFabTop - childY).toFloat()
                view.alpha = if (isExpanded()) 1f else 0f
                view.setOnClickListener(if(isExpanded()) fabClickListener else null)

                // positioning the label on the left of fab
                val label = view.getTag(utils.TAG_ID) as? TextView
                if(label != null) {

                    val labelRight = horizontalCenter - labelsOffset
                    val labelLeft = labelRight - label.measuredWidth
                    val labelTop = childY + (view.measuredHeight - label.measuredHeight) / 2

                    label.layout(labelLeft - values.labelSpacing, labelTop,
                            labelRight - values.labelSpacing, labelTop + label.measuredHeight)

                    label.translationY = if (isExpanded()) 0f else (initialFabTop - childY).toFloat()
                    label.alpha = if (isExpanded()) 1f else 0f
                }

                nextY = if(isExpanded()) childY - values.fabSpacing
                else childY + view.measuredHeight + values.fabSpacing
            }
        }
    }
}