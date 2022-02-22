package com.example.shopapp.presentation.ui

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentLoginBinding
import com.example.shopapp.domain.use_cases.CheckLogin
import com.example.shopapp.presentation.base.BaseFragment
import com.example.shopapp.presentation.viewmodels.LoginFactoryViewModel
import com.example.shopapp.presentation.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : BaseFragment<FragmentLoginBinding>(), View.OnClickListener {
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginFactoryViewModel: LoginFactoryViewModel
    private lateinit var checkLogin: CheckLogin

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLogin = CheckLogin()
        loginFactoryViewModel = LoginFactoryViewModel(checkLogin)
        viewModel = ViewModelProvider(this, loginFactoryViewModel).get(LoginViewModel::class.java)

        binding.tvRegister.setOnClickListener(this)

        binding.btLogin.setOnClickListener(this)

        binding.tvForgetPass.setOnClickListener(this)

        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect {
                event->
                when(event) {
                    is LoginViewModel.LoginInEvent.Success -> {
                        logInRegisterUser()
                    }
                    is LoginViewModel.LoginInEvent.ErrorLoginIn -> {
                        errorSnackBar(event.error, true)
                        if(event.error == requireContext().getString(R.string.checkedEmail)) {
                            binding.etEmail.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedPassword)) {
                            binding.etPassword.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedEmailCorrect)) {
                            binding.etEmail.error = event.error
                        }
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.tvForgetPass -> {
                  findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
                }

                R.id.btLogin -> {
                  viewModel.validLoginDetails(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                }

                R.id.tvRegister -> {
                    findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
                }
            }
        }
    }

    private fun logInRegisterUser() {
        showProgressDialog("Please wait ...")

        val email = binding.etEmail.text.toString().trim { it <= ' ' }
        val password = binding.etPassword.text.toString().trim { it <= ' ' }

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        hideProgressDialog()

                        if (task.isSuccessful) {
                            errorSnackBar("You are logged successfully", false)
                        } else {
                            errorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }.await()
            }
        }
    }
}