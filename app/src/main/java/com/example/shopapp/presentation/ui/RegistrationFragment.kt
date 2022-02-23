package com.example.shopapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.shopapp.R
import com.example.shopapp.data.firestore.FireStore
import com.example.shopapp.databinding.FragmentRegistrationBinding
import com.example.shopapp.domain.models.Users
import com.example.shopapp.domain.use_cases.CheckRegistration
import com.example.shopapp.presentation.base.BaseFragment
import com.example.shopapp.presentation.viewmodels.RegistrationFactoryViewModel
import com.example.shopapp.presentation.viewmodels.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var viewModelFactory: RegistrationFactoryViewModel
    private lateinit var checkRegistration: CheckRegistration


    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRegistrationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkRegistration = CheckRegistration()
        viewModelFactory = RegistrationFactoryViewModel(checkRegistration)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegistrationViewModel::class.java)


        binding.tvLogin.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivLeft.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btRegister.setOnClickListener {
            viewModel.validRegisterDetails(binding.etFirstName.text.toString(), binding.etLastName.text.toString(),
                binding.etEmailID.text.toString(), binding.etPassword.text.toString(), binding.etConfirm.text.toString(), binding.checkBox.isChecked
                )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.registrationEvent.collect {
                event->
                when(event) {
                    is RegistrationViewModel.RegistrationInEvent.Success -> {
                      registerUser()
                    }
                    is RegistrationViewModel.RegistrationInEvent.ErrorRegistrationIn -> {
                        errorSnackBar(event.error, true)
                        if (event.error == requireContext().getString(R.string.checkedName)){
                            binding.etFirstName.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedLatName)) {
                            binding.etLastName.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedEmail)) {
                            binding.etEmailID.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedPassword)) {
                            binding.etPassword.error = event.error
                        }
                        if(event.error == requireContext().getString(R.string.checkedConfirm)) {
                            binding.etConfirm.error = event.error
                        }
                    }
                }
            }
        }
    }




    private fun registerUser() {
        val email = binding.etEmailID.text.toString().trim { it <= ' ' }
        val password = binding.etPassword.text.toString().trim { it <= ' ' }

                showProgressDialog("Please wait...")

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                      //  hideProgressDialog()

                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = Users(
                                firebaseUser.uid,
                                binding.etFirstName.text.toString(),
                                binding.etLastName.text.toString(),
                                binding.etEmailID.text.toString(),

                            )

                             FireStore().registerUser(this@RegistrationFragment, user)

                        //    FirebaseAuth.getInstance().signOut()
                       //     findNavController().popBackStack()

                        } else {
                            hideProgressDialog()
                            errorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }.await()
            }
        }
    }

     fun userRegistrationSuccessful() {
        hideProgressDialog()

        toast("You are registered successfully")
    }



}
