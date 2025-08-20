package com.hastaprimasolusi.rana.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.SliderAdapterExample.SliderAdapterVH
import com.hastaprimasolusi.rana.data.local.SliderItem
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout_item.view.*
import kotlin.properties.Delegates

/**
 * Created by maasrahman on 16/09/20.
 */
class SliderAdapterExample : SliderViewAdapter<SliderAdapterVH>() {

    private var listData: List<SliderItem> by Delegates.observable(emptyList()){_, _, _ ->
        notifyDataSetChanged()
    }

    fun updateData(list: List<SliderItem>){
        listData = list
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH = SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null))

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) = viewHolder.bindItem(listData[position])

    override fun getCount(): Int = listData.size

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        fun bindItem(model: SliderItem){
            Glide.with(itemView)
                .load(model.imageUrl)
                .apply(RequestOptions().error(R.drawable.no_image))
                .into(itemView.imgSlider)

        }
    }

}