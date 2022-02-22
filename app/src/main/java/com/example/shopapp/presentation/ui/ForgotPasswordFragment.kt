package com.example.shopapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentForgotPasswordBinding
import com.example.shopapp.domain.use_cases.CheckForgotPassword
import com.example.shopapp.presentation.base.BaseFragment
import com.example.shopapp.presentation.viewmodels.ForgotPasswordFactoryViewModel
import com.example.shopapp.presentation.viewmodels.ForgotPasswordViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {
    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var factory: ForgotPasswordFactoryViewModel
    private lateinit var checkForgotPassword: CheckForgotPassword

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentForgotPasswordBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkForgotPassword = CheckForgotPassword()
        factory = ForgotPasswordFactoryViewModel(checkForgotPassword)
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btSubmit.setOnClickListener {
            viewModel.validEmailDetails(binding.etEmail.text.toString())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.emailEvent.collect {
                    event->
                when(event) {
                    is ForgotPasswordViewModel.ForgotPasswordInEvent.Success -> {
                          checkSendPasswordResetEmail()
                    }
                    is ForgotPasswordViewModel.ForgotPasswordInEvent.ErrorForgotPasswordIn -> {
                        errorSnackBar(event.error, true)
                        if(event.error == requireContext().getString(R.string.checkedEmail)) {
                            binding.etEmail.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedEmailCorrect)) {
                            binding.etEmail.error = event.error
                        }
                    }
                }
            }
        }
    }

private fun checkSendPasswordResetEmail() {
    showProgressDialog("Please wait ...")

    val email = binding.etEmail.text.toString().trim { it <= ' ' }

    CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->

                    hideProgressDialog()

                    if (task.isSuccessful) {
                        errorSnackBar("You are logged successfully", false)
                        findNavController().popBackStack()
                    } else {
                        errorSnackBar(task.exception!!.message.toString(), true)
                    }
                }.await()
        }
    }
}
}