package com.doitunzo.imageSearch.utils

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.doitunzo.imageSearch.repo.Repository
import com.doitunzo.imageSearch.viewModel.ImageListViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(
        private val application: Application,
        private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(ImageListViewModel::class.java) -> ImageListViewModel(repository)
                    else -> throw IllegalArgumentException("Unknown viewmodel class ${modelClass.name}")
                }
            } as T

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) = INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE
                            ?: ViewModelFactory(
                                    application, Injection.provideRepository(application)
                            )
                                    .also { INSTANCE = it }
                }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}