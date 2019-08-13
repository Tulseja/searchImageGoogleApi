package com.doitunzo.imageSearch.utils

import com.android.volley.VolleyError

data class Resource<ResultType>(var status : Status, var data: ResultType? = null, var error: VolleyError? = null, var isPaginatedLoading: Boolean = false) {

    companion object {
        /**
         * Creates [Resource] with [Status.SUCCESS] and [data]
         */
        fun <ResultType> success(data: ResultType, isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(
            Status.SUCCESS,data)

        /**
         * Creates [Resource] with [Status.LOADING] to notify UI to load
         */
        fun <ResultType> loading(isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(Status.LOADING, isPaginatedLoading = isPaginatedLoading)

        fun <ResultType> error(apiError: VolleyError?, isPaginatedLoading: Boolean = false): Resource<ResultType> = Resource(
            Status.ERROR, error = apiError)
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING;

    /**
     * Returns `true` if the [Status] is success else `false`.
     */
    fun isSuccessful() = this == SUCCESS

    /**
     * Returns `true` if the [Status] is loading else `false`.
     */
    fun isLoading() = this == LOADING
}