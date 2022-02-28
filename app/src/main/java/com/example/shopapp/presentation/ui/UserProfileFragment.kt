package com.example.shopapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.shopapp.common.Constants
import com.example.shopapp.databinding.FragmentUserProfileBinding
import com.example.shopapp.domain.use_cases.CheckMobile
import com.example.shopapp.domain.use_cases.GlideLoader
import com.example.shopapp.presentation.base.BaseFragment
import com.example.shopapp.presentation.viewmodels.UserProfileFactory
import com.example.shopapp.presentation.viewmodels.UserProfileViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException


class UserProfileFragment : BaseFragment<FragmentUserProfileBinding>(),EasyPermissions.PermissionCallbacks  {
    private val args: UserProfileFragmentArgs by navArgs()
    private lateinit var glideLoader: GlideLoader
    private lateinit var checkMobile: CheckMobile
    private lateinit var userProfileFactory: UserProfileFactory
    private lateinit var viewModel: UserProfileViewModel

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentUserProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glideLoader = GlideLoader()
        checkMobile = CheckMobile()
        userProfileFactory = UserProfileFactory(checkMobile)
        viewModel = ViewModelProvider(this, userProfileFactory).get(UserProfileViewModel::class.java)
          getUsers()
        binding.ivUserPhoto.setOnClickListener {
            getPhotoPermission()
        }
    }

    private fun getUsers() {
        val users = args.users
        binding.etFirstName.isEnabled = false
        binding.etFirstName.setText(users.firstName)

        binding.etLastName.isEnabled = false
        binding.etLastName.setText(users.lastName)

        binding.etEmailID.isEnabled = false
        binding.etEmailID.setText(users.email)
    }

    private fun getPhotoPermission() {
        EasyPermissions.requestPermissions(
            this,
            "you need accept location permission to use this app",
            Constants.READ_STORAGE_PERMISSION_CODE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }




    private fun showImageChooser() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        val selectImageUri = data.data!!

                        glideLoader.loadUserPicture(selectImageUri, binding.ivUserPhoto, requireContext())

                    } catch (e: IOException) {
                        toast("image selected failed")
                    }
                }
            }
        } else if( requestCode == AppCompatActivity.RESULT_CANCELED) {
            toast("image result canceled")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(Constants.hasPhotoPermission(requireContext())){
            showImageChooser()
        }else{
            snackBar("No")
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this , perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }else {
            getPhotoPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults ,this)
    }

}