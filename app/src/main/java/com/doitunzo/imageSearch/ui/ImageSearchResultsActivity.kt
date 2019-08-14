package com.doitunzo.imageSearch.ui

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import com.doitunzo.imageSearch.*
import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.responsePOJO.Items
import com.doitunzo.imageSearch.utils.*
import com.doitunzo.imageSearch.viewModel.ImageListViewModel

import kotlinx.android.synthetic.main.image_search_result_lyt.*
import kotlinx.android.synthetic.main.generic_error_retry.*


class ImageSearchResultsActivity :  AppCompatActivity()  , HomeImageAdapter.ItemClickListener {
    override fun onMoreClicked(position: Int) {
        mHomeAdapter?.notifyItemChanged(position,MORE_CLICKED_EVENT)
    }

    override fun onImageClicked(url: String?) {
        if(!TextUtils.isEmpty(url)) {
            val intent = Intent(this, FullScreenImageActivity::class.java).apply {
                putExtra(URL_FULL_SCREEN, url)
            }
            startActivity(intent)
        } else
            Snackbar.make(parent_lyt,getString(R.string.generic_error_string), LENGTH_SHORT).show()

    }


    companion object {
        fun startActivity(activity : Activity, query : String){
            val intent = Intent(activity,ImageSearchResultsActivity::class.java).apply {
                putExtra(QUERY_STRING,query)
            }
            activity.startActivity(intent)
        }
    }


    lateinit var viewModel: ImageListViewModel
    private var mHomeAdapter : HomeImageAdapter? = null
    private var pageNum = 1
    private var isLoading = false
    private var isLastPage = false
    private var query : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_search_result_lyt)
        query = intent?.getStringExtra(QUERY_STRING) ?: ""
        initializeRecyclerView()
        setListeners()
        observeViewModel()
        supportActionBar?.hide()
    }

    private fun setListeners(){
        tv_retry.setOnClickListener {
            viewModel.fetchImages(INITIAL_PAGE_NUM , query)
        }
    }

    private fun initializeRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        homeRv.layoutManager = layoutManager
        mHomeAdapter = HomeImageAdapter(this)
        homeRv.adapter = mHomeAdapter
        homeRv.addOnScrollListener(OnScrollListener(layoutManager))
    }

    private fun observeViewModel(){
        viewModel = obtainViewModel(ImageListViewModel::class.java)

        viewModel.fetchImages(INITIAL_PAGE_NUM , query)

        viewModel.imageListLiveData.observe(this, Observer {
            when(it?.status){
                Status.ERROR ->{
                    isLoading = false
                    if(it.isPaginatedLoading){
                        mHomeAdapter?.removeLoadingViewFooter()
                        Snackbar.make(parent_lyt,getString(R.string.generic_error_string),LENGTH_SHORT).show()
                    } else {
                        errorLyt.show()
                        homeRv.hide()
                        progress_bar.hide()
                    }
                }
                Status.SUCCESS -> {
                    isLoading = false
                    if(it.isPaginatedLoading){
                        mHomeAdapter?.removeLoadingViewFooter()
                    } else {
                        homeRv.show()
                        progress_bar.hide()
                        errorLyt.hide()
                    }
                    if(it.data?.items?.isNotEmpty() == true)
                        bindView(it.data?.items!!)
                }
                Status.LOADING -> {
                    isLoading = true
                    if(it.isPaginatedLoading){
                        mHomeAdapter?.addLoadingViewFooter()
                    } else {
                        progress_bar.show()
                        errorLyt.hide()
                        homeRv.hide()
                    }
                }
            }
        })
    }

    private fun bindView(itemList : List<Items>){
        if(itemList.size < 10){
            isLastPage = true
        }
        mHomeAdapter?.updateData(itemList)
    }

    private fun loadNextPage(){
        pageNum++
        viewModel.fetchImages(pageNum , query)
    }

    inner class OnScrollListener(layoutManager: LinearLayoutManager) : PaginationScrollListener(layoutManager) {
        override fun isLoading() = isLoading
        override fun loadMoreItems() = loadNextPage()
        override fun isLastPage() = isLastPage
    }


}