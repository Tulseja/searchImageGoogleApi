package com.doitunzo.imageSearch.repo

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import android.webkit.URLUtil
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.doitunzo.imageSearch.R
import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.utils.Resource
import com.google.gson.Gson
import com.doitunzo.imageSearch.utils.CustomHttpRequest

import com.android.volley.Request
import com.doitunzo.imageSearch.CUSTOM_SEARCH_API_KEY
import com.doitunzo.imageSearch.CUSTOM_SEARCH_CX_KEY
import com.doitunzo.imageSearch.utils.HttpResponseListener


class RemoteDataSource(private val appContext: Context){

    companion object {
        fun getInstance(appContext: Application): RemoteDataSource {
            return RemoteDataSource(appContext)
        }
    }

    fun loadImages(pageNum : String , query : String)  : LiveData<Resource<Image>> {
        val data = MutableLiveData<Resource<Image>>()
        data.value = Resource.loading(isPaginationState(pageNum.toInt()))

        var url = "https://www.googleapis.com/customsearch/v1?q=$query&cx=$CUSTOM_SEARCH_CX_KEY&key=$CUSTOM_SEARCH_API_KEY"
        url+="&page=$pageNum"



        if (!URLUtil.isValidUrl(url)) {
            data.value = Resource.error(getInvalidUrlError(),isPaginationState(pageNum.toInt()))
        }
        val queue = Volley.newRequestQueue(appContext)

        /*val getRequest: StringRequest = object : StringRequest(Method.GET, url, Response.Listener {
            // response
            val gson = Gson()
//            val imageListType = object : TypeToken<List<Image>>() {}.type
            val imgResponse : Image = gson.fromJson(it, Image::class.java)
            Log.d("AK",it)
            data.value = Resource.success(imgResponse,isPaginationState(pageNum.toInt()))

        }, Response.ErrorListener {
            data.value = Resource.error(it , isPaginationState(pageNum.toInt()))
        }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String?> {
                val header = HashMap<String,String>()
                header["Authorization"] = "Client-ID be119b83c0e66e83832a6dd5bce8e76ea6639e35850e25909d1b1d95d55be930"
                return header
            }
        }*/
        val request = CustomHttpRequest(
            Request.Method
                .GET, url, 1, null, null, Image::class.java, object : HttpResponseListener {
                override fun onResponseSuccess(response: Any?, requestID: Int) {
                    if(response is Image){
                        data.value = Resource.success(response,isPaginationState(pageNum.toInt()))
                    }
                }

                override fun onResponseError(error: VolleyError?, requestID: Int) {
                    data.value = Resource.error(error,isPaginationState(pageNum.toInt()))
                }

            }
        )
//        not creating singleton here just for one request
        Volley.newRequestQueue(appContext).add<Image>(request)
        queue.add(request)

        return data

    }

    private fun isPaginationState(pageNum: Int) = pageNum > 1

    private fun getInvalidUrlError() = VolleyError(appContext.getString(R.string.msg_invalid_url))

    private fun getNoInternetError() = VolleyError(appContext.getString(R.string.no_connection))


}