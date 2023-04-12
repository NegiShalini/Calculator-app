package com.example.calculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mActivityMainBindingBinding: ActivityMainBinding

    private val mCalculatorFragment by lazy { CalculatorFragment() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBindingBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        addFragment(mCalculatorFragment)
    }

    /**add a new new fragment in the mainActivity and return if already added*/
    private fun addFragment(fragment: Fragment) {
        Log.d(TAG, "addFragment() is called")
        if (fragment.isAdded) {
            return
        }
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, fragment)
            .commit()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}

//mFragmentBinding.inputTextview.text =  mFragmentBinding.inputTextview.text.substring(0,  mFragmentBinding.inputTextview.text.length - 1)
//mFragmentBinding.inputTextview.append(operator)
//mOperatorAdded = true