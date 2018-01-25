package com.dekoservidoni.omfmsample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dekoservidoni.omfm.OneMoreFabMenu

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), OneMoreFabMenu.OptionsClick {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOptionsClick(this@MainActivity)
    }

    override fun onOptionClick(optionId: Int?) {

        var text = ""

        when(optionId) {
            R.id.main_option -> text = "Option Main clicked!"
            R.id.option1 -> text = "Option 1 clicked!"
            R.id.option2 -> text = "Option 2 clicked!"
            R.id.option3 -> text = "Option 3 clicked!"
            R.id.option4 -> text = "Option 4 clicked!"
        }

        if(text.isNotEmpty()) {
            Snackbar.make(fragment.view as View, text, Snackbar.LENGTH_SHORT)
                    .setAction("Done", null).show()
        }
    }
}
