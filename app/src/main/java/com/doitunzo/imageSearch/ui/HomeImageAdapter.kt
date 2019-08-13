package com.doitunzo.imageSearch.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.doitunzo.imageSearch.R
import com.doitunzo.imageSearch.responsePOJO.Image
import com.doitunzo.imageSearch.utils.PaginationAdapter



class HomeImageAdapter(val clickListener: ItemClickListener) : PaginationAdapter<Image>() {
    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_image_list_item, parent, false)
        return ImageViewHolder(view,clickListener)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(dataList[position])
        }
    }

    override fun addLoadingViewFooter() {
        addLoadingViewFooter(Image())
    }

    fun updateData(contestList : List<Image>){
        val currentSize = itemCount
        dataList.addAll(contestList)
        notifyItemRangeInserted(currentSize, contestList.size)
    }



     class ImageViewHolder(itemView: View, val clickListener : ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val context by lazy { itemView.context }
        val displayIv = itemView.findViewById<ImageView>(R.id.display_iv)


        fun bind(image: Image) {
            //TODO
            /*ImageLoader.getInstance(context.applicationContext).loadImage(image.url?.fullUrl , displayIv ,R.mipmap.ic_launcher)*/
            itemView.setOnClickListener {
                if(image.url?.fullUrl != null)
                    clickListener.onItemSelected(image.url?.fullUrl!!)
            }
        }
    }

    interface ItemClickListener{
        fun onItemSelected(url : String)
    }
}