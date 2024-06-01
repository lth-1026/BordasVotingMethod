package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VoteActivity : AppCompatActivity() {
    private lateinit var preferencesLayout: LinearLayout
    private lateinit var bordaVoteLayout: LinearLayout
    private lateinit var confirmBt: Button

    private lateinit var options: List<OptionData>

    private var isNotUnique = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vote_activity)

        val optionNames = intent.getStringArrayListExtra("options")

        preferencesLayout = findViewById(R.id.preferencesLayout)
        bordaVoteLayout = findViewById(R.id.bordaVoteLayout)
        confirmBt = findViewById(R.id.confirmBt)

        //set seekbar
        val optionSeekBars = mutableListOf<SeekBar>()

        optionNames?.forEach { optionName ->
            val optionTv = TextView(this)
            optionTv.text = optionName

            preferencesLayout.addView(optionTv)

            val optionSeek = SeekBar(this)
            optionSeek.tag = optionName

            optionSeekBars.add(optionSeek)

            optionSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    calOptionsValue(optionSeekBars)
                }
            })

            preferencesLayout.addView(optionSeek)
        }

        confirmBt.setOnClickListener {
            //if not unique, send toast and return this function, so user can't return main page
            if(isNotUnique) {
                Toast.makeText(this, "Vote is not unique!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()

            val points = options.sortedBy { it.index }.mapIndexed { _, option ->
                option.point
            }

            val pointArray = points.toIntArray()

            resultIntent.putExtra("points", pointArray)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun calOptionsValue(optionSeekBars: MutableList<SeekBar>) {
        //remove previous bordaVote points
        bordaVoteLayout.removeAllViews()

        //when you start calOption, always assume that option is unique
        isNotUnique = false

        //make options by OptionData
        options = optionSeekBars.mapIndexed { index, option ->
            OptionData(option.tag.toString(), option.progress, 0, index, true)
        }

        //sort options by progress
        options = options.sortedWith(OptionComparator()).reversed()

        //attach point with option name on borda vote layout
        options.forEachIndexed {index, option ->
            val valueTv = TextView(this@VoteActivity)
            valueTv.gravity = Gravity.CENTER

            //if point is unique
            if(option.isUnique) {
                option.point = options.size - index - 1
                valueTv.text = "${option.name} -> ${option.point} point"
            } else {
                //point is not unique
                isNotUnique = true
                valueTv.text = "${option.name} -> <not unique>"
            }

            bordaVoteLayout.addView(valueTv)
        }
    }

    data class OptionData(val name: String, val progress: Int, var point: Int, val index: Int, var isUnique: Boolean)

    class OptionComparator: Comparator<OptionData> {
        override fun compare(option1: OptionData, option2: OptionData): Int {
            return if (option1.progress == option2.progress) {
                //option point is not unique
                option1.isUnique = false
                option2.isUnique = false

                option2.index.compareTo(option1.index)
            } else {
                option1.progress.compareTo(option2.progress)
            }
        }
    }

}