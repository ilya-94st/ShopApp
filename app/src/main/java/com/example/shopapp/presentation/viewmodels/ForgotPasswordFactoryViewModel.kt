package com.example.shopapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.domain.use_cases.CheckForgotPassword

@Suppress("UNCHECKED_CAST")
class ForgotPasswordFactoryViewModel(private val checkForgotPassword: CheckForgotPassword)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)){
            return ForgotPasswordViewModel(checkForgotPassword) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}