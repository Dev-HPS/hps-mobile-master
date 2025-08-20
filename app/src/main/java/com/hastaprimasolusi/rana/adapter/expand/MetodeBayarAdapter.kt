package com.hastaprimasolusi.rana.adapter.expand

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.data.network.response.PayMethodModel

/**
 * Created By maasrahman on 2020-05-01
 */
class MetodeBayarAdapter(private val context: Context, private val listTitle: MutableList<String>,
                         private val listData: MutableMap<String, MutableList<PayMethodModel>>) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return listTitle[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val listTitle = getGroup(groupPosition) as String
        if (view == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.item_list_group, null)
        }
        val listTitleTextView = view!!.findViewById<TextView>(R.id.txtTitle)
        listTitleTextView.text = listTitle
        return view

    }

    override fun getChildrenCount(groupPosition: Int): Int {
       return listData[listTitle[groupPosition]]?.size ?: 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listData[listTitle[groupPosition]]?.get(childPosition) ?: PayMethodModel()
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var view = convertView
        val model = getChild(groupPosition, childPosition) as PayMethodModel
        if (view == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = layoutInflater.inflate(R.layout.item_list_paymethod, null)
        }
        val expandedListImage = view!!.findViewById<ImageView>(R.id.imgTipeBayar)
        val expandedListTextView = view.findViewById<TextView>(R.id.txtTipeBayar)
        expandedListTextView?.text = model.pAYMETHODNAME
        Glide.with(view)
            .load(model.pAYMETHODLOGO)
            .apply(RequestOptions().error(R.drawable.no_image))
            .into(expandedListImage)
        return view

    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listTitle.size
    }
}