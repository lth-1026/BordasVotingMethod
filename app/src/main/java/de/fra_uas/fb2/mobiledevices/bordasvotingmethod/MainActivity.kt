package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.text.set
import androidx.core.widget.addTextChangedListener
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var numberOptionEt: EditText
    private lateinit var votingEt: EditText

    private var optionCount: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOptionEt = findViewById(R.id.numberOptionEt)
        votingEt = findViewById(R.id.votingEt)

        numberOptionEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                handleOptionCount()
            }
        }

        votingEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
               handleOptions()
            }
        }
    }

    private fun handleOptionCount() {
        val number = numberOptionEt.text.toString().toInt()

        if(number < 2 || number > 10) {
            //todo: do something
        }

        optionCount = number

    }

    private fun handleOptions() {
        val inputValue = votingEt.text.toString()

        var options = mutableListOf<String>()
        var optionsSize = 0

        if(inputValue.isNotEmpty()) {
            options = inputValue.split(",").toMutableList()
            optionsSize = options.size
        }

        if(optionsSize < optionCount) {
            while (optionsSize < optionCount) {
                options.add("Option x")
                optionsSize++
            }
        } else if(optionsSize > optionCount) {
            options.subList(optionCount, optionsSize).clear()
        }

        votingEt.setText(options.joinToString(","))
    }
}