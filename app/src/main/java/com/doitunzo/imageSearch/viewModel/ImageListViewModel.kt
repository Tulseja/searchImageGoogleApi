package com.doitunzo.imageSearch.viewModel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.doitunzo.imageSearch.repo.Repository
import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.utils.Resource
import com.doitunzo.imageSearch.utils.Status


class ImageListViewModel(private val repository: Repository) : ViewModel() {


    val _pageNumLiveData = MutableLiveData<String>()
    private var query : String = ""

    fun fetchImages(pageNum : Int , query : String){
        this.query = query
        _pageNumLiveData.value = pageNum.toString()
    }

    val imageListLiveData = Transformations.switchMap(_pageNumLiveData) {
        val result = repository.getImageList(it , query)
        return@switchMap Transformations.map(result) { mapToUi(it) }
    }


    fun mapToUi(result: Resource<Image>): Resource<Image> {
        when (result.status) {
            Status.SUCCESS -> {
                /*if(result.data?.isNotEmpty() == true)*/
                return Resource.success(result.data!!,result.isPaginatedLoading)
            }

            Status.LOADING -> {
                return Resource.loading(result.isPaginatedLoading)
            }

            Status.ERROR -> {
                return Resource.error(result.error,result.isPaginatedLoading)
            }
        }
    }



}
