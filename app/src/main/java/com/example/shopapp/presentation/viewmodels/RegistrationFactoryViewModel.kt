package com.example.shopapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopapp.domain.use_cases.CheckRegistration


@Suppress("UNCHECKED_CAST")
class RegistrationFactoryViewModel(private val checkRegistration: CheckRegistration)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegistrationViewModel::class.java)){
            return RegistrationViewModel(checkRegistration) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}