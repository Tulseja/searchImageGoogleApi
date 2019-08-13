package com.doitunzo.imageSearch.utils

import android.app.Application
import com.doitunzo.imageSearch.repo.RemoteDataSource
import com.doitunzo.imageSearch.repo.Repository
import com.doitunzo.imageSearch.repo.RepositoryImpl

object Injection {

    fun provideRepository(app: Application): Repository {
        val remoteDataSource = provideChannelsRemoteDataSource(app)

        return RepositoryImpl(remoteDataSource)
    }

    private fun provideChannelsRemoteDataSource(app: Application): RemoteDataSource = RemoteDataSource.getInstance(app)



}