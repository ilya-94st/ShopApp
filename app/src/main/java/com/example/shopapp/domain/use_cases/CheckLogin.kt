package com.example.shopapp.domain.use_cases

import android.text.TextUtils

class CheckLogin {

    fun isEmptyField(filed:String) = TextUtils.isEmpty(filed.trim { it <= ' ' })

    fun checkEmail(filed: String) = !filed.contains("@")

    fun  passwordLength(field: String) = field.length <= 6

}