package com.example.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.CalculatorFragmentLayoutBinding
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorFragment : Fragment(), OnClickListener {
    private lateinit var mFragmentBinding: CalculatorFragmentLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.calculator_fragment_layout, container, false)
        return mFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextViewListener()
        inputTextViewTextWatcher()
    }

    /**setting onclickListener for all the views of the layout*/
    private fun setTextViewListener() {
        Log.d(TAG, "setTextViewListener() is called")
        mFragmentBinding.one.setOnClickListener(this)
        mFragmentBinding.two.setOnClickListener(this)
        mFragmentBinding.three.setOnClickListener(this)
        mFragmentBinding.four.setOnClickListener(this)
        mFragmentBinding.five.setOnClickListener(this)
        mFragmentBinding.six.setOnClickListener(this)
        mFragmentBinding.seven.setOnClickListener(this)
        mFragmentBinding.eight.setOnClickListener(this)
        mFragmentBinding.nine.setOnClickListener(this)
        mFragmentBinding.zero.setOnClickListener(this)
        mFragmentBinding.plus.setOnClickListener(this)
        mFragmentBinding.minus.setOnClickListener(this)
        mFragmentBinding.multiply.setOnClickListener(this)
        mFragmentBinding.divide.setOnClickListener(this)
        mFragmentBinding.modulas.setOnClickListener(this)
        mFragmentBinding.allClear.setOnClickListener(this)
        mFragmentBinding.clear.setOnClickListener(this)
        mFragmentBinding.decimal.setOnClickListener(this)
        mFragmentBinding.equal.setOnClickListener(this)
    }

    /** make a call to addToInputField() with a specified text as parameter by clicking on specified textview */
    override fun onClick(view: View?) {
        Log.i(TAG, "onClick() is called")
        when (view?.id) {
            R.id.one -> {
                Log.d(TAG, " 1 is pressed")
                addToInputField("1")
            }
            R.id.two -> {
                Log.d(TAG, " 2 is pressed")
                addToInputField("2")
            }
            R.id.three -> {
                Log.d(TAG, " 3 is pressed")
                addToInputField("3")
            }
            R.id.four -> {
                Log.d(TAG, " 4 is pressed")
                addToInputField("4")
            }
            R.id.five -> {
                Log.d(TAG, " 5 is pressed")
                addToInputField("5")
            }
            R.id.six -> {
                Log.d(TAG, " 6 is pressed")
                addToInputField("6")
            }
            R.id.seven -> {
                Log.d(TAG, " 7 is pressed")
                addToInputField("7")
            }
            R.id.eight -> {
                Log.d(TAG, " 8 is pressed")
                addToInputField("8")
            }
            R.id.nine -> {
                Log.d(TAG, " 9 is pressed")
                addToInputField("9")
            }
            R.id.zero -> {
                Log.d(TAG, " 0 is pressed")
                addToInputField("0")
            }
            R.id.plus -> {
                Log.d(TAG, " + is pressed")
                addToInputField("+")
            }
            R.id.minus -> {
                Log.d(TAG, " - is pressed")
                addToInputField("-")
            }
            R.id.divide -> {
                Log.d(TAG, " / is pressed")
                addToInputField("/")
            }
            R.id.multiply -> {
                Log.d(TAG, " * is pressed")
                addToInputField("*")
            }
            R.id.modulas -> {
                Log.d(TAG, " % is pressed")
                addToInputField("%")
            }
            R.id.decimal -> {
                Log.d(TAG, " . is pressed")
                addToInputField(".")
            }
            R.id.allClear -> {
                Log.d(TAG, " onAllClear() is called")
                onAllClear()
            }
            R.id.clear -> {
                Log.d(TAG, " onClear() is called")
                onClear()
            }
            R.id.equal -> {
                Log.d(TAG, " onDoubleClickEqual() is called")
                if (mFragmentBinding.resultTextview.text.isBlank()) {
                    return
                }
                onClickEqual()
            }
        }
    }

    /** if result Textview contains a error message then return else display the result from result textview to input textview */
    private fun onClickEqual() {
        Log.d(TAG, "onClickEqual() is called ")
        val result = mFragmentBinding.resultTextview.text
        if (result == "ERROR") {
            return
        }
        mFragmentBinding.inputTextview.text = result
    }

    /** When input textview is empty also make result textview empty and equal button hidden otherwise
     * clear the text one by one on pressing back button*/
    private fun onClear() {
        Log.d(TAG, "onClear() is called")
        val inputData = mFragmentBinding.inputTextview.text
        if (inputData.isBlank() || inputData.length == 1) {

            mFragmentBinding.resultTextview.text = ""
            mFragmentBinding.inputTextview.text = ""
            mFragmentBinding.textviewEqual.isVisible = false
            return
        }
        mFragmentBinding.inputTextview.text = inputData.dropLast(1)
    }

    /** set input to the inputTextview of calculator by applying certain constraints*/
    private fun addToInputField(input: String) {
        Log.d(TAG, "addToInputField() is called")
        val inputData = mFragmentBinding.inputTextview.text.toString()
        val result = mFragmentBinding.resultTextview.text.toString()
        if (inputData == result && input.isDigitsOnly()) {
            mFragmentBinding.inputTextview.text = ""
            mFragmentBinding.resultTextview.text = ""
            mFragmentBinding.textviewEqual.isVisible = false
        }
        val regex = Regex("\\d+/0+")
        if (inputData.contains(regex)) {
            Log.d(TAG, "input Textview contain  a expression having divide by zero")
            return
        }

        if (input.isDigitsOnly()) {
            Log.d(TAG, "input data is a number")
            mFragmentBinding.inputTextview.append(input)
            return
        }
        val operatorList = listOf("+", "-", "/", "*", "%")
        if (operatorList.contains(input)) {
            Log.d(TAG, "input data is a operator")
            if (inputData.isEmpty() && input != "-") {
                return
            }
            if (inputData.isEmpty() && input == "-") {
                mFragmentBinding.inputTextview.append(input)
                return
            }
            if (inputData == "-") {
                return
            }
            val lastChar = inputData.takeLast(1)
            Log.d(TAG, "last character : $lastChar")
            if (lastChar == input) {
                return
            }

            if (lastChar == ".") {
                return
            }
            if (operatorList.contains(lastChar)) {
                mFragmentBinding.inputTextview.text = inputData.dropLast(1)
                mFragmentBinding.inputTextview.append(input)
                return
            }
            mFragmentBinding.inputTextview.append(input)
            return
        }
        if (input == ".") {

            if (inputData.isEmpty()) {
                mFragmentBinding.inputTextview.append("0.")
                return
            }
            val lastChar = inputData.takeLast(1)
            if (lastChar == ".") {
                return
            }
            if (operatorList.contains(lastChar)) {
                mFragmentBinding.inputTextview.append("0.")
                return
            }
            val operatorIndex = isContainOperator(operatorList, inputData)
            if (operatorIndex != -1) {
                val lastIndex = inputData.length
                val subString = inputData.substring((lastIndex - operatorIndex), lastIndex)
                Log.d(
                    TAG,
                    "substring : $subString operator index: $operatorIndex Last index: $lastIndex"
                )
                if (subString.contains(".")) {
                    return
                }
                mFragmentBinding.inputTextview.append(".")
                return
            }
            if (inputData.contains(".")) {
                return
            }
            mFragmentBinding.inputTextview.append(".")
        }
    }

    /**check if input textview contain operator or not and return the index of operator
     *
     * prams: operatorList : list of string type containing all operators , inputData- String type containg value of input textview
     *
     * return: index - integer type holding the index value of operator*/
    private fun isContainOperator(operatorList: List<String>, inputData: String): Int {
        Log.d(TAG, "isContainOperator() is called")
        val reverseInputData = inputData.reversed()
        Log.d(TAG, "input data after reversing the data is $reverseInputData")
        for (operator in operatorList) {
            if (reverseInputData.contains(operator)) {
                return reverseInputData.indexOf(operator)
            }
        }
        return -1
    }

    /**clear all the data of input textview and result textview also hide the equal button displayed at the beginning of the result calculator */
    private fun onAllClear() {
        Log.d(TAG, "onAllClear() is called")
        mFragmentBinding.inputTextview.text = ""
        mFragmentBinding.resultTextview.text = ""
        mFragmentBinding.textviewEqual.isVisible = false

    }

    /**evaluate the expression present in input textview*/
    private fun evaluateResult() {
        Log.d(TAG, "evaluateResult() is called")
        val inputData = mFragmentBinding.inputTextview.text.toString()
        if (inputData.isEmpty()) {
            return
        }
        if (inputData.isDigitsOnly()) {
            return
        }
        val lastChar = inputData.takeLast(1)
        val operatorList = listOf("+", "-", "*", "/", "%")
        if (isContainOperator(operatorList, inputData) == -1) {
            return
        }
        if (operatorList.contains(lastChar)) {
            return
        }
        if (lastChar == ".") {
            return
        }
        val regex = Regex("\\d+/0+")
        if (inputData.contains(regex)) {
            mFragmentBinding.resultTextview.text = getString(R.string.error_message)
            return
        }

        try {
            val expression = ExpressionBuilder(inputData).build()
            Log.d(TAG, "value in expression $expression")
            val result = expression.evaluate()
            mFragmentBinding.resultTextview.text = result.toString()
            mFragmentBinding.textviewEqual.isVisible = true
        } catch (e: Exception) {
            return
        }
    }

    /**setting text watcher for the input textview of calculator and calls evaluateResult() on every text change*/
    private fun inputTextViewTextWatcher() {
        Log.d(TAG, "inputTextViewTextWatcher() is called")
        mFragmentBinding.inputTextview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null)
                    return
                evaluateResult()
            }
        })
    }

    companion object {
        private val TAG = CalculatorFragment::class.java.simpleName
    }
}


