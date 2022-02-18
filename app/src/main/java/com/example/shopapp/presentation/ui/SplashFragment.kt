package com.example.shopapp.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentSplashBinding
import com.example.shopapp.presentation.base.BaseFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSplashBinding::inflate

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
     //       requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())
     //   } else {
     //       requireActivity().window.setFlags(
    //            WindowManager.LayoutParams.FLAG_FULLSCREEN,
   //             WindowManager.LayoutParams.FLAG_FULLSCREEN
      //      )
     //   }

        GlobalScope.launch {
            delay(2000)
            activity?.runOnUiThread {
                findNavController().navigate(R.id.action_splashFragment_to_registrationFragment)
            }
        }
    }
}