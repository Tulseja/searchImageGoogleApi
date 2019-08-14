package com.doitunzo.imageSearch.ui

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.support.v7.app.AppCompatActivity
import com.doitunzo.imageSearch.R
import kotlinx.android.synthetic.main.search_activity.*

class SearchActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        setListeners()
    }

    private fun setListeners(){
        search_tv.setOnClickListener {
            if(search_et.text.toString().isEmpty()){
                Snackbar.make(parent_lyt,getString(R.string.valid_query), LENGTH_SHORT).show()
            } else ImageSearchResultsActivity.startActivity(this,search_et.text.toString())
        }
    }



}