package com.doitunzo.imageSearch.ui


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_LONG
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.doitunzo.imageSearch.R
import com.doitunzo.imageSearch.URL_FULL_SCREEN
import kotlinx.android.synthetic.main.full_screen_image_activity.*

class FullScreenImageActivity : AppCompatActivity() {



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
//            Picasso.with(this).load(url).into(full_iv)
    }
}