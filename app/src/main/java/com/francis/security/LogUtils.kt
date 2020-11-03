package com.francis.security

import android.util.Log
import android.widget.Toast

object LogUtils {

    fun appLog(key: String?, message: String?) {
        Log.v("$key", "$message")
    }

    fun errorLog(key: String?, message: String?) {
        Log.e("$key", "$message")
    }


    fun showToast(message: String?) {
        Log.v("TOAST", "$message")
        Toast.makeText(AppController.instance, "$message", Toast.LENGTH_SHORT).show()
    }


}