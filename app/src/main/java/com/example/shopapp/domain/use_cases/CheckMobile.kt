package com.example.shopapp.domain.use_cases

import android.text.TextUtils

class CheckMobile {

    fun isEmptyField(filed:String) = TextUtils.isEmpty(filed.trim { it <= ' ' })

}