package com.example.shopapp.common

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object Constants {
    const val USERS = "users"

    const val READ_STORAGE_PERMISSION_CODE = 2

    const val PICK_IMAGE_REQUEST_CODE = 1

    fun hasPhotoPermission(context: Context) = EasyPermissions.hasPermissions(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
}