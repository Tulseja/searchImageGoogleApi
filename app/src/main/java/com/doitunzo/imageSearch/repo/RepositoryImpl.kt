package com.doitunzo.imageSearch.repo

import android.arch.lifecycle.LiveData
import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.utils.Resource


class RepositoryImpl(private val remoteDataSource: RemoteDataSource) : Repository {

    override fun getImageList(pageNum: String , query : String) : LiveData<Resource<Image>> {
        return remoteDataSource.loadImages(pageNum , query)
    }


}