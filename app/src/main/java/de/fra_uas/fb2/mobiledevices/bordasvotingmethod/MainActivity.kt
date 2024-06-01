package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private lateinit var numberOptionEt: EditText
    private lateinit var votingEt: EditText
    private lateinit var addBt: Button
    private lateinit var voteCountTv: TextView
    private lateinit var resultTv: TextView

    private lateinit var voteInfo: VoteInfo

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOptionEt = findViewById(R.id.numberOptionEt)
        votingEt = findViewById(R.id.votingEt)
        addBt = findViewById(R.id.addBt)
        voteCountTv = findViewById(R.id.votedCountTv)
        resultTv = findViewById(R.id.resultTv)

        //prepare for resultActivity
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val resultIntent = result.data
                val returnedResult = resultIntent?.getIntArrayExtra("points")

                //plus at resultTv
                val voteValue = voteCountTv.text.toString().toInt()
                voteCountTv.text = (voteValue + 1).toString()
                //TODO: sum each previous points

            }
        }

        //init new voteInfo
        voteInfo = VoteInfo()

        numberOptionEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) handleOptionCount()
        }

        votingEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) handleOptions()
        }

        addBt.setOnClickListener {
            moveToVoteActivity()
        }
    }

    private fun moveToVoteActivity() {
        handleOptions()

        val intent = Intent(this, VoteActivity::class.java)
        intent.putStringArrayListExtra("options", ArrayList(voteInfo.getOptionNames()))

        startForResult.launch(intent)
    }

    private fun handleOptionCount() {
        var number = 3
        val numberValue = numberOptionEt.text.toString()

        if (numberValue.isNotEmpty()) {
            number = numberValue.toInt()
        }

        if(number < 2 || number > 10) {
            //todo: do something
        }

        voteInfo.setNumOfOpt(number)

        //when options is empty, not work
        if(votingEt.text.isNotEmpty()) {
            handleOptions()

        }
    }

    private fun handleOptions() {
        val inputValue = votingEt.text.toString()

        voteInfo.makeOptions(inputValue)

        votingEt.setText(voteInfo.getOptionNamesToString())
    }
}