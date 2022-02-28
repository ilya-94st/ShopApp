package com.example.shopapp.domain.use_cases

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader {

    fun loadUserPicture(imageUri: Uri, imageView: ImageView, context: Context) {
        try {
            Glide.with(context).load(imageUri)
                .centerCrop()
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}