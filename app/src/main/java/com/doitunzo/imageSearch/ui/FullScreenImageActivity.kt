package com.doitunzo.imageSearch.ui


import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.doitunzo.imageSearch.R
import com.doitunzo.imageSearch.URL_FULL_SCREEN
import kotlinx.android.synthetic.main.full_screen_image_activity.*

class FullScreenImageActivity : Activity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_screen_image_activity)
        val url = intent?.getStringExtra(URL_FULL_SCREEN)
        if(!TextUtils.isEmpty(url))
            loadImage(url!!)
        else {
            Snackbar.make(full_lyt,getString(R.string.generic_error_string), LENGTH_LONG).show()
        }

    }

    private fun loadImage(url: String){
        Glide.with(full_iv).load(url).into(full_iv)
    }
}