package com.hastaprimasolusi.rana.adapter.tab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created By maasrahman on 6/1/20
 */
class KategoriTabAdapter(fm: FragmentManager, lifeCycle: Lifecycle, private val fragList: List<Fragment>, private val fragTitle: List<String>)
    : FragmentStateAdapter(fm, lifeCycle) {

    override fun getItemCount(): Int = fragList.size

    override fun createFragment(position: Int): Fragment = fragList[position]

}