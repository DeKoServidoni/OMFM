package com.dekoservidoni.omfm

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.TextView

class OneMoreFabMenu @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr), View.OnClickListener {

    interface OptionsClick {
        fun onOptionClick(optionId: Int?)
    }

    enum class Direction {
        EXPANDED, COLLAPSED
    }

    private var options = PopupMenu(context, null).menu
    private var inflater = MenuInflater(context)
    private var initialFab = FloatingActionButton(context)
    private var clickCallback: OptionsClick? = null

    // flags
    private var closeOnClick = false

    // initial state is collapsed
    private var state = Direction.COLLAPSED

    // tag id
    private val tagId = R.id.omfm_tag

    // sizes
    private var maxButtonWidth = 0
    private var maxButtonHeight = 0
    private var mainFabSize = -1
    private var secondaryFabSize = -1

    // layout parameters
    private var fabSpacing = 0
    private val initialFabRightMargin = 20
    private val initialFabBottomMargin = 25
    private val labelSpacing = 20
    private val childElevation = 10f
    private val labelPadding = 8
    private val initialFabSpacing = 35

    // animations
    private val expandInitialFab = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_main_expand)
    private val collapseInitialFab = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_main_collapse)
    private val upChildAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_child_expand)
    private val downChildAnimation = AnimationUtils.loadAnimation(context, R.anim.omfm_anim_child_collapse)

    // background colors
    private var expandedBackgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
    private var colorMainButton = ContextCompat.getColor(context, R.color.omfm_default_color)
    private var colorSecondaryButtons = ContextCompat.getColor(context, R.color.omfm_default_color)

    // click listener
    private val fabClickListener = OnClickListener {
        clickCallback?.onOptionClick(it.id)

        if(closeOnClick) {
            collapse()
        }
    }

    init {
        initializeUI(attrs)

        downChildAnimation.setAnimationListener(object : Animation.AnimationListener {

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
        val horizontalCenter = right - left - (maxButtonWidth / 2)

        val initialFabTop = bottom - top - initialFab.measuredHeight
        val initialFabLeft = horizontalCenter - (initialFab.measuredWidth / 2)
        val initialFabRight = initialFabLeft + initialFab.measuredWidth
        val initialFabBottom = initialFabTop + initialFab.measuredHeight

        initialFab.layout(initialFabLeft - initialFabRightMargin, initialFabTop - initialFabBottomMargin,
                initialFabRight - initialFabRightMargin, initialFabBottom - initialFabBottomMargin)
        initialFab.setOnClickListener(this@OneMoreFabMenu)

        // bring the initial fab to front so we can
        // call it onClick when the menu is collapsed
        bringChildToFront(initialFab)

        val labelsOffset = (maxButtonWidth / 2)

        var nextY = if(isExpanded()) initialFabTop - fabSpacing
        else initialFabTop + initialFab.measuredHeight + fabSpacing

        for(i in 0..(childCount-1)) {
            val view = getChildAt(i)

            // skipping gone views (because we don't need to calculate) and
            // the initial button
            if(view.id != initialFab.id && view.visibility != View.GONE) {

                // positioning the fab button
                val childX = horizontalCenter - (view.measuredWidth / 2)
                val childY = if(state == Direction.EXPANDED) nextY - view.measuredHeight else nextY

                view.layout(childX - initialFabRightMargin, childY,
                        childX + view.measuredWidth - initialFabRightMargin, childY + view.measuredHeight)

                view.translationY = if(isExpanded()) 0f else (initialFabTop - childY).toFloat()
                view.alpha = if (isExpanded()) 1f else 0f
                view.setOnClickListener(if(isExpanded()) fabClickListener else null)

                // positioning the label on the left of fab
                val label = view.getTag(tagId) as? TextView
                if(label != null) {

                    val labelRight = horizontalCenter - labelsOffset
                    val labelLeft = labelRight - label.measuredWidth
                    val labelTop = childY + (view.measuredHeight - label.measuredHeight) / 2

                    label.layout(labelLeft - labelSpacing, labelTop,
                            labelRight - labelSpacing, labelTop + label.measuredHeight)

                    label.translationY = if (isExpanded()) 0f else (initialFabTop - childY).toFloat()
                    label.alpha = if (isExpanded()) 1f else 0f
                }

                nextY = if(isExpanded()) childY - fabSpacing
                else childY + view.measuredHeight + fabSpacing
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // initializing values
        var height = 0
        var maxLabelWidth = 0
        var width = Resources.getSystem().displayMetrics.widthPixels

        maxButtonWidth = 0
        maxButtonHeight = 0

        // calculating the size of every fab + label
        for(i in 0..(childCount-1)) {
            val view = getChildAt(i)

            if(view.visibility != View.GONE) {

                maxButtonWidth = Math.max(maxButtonWidth, view.measuredWidth)
                height += view.measuredHeight

                // calculating the width of the label
                val label = view.getTag(tagId) as? TextView
                if(label != null) {
                    maxLabelWidth = Math.max(maxLabelWidth, label.measuredWidth)
                }
            }
        }

        if(isExpanded()) {
            // when the view is expanded, the height and width need to be
            // the entire screen
            height = Resources.getSystem().displayMetrics.heightPixels
            setBackgroundColor(expandedBackgroundColor)
            setOnClickListener({ collapse() })
        } else {
            // calculating the total width and height of the component
            width = maxButtonWidth + initialFabSpacing
            height = initialFab.measuredHeight + initialFabSpacing
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            setOnClickListener(null)
        }

        setMeasuredDimension(width, height)
    }

    override fun onClick(view: View?) {
        if (isExpanded()) collapse() else expand()
    }

    /// Public methods

    fun isExpanded() = state == Direction.EXPANDED

    fun collapse() {
        state = Direction.COLLAPSED
        initialFab.startAnimation(collapseInitialFab)
        animateChildren(downChildAnimation)
    }

    fun expand() {
        state = Direction.EXPANDED
        initialFab.startAnimation(expandInitialFab)
        animateChildren(upChildAnimation)
        requestLayout()
    }

    fun show() {
        visibility = View.VISIBLE
        initialFab.show()
    }

    fun hide() {
        if (isExpanded()) {
            downChildAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) { }
                override fun onAnimationEnd(animation: Animation?) {
                    hideMenu()
                    downChildAnimation?.setAnimationListener(null)
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

    private fun initializeUI(attrs: AttributeSet? = null) {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.OneMoreFabMenu)

        if (attributes.hasValue(R.styleable.OneMoreFabMenu_content_options)) {
            inflater.inflate(attributes.getResourceId(R.styleable.OneMoreFabMenu_content_options, 0), options)
        } else {
            throw Exception("OneMoreFabMenu need to have app:content_options with a resource menu!")
        }

        val mainButtonColor = attributes.getResourceId(R.styleable.OneMoreFabMenu_color_main_button, R.color.omfm_default_color)
        this.colorMainButton = ContextCompat.getColor(context, mainButtonColor)

        val secondaryButtonColor = attributes.getResourceId(R.styleable.OneMoreFabMenu_color_secondary_buttons, R.color.omfm_default_color)
        this.colorSecondaryButtons = ContextCompat.getColor(context, secondaryButtonColor)

        val backgroundColor = attributes.getResourceId(R.styleable.OneMoreFabMenu_expanded_background_color, android.R.color.transparent)
        this.expandedBackgroundColor = ContextCompat.getColor(context, backgroundColor)

        this.mainFabSize = attributes.getInt(R.styleable.OneMoreFabMenu_size_main_button, FloatingActionButton.SIZE_NORMAL)
        this.secondaryFabSize = attributes.getInt(R.styleable.OneMoreFabMenu_size_secondary_buttons, FloatingActionButton.SIZE_MINI)

        this.closeOnClick = attributes.getBoolean(R.styleable.OneMoreFabMenu_close_on_click, false)

        addButtons()

        attributes.recycle()
    }

    private fun addButtons() {
        // add the other buttons from the options "menu"
        for (i in 0..(options.size() - 1)) {

            val item = options.getItem(i)

            // creating the floating action button
            val fab = buildFabButton(item, i == 0)

            if(i == 0) {
                // get the first position of the options array
                // to be the first fab button of the component
                initialFab = fab

            } else {
                // creating the label for the button
                val label = buildTextLabel(item)
                fab.setTag(tagId, label)
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

        val size = if(isFirst) mainFabSize else secondaryFabSize
        fab.size = size

        val buttonColor = if(isFirst) colorMainButton else colorSecondaryButtons
        fab.backgroundTintList = ColorStateList.valueOf(buttonColor)

        if (Build.VERSION.SDK_INT >= 21) {
            fab.elevation = childElevation
        }

        return fab
    }

    private fun buildTextLabel(item: MenuItem): TextView {
        val label = TextView(context)
        label.text = item.title
        label.typeface = Typeface.DEFAULT_BOLD
        label.background = ContextCompat.getDrawable(context, R.drawable.omfm_label_rounded_corners)
        label.layoutParams = generateDefaultLayoutParams()
        label.setPadding(labelPadding, labelPadding,labelPadding,labelPadding)

        if (Build.VERSION.SDK_INT >= 21) {
            label.elevation = childElevation
        }

        return label
    }
}