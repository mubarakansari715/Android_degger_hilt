package com.mubarak.android_degger_hilt.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.mubarak.android_degger_hilt.R
import com.squareup.picasso.Picasso

object ImageLoading {
    @JvmStatic
    @BindingAdapter("android:loadImage")

    fun ImageView.loadImage(url: String?) {
        if (url != null && url.isNotEmpty()) {
            /* Glide.with(this.context)
                 .load(url)
                 .error(R.drawable.ic_launcher_background)
                 .into(this)*/
            Picasso.get()
                .load(url)
                .error(R.drawable.ic_launcher_background)
                .into(this)
        } else {
            Glide.with(this.context).load(R.drawable.ic_launcher_background).into(this)

        }
    }
}
