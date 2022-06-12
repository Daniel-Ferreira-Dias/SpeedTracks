package com.cme.speedtrackers.classes

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cme.speedtrackers.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageV: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageV)
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
}