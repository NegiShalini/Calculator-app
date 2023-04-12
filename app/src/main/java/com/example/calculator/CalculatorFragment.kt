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

    //    private var mDigitAdded = false
//    private var mOperatorAdded = false
//    private var mDecimalValue = false

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
//
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

    /**call onclickNumber() and onClickOperator() to set the specified text to the textview of calculator on clicking on particular keys */
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
                if(mFragmentBinding.resultTextview.text.isBlank())
                {
                    return
                }
                onClickEqual()
            }
        }
    }

    /**display the result from result textview to input textview*/
    private fun onClickEqual() {

        val result = mFragmentBinding.resultTextview.text
        if (result == "ERROR") {
            return
        }
        mFragmentBinding.inputTextview.text = result
    }

    /**clear the text one by one on pressing back button*/
    private fun onClear() {
        Log.d(TAG, "onClear() is called")
        val inputData = mFragmentBinding.inputTextview.text
        if(inputData.isBlank()|| inputData.length==1)
        {

            mFragmentBinding.resultTextview.text=""
            mFragmentBinding.inputTextview.text=""
            mFragmentBinding.textviewEqual.isVisible=false
            return
        }
        mFragmentBinding.inputTextview.text = inputData.dropLast(1)
    }
    /** add input to the input textview of calculator*/
    private fun addToInputField(input: String) {
        Log.d(TAG, "addToInputField() is called")
        val inputData = mFragmentBinding.inputTextview.text.toString()
        val result= mFragmentBinding.resultTextview.text.toString()
        if(inputData==result && input.isDigitsOnly())
        {
            mFragmentBinding.inputTextview.text=""
            mFragmentBinding.resultTextview.text=""
            mFragmentBinding.textviewEqual.isVisible=false
        }
        val regex = Regex("\\d+/0+")
        if (inputData.contains(regex)) {
            return
        }

        if (input.isDigitsOnly()) {
            Log.d(TAG, "input data is a number")
            addNumber(input)
            return
        }
        val operatorList = listOf("+", "-", "/", "*", "%")
        if (operatorList.contains(input)) {
            Log.d(TAG, "input data is a operator")
            if (inputData.isEmpty() && input != "-") {
                return
            }
            if (inputData.isEmpty() && input == "-") {
                addOperator(input)
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
            addOperator(input)
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
                Log.d(TAG, " Operator index : $operatorIndex")
                val lastIndex = inputData.length
                Log.d(TAG, "last index of input textview : $lastIndex")
                val subString = inputData.substring((lastIndex - operatorIndex), lastIndex)
                Log.d(TAG, "substring : $subString")
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
                Log.d(TAG, "$operator value of index is ${reverseInputData.indexOf(operator)}")
                return reverseInputData.indexOf(operator)
            }
        }
        return -1
    }

    /**add operator in the input textview*/
    private fun addOperator(operator: String) {
        Log.d(TAG, "addOperator() is called")
        mFragmentBinding.inputTextview.append(operator)
    }

    /**add number in the input textview*/
    private fun addNumber(number: String) {
        Log.d(TAG, "addNumber() is called")
        mFragmentBinding.inputTextview.append(number)
    }

    /**clear input data and result of the calculator also enable the disabled keys*/
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
            Log.d(TAG, "value in inputTextview $inputData")
            val expression = ExpressionBuilder(inputData).build()
            Log.d(TAG, "value in expression $expression")
            val result = expression.evaluate()
            mFragmentBinding.resultTextview.text = result.toString()
            mFragmentBinding.textviewEqual.isVisible = true
        } catch (e: Exception) {
            return
        }
    }
 //   /**setting onclickListener for all the views of the layout*/
//    private fun setTextViewListener() {
//        Log.d(TAG, "setTextViewListener() is called")
//        mFragmentBinding.one.setOnClickListener(this)
//        mFragmentBinding.two.setOnClickListener(this)
//        mFragmentBinding.three.setOnClickListener(this)
//        mFragmentBinding.four.setOnClickListener(this)
//        mFragmentBinding.five.setOnClickListener(this)
//        mFragmentBinding.six.setOnClickListener(this)
//        mFragmentBinding.seven.setOnClickListener(this)
//        mFragmentBinding.eight.setOnClickListener(this)
//        mFragmentBinding.nine.setOnClickListener(this)
//        mFragmentBinding.zero.setOnClickListener(this)
//        mFragmentBinding.plus.setOnClickListener(this)
//        mFragmentBinding.minus.setOnClickListener(this)
//        mFragmentBinding.multiply.setOnClickListener(this)
//        mFragmentBinding.divide.setOnClickListener(this)
//        mFragmentBinding.modulas.setOnClickListener(this)
//        mFragmentBinding.allClear.setOnClickListener(this)
//        mFragmentBinding.clear.setOnClickListener(this)
//        mFragmentBinding.decimal.setOnClickListener(this)
//        mFragmentBinding.equal.setOnClickListener(this)
//    }
//
//    /**call onclickNumber() and onClickOperator() to set the specified text to the textview of calculator on clicking on particular keys */
//    override fun onClick(view: View?) {
//        Log.i(TAG, "onClick() is called")
//        when (view?.id) {
//            R.id.one -> {
//                Log.d(TAG, " 1 is pressed")
//                enableKeys()
//                onNumberInput("1")
//            }
//            R.id.two -> {
//                Log.d(TAG, " 2 is pressed")
//                enableKeys()
//                onNumberInput("2")
//            }
//            R.id.three -> {
//                Log.d(TAG, " 3 is pressed")
//                enableKeys()
//                onNumberInput("3")
//            }
//            R.id.four -> {
//                Log.d(TAG, " 4 is pressed")
//                enableKeys()
//                onNumberInput("4")
//            }
//            R.id.five -> {
//                Log.d(TAG, " 5 is pressed")
//                enableKeys()
//                onNumberInput("5")
//            }
//            R.id.six -> {
//                Log.d(TAG, " 6 is pressed")
//                enableKeys()
//                onNumberInput("6")
//            }
//            R.id.seven -> {
//                Log.d(TAG, " 7 is pressed")
//                enableKeys()
//                onNumberInput("7")
//            }
//            R.id.eight -> {
//                Log.d(TAG, " 8 is pressed")
//                enableKeys()
//                onNumberInput("8")
//            }
//            R.id.nine -> {
//                Log.d(TAG, " 9 is pressed")
//                enableKeys()
//                onNumberInput("9")
//            }
//            R.id.zero -> {
//                Log.d(TAG, " 0 is pressed")
//                enableKeys()
//                onNumberInput("0")
//            }
//            R.id.plus -> {
//                Log.d(TAG, " + is pressed")
//                onclickOperator("+")
//            }
//            R.id.minus -> {
//                Log.d(TAG, " - is pressed")
//                if (mFragmentBinding.inputTextview.text.isEmpty()) {
//                    mFragmentBinding.inputTextview.text = "-"
//                } else
//                    onclickOperator("-")
//            }
//            R.id.divide -> {
//                Log.d(TAG, " / is pressed")
//                onclickOperator("/")
//            }
//            R.id.multiply -> {
//                Log.d(TAG, " * is pressed")
//                onclickOperator("*")
//            }
//            R.id.modulas -> {
//                Log.d(TAG, " % is pressed")
//                onclickOperator("%")
//            }
//            R.id.decimal -> {
//                Log.d(TAG, " . is pressed")
//                // onclickOperator(".")
//                onclickDecimalPoint(".")
//            }
//            R.id.allClear -> {
//                Log.d(TAG, " onAllClear() is called")
//                enableKeys()
//                onAllClear()
//            }
//            R.id.clear -> {
//                Log.d(TAG, " onClear() is called")
//                enableKeys()
//                onClear()
//            }
//            R.id.equal -> {
//                Log.d(TAG, " onDoubleClickEqual() is called")
//                onDoubleClickEqual()
//            }
//        }
//    }
//
//    /** concatenate decimal point with zero if it is inserted alone without any digit or after an operator
//     * also enable inserting only a single decimal point with one digit
//     *
//     * prams: decimal - string value storing decimal point*/
//    private fun onclickDecimalPoint(decimal: String) {
//        Log.d(TAG, "onClickDecimalPoint() is called")
//        if (!mDigitAdded && !mDecimalValue || mOperatorAdded) {
//            mFragmentBinding.inputTextview.append("0$decimal")
//            mDecimalValue = true
//            mOperatorAdded = false
//            return
//        }
//        if (!mDecimalValue) {
//            mFragmentBinding.inputTextview.append(decimal)
//            mDecimalValue = true
//        }
//    }
//
//    /** setting operator to textview and return if a operator is already there or no digit is there in the input textview
//     *also replace older operator with a new one if two operator are pressed simultaneously
//     *
//     * prams: operator - string value used to store operator like: +,-,*,/,% */
//    private fun onclickOperator(operator: String) {
//        Log.d(TAG, "onClickOperator() is called")
//        if (!mDigitAdded && !mOperatorAdded) {
//            Log.d(TAG, "statement one executed")
//            return
//        }
//        if (mOperatorAdded) {
//            Log.d(TAG, "if block two executed")
//            mFragmentBinding.inputTextview.text = mFragmentBinding.inputTextview.text.substring(
//                0,
//                mFragmentBinding.inputTextview.text.length - 1
//            )
//            mFragmentBinding.inputTextview.append(operator)
//            mOperatorAdded = true
//            mDecimalValue = false
//            return
//        }
//        Log.d(TAG, "block three executed")
//        mFragmentBinding.inputTextview.append(operator)
//        mOperatorAdded = true
//        mDecimalValue = false
//
//    }
//
//    /** setting specified numbers to textview passed by clicking on particular keys of calculator
//     *
//     * prams:number- string value which store the number*/
//    private fun onNumberInput(number: String) {
//        Log.d(TAG, "onClickNumber() is called$number")
////        mFragmentBinding.inputTextview.append(number)
////        mDigitAdded = true
////        mOperatorAdded = false
//    }
//
//    /**evaluating the expression of input textview and showing the result in resultTextview  with equal to symbol in the begining*/
//    private fun evaluateResult() {
//        Log.d(TAG, "evaluateResult() is called")
//        val data = mFragmentBinding.inputTextview.text
//        try {
//            Log.d(TAG, "value in inputTextview $data")
//            val expression = ExpressionBuilder(data.toString()).build()
//            Log.d(TAG, "value in expression $expression")
//            val result = expression.evaluate()
//            mFragmentBinding.resultTextview.text = result.toString()
//            mFragmentBinding.textviewEqual.isVisible = true
//        } catch (e: Exception) {
//            return
//        }
//    }
//
    /**setting text listener to the input textview of fragment*/
    private fun inputTextViewTextWatcher() {
        Log.d(TAG, "inputTextViewTextWatcher() is called")

        mFragmentBinding.inputTextview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "$s")
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                Log.i(TAG, "start $start , $after , $count")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "start $start , $before , $count")
                if (s == null)
                    return
                evaluateResult()
                //addToInputField(s.toString())
//                val regex = Regex("^[.\\d-]+[+*%/-]+\\d+")
//                if (s?.contains(regex) == true) {
//                    evaluateResult()
//                }
            }
        })
    }
//
//    private fun addToInputView(char: String) {
//        Log.d(TAG, "addToInputView: ")
//
//    }
//
//    /**disable operator keys when a expression contain divide by zero*/
//    private fun disableKeys(textview: TextView) {
//        Log.d(TAG, "disableKeys() is called")
//        textview.isClickable = false
//    }
//
//    /**enable operator keys which was disabled*/
//    private fun enableKeys() {
//        Log.d(TAG, "enableKeys() is called")
//        mFragmentBinding.plus.isClickable = true
//        mFragmentBinding.minus.isClickable = true
//        mFragmentBinding.multiply.isClickable = true
//        mFragmentBinding.divide.isClickable = true
//        mFragmentBinding.modulas.isClickable = true
//    }
//
//    /**making result of expression to set as new input data to evaluate*/
//    private fun onDoubleClickEqual() {
//        Log.d(TAG, "onDoubleClickEqual() is called")
//        if (mFragmentBinding.resultTextview.text.isNotEmpty() && mFragmentBinding.resultTextview.text != "ERROR") {
//            mFragmentBinding.inputTextview.text = mFragmentBinding.resultTextview.text
//            mDigitAdded = true
//            mFragmentBinding.resultTextview.text = ""
//            mFragmentBinding.textviewEqual.isVisible = false
//            return
//        }
//        val divideByZeroPattern = Regex("0\\d|/0[\\d.]")
//        if (mFragmentBinding.inputTextview.text.contains(divideByZeroPattern)) {
//            mFragmentBinding.resultTextview.text = "ERROR"
//            disableKeys(mFragmentBinding.plus)
//            disableKeys(mFragmentBinding.minus)
//            disableKeys(mFragmentBinding.multiply)
//            disableKeys(mFragmentBinding.divide)
//            disableKeys(mFragmentBinding.modulas)
//            return
//        }
//
////        mFragmentBinding.inputTextview.text = ""
////        mFragmentBinding.inputTextview.text = mFragmentBinding.resultTextview.text
////        mDigitAdded = true
////        mFragmentBinding.resultTextview.text = ""
////        mFragmentBinding.textviewEqual.isVisible = false
//    }
//
//    /**clear input data and result of the calculator also enable the disabled keys*/
//    private fun onAllClear() {
//        Log.d(TAG, "onAllClear() is called")
//        mFragmentBinding.inputTextview.text = ""
//        mFragmentBinding.resultTextview.text = ""
//        mFragmentBinding.textviewEqual.isVisible = false
//        mDigitAdded = false
//        mOperatorAdded = false
//        mDecimalValue = false
//
//    }
//
//    /**clear the last input text from the input textview of calculator also enable the already disabled keys*/
//    private fun onClear() {
//        Log.d(TAG, "onClear() is called")
//        val lastChar = mFragmentBinding.inputTextview.text.toString().takeLast(1)
//        Log.d(TAG, "value of last character $lastChar")
//        mFragmentBinding.inputTextview.text = mFragmentBinding.inputTextview.text.dropLast(1)
//        val secondLastChar = mFragmentBinding.inputTextview.text.toString().takeLast(1)
//        Log.d(TAG, "second last character of textview is $secondLastChar")
//        val operatorList = listOf("+", "-", "*", "/", "%")
//        if (mFragmentBinding.inputTextview.text.isEmpty()) {
//            mDigitAdded = false
//            mOperatorAdded = false
//            return
//        }
//        if (lastChar == ".") {
//            Log.d(TAG, "last character is decimal")
//            mDecimalValue = false
//            mDigitAdded = false
//            mOperatorAdded = false
//            return
//        }
//        if (lastChar.isDigitsOnly() && operatorList.contains(secondLastChar)) {
//            Log.d(TAG, "last character is a number is removed")
//            mDigitAdded = true
//            mOperatorAdded = true
//            return
//        }
//        if (operatorList.contains(lastChar)) {
//            Log.d(TAG, "last character is a operator is removed")
//            mOperatorAdded = false
//            return
//        }
    //   }

    companion object {
        private val TAG = CalculatorFragment::class.java.simpleName
    }
}


//    private fun onclickDecimalPoint(decimal: String) {
//        if(mDigitAdded && mDecimalValue)
//            return
//        if ( mDigitAdded  )
//        {
//            mFragmentBinding.inputTextview.append(decimal)
//            mDecimalValue=true
//            return
//        }
//        if(!mDigitAdded && !mDecimalValue || mOperatorAdded) {
//            mFragmentBinding.inputTextview.append("0$decimal")
//            mDecimalValue=true
//            return
//        }
//
//    }
// private var mDecimalValue=false
//  mDecimalValue=false





