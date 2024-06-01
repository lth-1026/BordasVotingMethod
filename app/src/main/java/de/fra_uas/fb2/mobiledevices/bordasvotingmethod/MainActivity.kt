package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private lateinit var numberOptionEt: EditText
    private lateinit var votingEt: EditText
    private lateinit var addBt: Button

    private lateinit var voteInfo: VoteInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //prepare for resultActivity
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val resultIntent = result.data
                val returnedResult = resultIntent?.getIntArrayExtra("points")
                //TODO: sum each previous points
            }
        }

        numberOptionEt = findViewById(R.id.numberOptionEt)
        votingEt = findViewById(R.id.votingEt)
        addBt = findViewById(R.id.addBt)

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
        //startActivity(intent)

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