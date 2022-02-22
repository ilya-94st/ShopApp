package com.example.shopapp.domain.use_cases

import android.text.TextUtils

class CheckForgotPassword {

    fun isEmptyField(filed:String) = TextUtils.isEmpty(filed.trim { it <= ' ' })

    fun checkEmail(filed: String) = !filed.contains("@")
}