package com.dekoservidoni.omfm.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MenuInflater
import android.widget.PopupMenu
import com.dekoservidoni.omfm.R

/**
 * Class responsible to hold all the necessary
 * parameters to build the menu, like heights, widths, animations
 * and etc
 *
 * @author André Servidoni
 */
internal data class OneMoreFabValues(private val context: Context) {

    var options = PopupMenu(context, null).menu
    var inflater = MenuInflater(context)

    // flags
    var closeOnClick = false
    var rotateMainButton = true
    var enableMainAsAction = false

    // initial state (collapsed)
    var state = OneMoreFabUtils.Direction.COLLAPSED

    // sizes
    var maxButtonWidth = 0
    var maxButtonHeight = 0
    var mainFabSize = -1
    var secondaryFabSize = -1

    // layout parameters
    val initialFabRightMargin = 20
    val initialFabBottomMargin = 25
    val initialFabSpacing = 35

    var fabSpacing = 0
    val labelSpacing = 20
    val labelPadding = 8

    val childElevation = 10f

    // background colors
    var labelBackgroundColor = -1
    var labelTextColor = ContextCompat.getColor(context, R.color.omfm_label_text_black)
    var expandedBackgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
    var colorMainButton = ContextCompat.getColor(context, R.color.omfm_default_color)
    var colorSecondaryButtons = ContextCompat.getColor(context, R.color.omfm_default_color)

    // drawables
    var labelBackgroundDrawable = R.drawable.omfm_label_rounded_corners
    var mainCollapsedDrawable: Drawable? = null
    var mainExpandedDrawable: Drawable? = null

    fun initializeValues(attrs: AttributeSet? = null) {
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

        this.labelBackgroundColor = attributes.getColor(R.styleable.OneMoreFabMenu_label_background_color, -1)
        this.labelBackgroundDrawable = attributes.getResourceId(R.styleable.OneMoreFabMenu_label_background_drawable, R.drawable.omfm_label_rounded_corners)

        val labelTextColor = attributes.getResourceId(R.styleable.OneMoreFabMenu_label_text_color, R.color.omfm_label_text_black)
        this.labelTextColor = ContextCompat.getColor(context, labelTextColor)

        this.mainFabSize = attributes.getInt(R.styleable.OneMoreFabMenu_size_main_button, FloatingActionButton.SIZE_NORMAL)
        this.secondaryFabSize = attributes.getInt(R.styleable.OneMoreFabMenu_size_secondary_buttons, FloatingActionButton.SIZE_MINI)

        this.closeOnClick = attributes.getBoolean(R.styleable.OneMoreFabMenu_close_on_click, false)
        this.rotateMainButton = attributes.getBoolean(R.styleable.OneMoreFabMenu_rotate_main_button, true)
        this.enableMainAsAction = attributes.getBoolean(R.styleable.OneMoreFabMenu_enable_main_as_action, false)

        val mainExpandedDrawable = attributes.getResourceId(R.styleable.OneMoreFabMenu_main_action_drawable, -1)
        this.mainExpandedDrawable = if(mainExpandedDrawable != -1) ContextCompat.getDrawable(context, mainExpandedDrawable) else null

        attributes.recycle()
    }
}