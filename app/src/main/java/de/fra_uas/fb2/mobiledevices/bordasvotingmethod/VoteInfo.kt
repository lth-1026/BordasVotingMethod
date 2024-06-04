package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

class VoteInfo {
    private var numOfOpt = 3
    private var options = mutableListOf<Option>()

    fun makeOptions(inputNum: String?, inputOptions: String?) {
        //clear Options
        options.clear()

        //if inputNum is null, set
        var num = inputNum
        num = num ?: "3"
        setNumOfOpt(num.toInt())

        var options = inputOptions
        options = options ?: ""
        processOptions(options)
    }

    private fun setNumOfOpt(inputNum: Int) {
        numOfOpt = inputNum
    }

    fun getOptionSize(): Int {
        return options.size
    }

    //make options by voteEt
    private fun processOptions(input: String) {
        var optionsSize = 0

        if(input.isNotEmpty()) {
            //make option objects, and default value of option.point is 0
            options = input.split(",").mapIndexed{ _, option -> Option(option)}.toMutableList()
            optionsSize = options.size
        }

        if(optionsSize < numOfOpt) {
            while (optionsSize < numOfOpt) {
                options.add(Option("Option "+ (optionsSize+1)))
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

    fun sumOptionPoints(points: List<Int>) {
        points.forEachIndexed { index, point ->
            options[index].point += point
        }
    }

    fun clearOptionsPoint() {
        options.forEach{it.point = 0}
    }

    fun getBordaVoteResult() : String {
        val maxValueIndexes = calMaxOptions()

        //make default text
        val resultTexts = options.mapIndexed { _, option -> "${option.name} -> ${option.point} points" }.toMutableList()
        //highlight at max point by saved index
        maxValueIndexes.forEach { index -> resultTexts[index] = "*** ${resultTexts[index]} ***" }

        return resultTexts.joinToString( "\n")
    }

    private fun calMaxOptions(): List<Int> {
        //if option is not defined, return emptyList
        if(options.size == 0) return emptyList()

        val maxValueIndexes = mutableListOf<Int>()
        var max = options[0].point

        //find max point and save that index at maxvalueIndexes
        options.forEachIndexed { index, option ->
            when {
                option.point > max -> {
                    max = option.point
                    maxValueIndexes.clear()
                    maxValueIndexes.add(index)
                }
                option.point == max -> {
                    maxValueIndexes.add(index)
                }
            }
        }

        return maxValueIndexes
    }

    data class Option(val name: String, var point: Int = 0) {}
}