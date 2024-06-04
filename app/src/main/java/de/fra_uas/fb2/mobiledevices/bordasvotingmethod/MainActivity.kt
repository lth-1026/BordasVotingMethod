package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private lateinit var numberOptionEt: EditText
    private lateinit var votingEt: EditText
    private lateinit var startBt: Button
    private lateinit var addBt: Button
    private lateinit var voteCountTv: TextView
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var resultSw: Switch
    private lateinit var resultTv: TextView

    private lateinit var voteInfo: VoteInfo

    private var savedInputNum: String? = "3"
    private var savedInputOption: String? = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberOptionEt = findViewById(R.id.numberOptionEt)
        votingEt = findViewById(R.id.votingEt)
        startBt = findViewById(R.id.startBt)
        addBt = findViewById(R.id.addBt)
        voteCountTv = findViewById(R.id.votedCountTv)
        resultSw = findViewById(R.id.resultSw)
        resultTv = findViewById(R.id.resultTv)

        //prepare for resultActivity
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK) {
                val resultIntent = result.data
                val returnedResult = resultIntent?.getIntArrayExtra("points")

                //plus at resultTv
                val voteValue = voteCountTv.text.toString().toInt()
                voteCountTv.text = (voteValue + 1).toString()

                //sum each previous points
                voteInfo.sumOptionPoints(returnedResult!!.toList())

                //show result directly if isChecked is true
                showResult(resultSw.isChecked)

            }else if(result.resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Vote has been cancelled!", Toast.LENGTH_SHORT).show()
            }
        }

        //init new voteInfo
        voteInfo = VoteInfo()

        numberOptionEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                //get input value and validation and set again
                var numberValue = numberOptionEt.text.toString()
                numberValue = validateOptionNum(numberValue)
                numberOptionEt.setText(numberValue)

                //if something change
                if(savedInputNum != numberValue) {
                    clearVoteResult()
                    Toast.makeText(this, "All votes reset!", Toast.LENGTH_SHORT).show()
                    savedInputNum = numberValue
                    voteInfo.makeOptions(numberValue, savedInputOption)
                }
            }
        }

        votingEt.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus) {
                val inputOptionValue = votingEt.text.toString()

                //if something change
                if(savedInputOption != inputOptionValue) {
                    clearVoteResult()
                    Toast.makeText(this, "All votes reset!", Toast.LENGTH_SHORT).show()
                    savedInputOption = inputOptionValue

                    voteInfo.makeOptions(savedInputNum, inputOptionValue)
                }
            }
        }

        startBt.setOnClickListener {
            //reset vote
            clearVoteResult()
            savedInputNum = "3"
            savedInputOption = ""
            Toast.makeText(this, "Starting a new!", Toast.LENGTH_SHORT).show()
        }

        addBt.setOnClickListener {
            moveToVoteActivity()
        }

        resultSw.setOnCheckedChangeListener { _, isChecked ->
            showResult(isChecked)
        }
    }

    private fun moveToVoteActivity() {
        //make options
        //processOptions()

        //if options is not defined, make options
        //this situation invoke when user never touch edittext
        if(voteInfo.getOptionSize() == 0) {
            //make 3 as default value
            numberOptionEt.setText(savedInputNum)
            voteInfo.makeOptions(savedInputNum, savedInputOption)
        }

        //clear edittext and control data
        numberOptionEt.clearFocus()
        votingEt.clearFocus()

        val intent = Intent(this, VoteActivity::class.java)
        intent.putStringArrayListExtra("options", ArrayList(voteInfo.getOptionNames()))

        startForResult.launch(intent)
    }

    private fun validateOptionNum(input: String): String {
        var num = 3
        if(input.isNotEmpty()) {
            num = input.toInt()
        }

        if(num < 2) {
            num = 2
        } else if(num > 10) {
            num = 10
        }

        return num.toString()
    }

    private fun clearVoteResult() {
        voteInfo.clearOptionsPoint()
        voteCountTv.text = "0"
        resultTv.text = ""
    }

    private fun showResult(isChecked: Boolean) {
        if(isChecked) {
            resultTv.text = voteInfo.getBordaVoteResult()
        } else {
            resultTv.text = ""
        }
    }
}