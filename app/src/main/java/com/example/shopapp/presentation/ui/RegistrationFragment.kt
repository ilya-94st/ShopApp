package com.example.shopapp.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.shopapp.databinding.FragmentRegistrationBinding
import com.example.shopapp.presentation.base.BaseFragment


class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentRegistrationBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}