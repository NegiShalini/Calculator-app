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
    private val mOperatorList = listOf("+", "-", "/", "*", "%")
    private val mRegex = Regex(REGEX_DIVIDE_BY_ZERO)

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
                onBackKey()
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

    /** set result to inputText and return if result shows error */
    private fun onClickEqual() {
        Log.d(TAG, "onClickEqual() is called ")
        val result = mFragmentBinding.resultTextview.text
        if (result == ERROR_MESSAGE) {
            return
        }
      addDataToInputTextview(result.toString())
    }

    /**calls onAllClear() if inputTextview is either blank or contains one character or clear the text one by one on pressing back button*/
    private fun onBackKey() {
        Log.d(TAG, "onClear() is called")
        val inputData = mFragmentBinding.inputTextview.text
        if (inputData.isEmpty() || inputData.length == 1) {
            onAllClear()
            return
        }
       addDataToInputTextview( inputData.dropLast(1).toString())
    }

    /** set input to the inputTextview of calculator by applying certain constraints*/
    private fun addToInputField(input: String) {
        Log.d(TAG, "addToInputField() is called")
        val inputData = mFragmentBinding.inputTextview.text.toString()
        val result = mFragmentBinding.resultTextview.text.toString()
        if (inputData == result && input.isDigitsOnly()) {
            onAllClear()
        }
        if (inputData.contains(mRegex)) {//return if inputData is divided by zero
            Log.d(TAG, "input Textview contain  a expression having divide by zero")
            return
        }
        if (input.isDigitsOnly()) {
            Log.d(TAG, "input data is a number")
            appendDataToTextview(input)
            return
        }
        if (mOperatorList.contains(input)) {
            addOperatorToTextview(input, inputData)
            return
        }
        if (input == DECIMAL_SIGN) {
            addDecimalToTextview(input, inputData)
            return
        }

    }

    /**set constraints for input textview when input data in decimal*/
    private fun addDecimalToTextview(decimal: String, inputData: String) {
        Log.d(TAG, "isDecimal() is called")
        if (inputData.isEmpty()) {
            concatenateDecimalZero()
            return
        }
        val lastChar = inputData.takeLast(1)
        if (lastChar == decimal) {
            return
        }
        if (mOperatorList.contains(lastChar)) {
            concatenateDecimalZero()
            return
        }
        val operatorIndex = isContainOperator(mOperatorList, inputData)
        if (operatorIndex != -1) {
            if (inputData.substring((inputData.length - operatorIndex), inputData.length)
                    .contains(decimal)
            )
                return
            appendDataToTextview(decimal)
            return
        }
        if (inputData.contains(decimal)) {
            return
        }
        appendDataToTextview(decimal)
    }

    /**concatenate decimal with zero  */
    private fun concatenateDecimalZero() {
        Log.d(TAG, "concatenateZero() is called")
        mFragmentBinding.inputTextview.append(DECIMAL_ZERO)
    }

    /** set data to input textview of calculator*/
    private fun appendDataToTextview(data: String) {
        Log.d(TAG, "appendData() is called ")
        mFragmentBinding.inputTextview.append(data)
    }

    /**set constraints for inputTextview when inputData is a operator*/
    private fun addOperatorToTextview(operator: String, inputData: String) {
        Log.d(TAG, "isOperator() is called")
        Log.d(TAG, "input data is a operator")
        if (inputData.isEmpty() && operator != MINUS_SIGN) {
            return
        }
        if (inputData.isEmpty() && operator == MINUS_SIGN) {
            appendDataToTextview(operator)
            return
        }
        if (inputData == MINUS_SIGN) {
            return
        }
        val lastChar = inputData.takeLast(1)
        if (lastChar == operator) {
            return
        }

        if (lastChar == DECIMAL_SIGN) {
            return
        }
        if (mOperatorList.contains(lastChar)) {
            addDataToInputTextview(inputData.dropLast(1))
            appendDataToTextview(operator)
            return
        }
        appendDataToTextview(operator)
    }


    /**returns the index of last operator if present in inputTextview
     *
     * prams: operatorList-list of operator, inputData-data inputextview holds
     *
     * return: index*/
    private fun isContainOperator(operatorList: List<String>, inputData: String): Int {
        Log.d(TAG, "isContainOperator() is called")
        val reverseInputData = inputData.reversed()
        for (operator in operatorList) {
            if (reverseInputData.contains(operator)) {
                return reverseInputData.indexOf(operator)
            }
        }
        return -1
    }

    /**clear all the data of calculator app */
    private fun onAllClear() {
        Log.d(TAG, "onAllClear() is called")
        addDataToInputTextview("")
        addDataToResultTextview("")
        mFragmentBinding.equalTextView.isVisible = false

    }
/**display data in input textview*/
    private fun addDataToInputTextview(data: String) {
    Log.d(TAG, "addDataToInputTextview() is called")
    mFragmentBinding.inputTextview.text = data
    }

    /**display result in resultTextview after evaluating expression*/
    private fun displayResult(inputData: CharSequence) {
        Log.d(TAG, "evaluateResult() is called")
        if (inputData.isEmpty()) {
            return
        }
        if (inputData.isDigitsOnly()) {
            return
        }
        val lastChar = inputData.takeLast(1)
        if (isContainOperator(mOperatorList, inputData.toString()) == -1) {
            return
        }
        if (mOperatorList.contains(lastChar)) {
            return
        }
        if (lastChar == DECIMAL_SIGN) {
            return
        }
        if (inputData.contains(mRegex)) {
            addDataToResultTextview(ERROR_MESSAGE)
            return
        }
        try {
            evaluateExpression(inputData)
        } catch (e: Exception) {
            Log.d(TAG, "Exception is caught $e")
        }
    }

    /**evaluate the expression of inputTextview*/
    private fun evaluateExpression(inputData: CharSequence) {
        Log.d(TAG, "evaluateExpression() is called")
        val result = ExpressionBuilder(inputData.toString()).build().evaluate()
        addDataToResultTextview(result.toString())
        mFragmentBinding.equalTextView.isVisible = true
    }

    /**display data in result Textview*/
    private fun addDataToResultTextview(data: String) {
        Log.d(TAG, "addDataToResultTextview() is called")
        mFragmentBinding.resultTextview.text = data
    }

    /**setting text watcher for the input textview of calculator ,calls evaluateResult() */
    private fun inputTextViewTextWatcher() {
        Log.d(TAG, "inputTextViewTextWatcher() is called")
        mFragmentBinding.inputTextview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(data: Editable?) {}
            override fun beforeTextChanged(
                data: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(data: CharSequence?, start: Int, before: Int, count: Int) {
                if (data == null)
                    return
                displayResult(data)
            }
        })
    }

    companion object {
        private val TAG = CalculatorFragment::class.java.simpleName
        private const val MINUS_SIGN = "-"
        private const val DECIMAL_SIGN = "."
        private const val DECIMAL_ZERO = "0."
        private const val ERROR_MESSAGE = "ERROR"
        private const val REGEX_DIVIDE_BY_ZERO = "\\d+/0+"
    }
}


