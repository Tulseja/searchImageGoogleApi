package com.doitunzo.imageSearch.responsePOJO

import com.google.gson.annotations.SerializedName


data class Image(@SerializedName("items") var items : List<Items>? = null)

data class Items(@SerializedName("pagemap") var pagemap : PageMap ? = null)

data class PageMap(@SerializedName("cse_thumbnail") var cse_thumbnails : List<ImageDetails> ? = null ,
                   @SerializedName("cse_image") var cse_images : List<ImageDetails> ? = null )


data class ImageDetails(@SerializedName("width") var width  : String ? = null ,
                        @SerializedName("height") var height  : String ? = null ,
                        @SerializedName("src") var src  : String ? = null)



