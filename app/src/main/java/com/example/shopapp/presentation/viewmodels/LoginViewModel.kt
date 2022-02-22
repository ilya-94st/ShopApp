package com.example.shopapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.shopapp.domain.use_cases.CheckLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel(private val checkLogin: CheckLogin): ViewModel() {
    private val _loginEvent = MutableStateFlow<LoginInEvent>(LoginInEvent.Empty)

    val loginEvent: StateFlow<LoginInEvent> = _loginEvent


    fun validLoginDetails(etEmail: String, etPassword: String): Boolean {
        return when {
            checkLogin.isEmptyField(etEmail) -> {
                _loginEvent.value = LoginInEvent.ErrorLoginIn("enter email")
                false
            }
            checkLogin.isEmptyField(etPassword) -> {
                _loginEvent.value = LoginInEvent.ErrorLoginIn("enter password")
                false
            }
            checkLogin.passwordLength(etPassword)-> {
                _loginEvent.value = LoginInEvent.ErrorLoginIn("Введите password больше 6")
                false
            }
            checkLogin.checkEmail(etEmail) -> {
                _loginEvent.value = LoginInEvent.ErrorLoginIn("enter the correct email")
                false
            }
            else -> {
                _loginEvent.value = LoginInEvent.Success
                true
            }
        }
    }

    sealed class LoginInEvent {
        data class ErrorLoginIn(val error: String) : LoginInEvent()
        object Success : LoginInEvent()
        object Empty: LoginInEvent()
    }

}