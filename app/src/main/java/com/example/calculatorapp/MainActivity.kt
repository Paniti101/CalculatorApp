package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    fun  numberAction(view:View){
        if (view is Button){
            if (view.text == "."){
                if (canAddDecimal)
                    working.append(view.text)

                canAddOperation = false
            }else
            working.append(view.text)
            canAddOperation = true
        }
    }
    fun  operationAction(view:View){
        if (view is Button && canAddOperation){
            working.append(view.text)
            canAddOperation = false
        }
    }

    fun  allClearAction(view:View){
        working.text = ""
        results.text = ""
    }
    fun  backSpaceAction(view:View){
        val length = working.length()
        if (length > 0)
            working.text = working.text.subSequence(0,length-1)
    }
    fun  equalsAction (view:View){
        results.text = calculateResults()
    }

    private  fun calculateResults(): String{
        val digitsOperations = digitsOperations()
        if (digitsOperations.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperations)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex){

                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x')|| list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val  newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for (i in passedList.indices){
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator){
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i +1
                    }
                    'x' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i +1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }


    private fun digitsOperations(): MutableList<Any>{
        val  list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in working.text){
            if (character.isDigit()||character == '.')
                currentDigit += character
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit !="")
            list.add(currentDigit.toFloat())

        return list
    }
}