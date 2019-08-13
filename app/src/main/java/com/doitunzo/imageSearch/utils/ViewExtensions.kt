package com.doitunzo.imageSearch.utils

import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.doitunzo.imageSearch.R



fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun ImageView.loadImage(uri: String?, @DrawableRes placeHolder: Int = R.drawable.ic_launcher_background) {
    Glide.with(this.context).load(uri).placeholder(placeHolder).into(this)
}