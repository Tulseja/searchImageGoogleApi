package com.doitunzo.imageSearch.ui

import android.annotation.SuppressLint
import android.support.constraint.Group
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.doitunzo.imageSearch.R
import com.doitunzo.imageSearch.responsePOJO.Items
import com.doitunzo.imageSearch.utils.PaginationAdapter

import com.bumptech.glide.request.RequestOptions
import com.doitunzo.imageSearch.MORE_CLICKED_EVENT
import com.doitunzo.imageSearch.utils.hide
import com.doitunzo.imageSearch.utils.show


class HomeImageAdapter(val clickListener: ItemClickListener) : PaginationAdapter<Items>() {

    var recyclerView : RecyclerView  ? = null


    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_image_list_item, parent, false)
        return ImageViewHolder(view,clickListener)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(dataList[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isNotEmpty()) when (payloads[0]) {
            MORE_CLICKED_EVENT -> {
                if(holder is ImageViewHolder){
                    holder.bindWidthHeightVisibility(dataList[position])
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun addLoadingViewFooter() {
        recyclerView?.post{
            addLoadingViewFooter(Items())
        }
    }

    fun updateData(itemList : List<Items>){
        val currentSize = itemCount
        dataList.addAll(itemList)
        notifyItemRangeInserted(currentSize, itemList.size)
    }


     class ImageViewHolder(itemView: View, val clickListener : ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        val context by lazy { itemView.context }
        val displayIv = itemView.findViewById<ImageView>(R.id.display_iv)
        val moreView  = itemView.findViewById<View>(R.id.sep_view)
        val widthValueTv  = itemView.findViewById<TextView>(R.id.width_value_tv)
        val heightValueTv  = itemView.findViewById<TextView>(R.id.height_value_tv)
         val moreGrp = itemView.findViewById<Group>(R.id.more_grp)
         val widthHeightGrp = itemView.findViewById<Group>(R.id.width_height_grp)


        @SuppressLint("CheckResult")
        fun bind(item: Items) {

            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_launcher_background)
            requestOptions.error(R.drawable.ic_launcher_background)
            val uri = item.pagemap?.cse_thumbnails?.get(0)?.src ?: item.pagemap?.cse_images?.get(0)?.src

            Glide.with(displayIv).applyDefaultRequestOptions(requestOptions).load(uri).into(displayIv)
            displayIv.setOnClickListener {
                clickListener.onImageClicked(uri)
            }
            moreView.setOnClickListener {
                clickListener.onMoreClicked(adapterPosition)
            }
            moreGrp.show()
            widthHeightGrp.hide()
        }

         fun bindWidthHeightVisibility(item : Items){
             moreGrp.hide()
             widthHeightGrp.show()
             widthValueTv.text = item.pagemap?.cse_thumbnails?.get(0)?.width ?: context.getString(R.string.na)
             heightValueTv.text = item.pagemap?.cse_thumbnails?.get(0)?.height?: context.getString(R.string.na)
         }
    }

    interface ItemClickListener{
        fun onMoreClicked(position : Int)
        fun onImageClicked(url : String?)
    }
}