package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import java.io.Serializable

class VoteInfo : Serializable {
    private var numOfOpt = 3
    private var options = mutableListOf<Option>()

    fun setNumOfOpt(inputNum: Int) {
        numOfOpt = inputNum
    }

    fun getOptions(): String {
        return options.sortedWith(compareBy<Option> { it.index })
            .joinToString(",") { option -> option.name }
    }

    //make options by voteEt
    fun makeOptions(input: String) {
        var optionsSize = 0

        if(input.isNotEmpty()) {
            options = input.split(",").mapIndexed{index, option -> Option(option, index)}.toMutableList()
            optionsSize = options.size
        }

        if(optionsSize < numOfOpt) {
            while (optionsSize < numOfOpt) {
                options.add(Option("Option "+ (optionsSize+1), optionsSize))
                optionsSize++
            }
        } else if(optionsSize > numOfOpt) {
            options.subList(numOfOpt, optionsSize).clear()
        }
    }

    fun getOptionNames(): MutableList<String> {
        return options.map{ option ->  option.name}
            .toMutableList()
    }

    fun getOptionNamesToString(): String {
        return getOptionNames().joinToString(",")
    }
}