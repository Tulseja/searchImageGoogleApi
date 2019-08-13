package com.doitunzo.imageSearch.repo

import android.arch.lifecycle.LiveData

import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.utils.Resource


interface Repository {
    fun getImageList(pageNum : String , query : String)  : LiveData<Resource<Image>>
}