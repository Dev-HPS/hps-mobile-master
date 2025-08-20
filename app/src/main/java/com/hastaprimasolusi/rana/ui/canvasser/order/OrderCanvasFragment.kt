package com.hastaprimasolusi.rana.ui.canvasser.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.tab.KategoriTabAdapter
import com.hastaprimasolusi.rana.data.network.response.order.CartProdukModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.UtilsPref
import com.hastaprimasolusi.rana.utils.convertCurrency
import com.hastaprimasolusi.rana.utils.convertCurrencyNo
import kotlinx.android.synthetic.main.fragment_order_canvas.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/1/20
 */
class OrderCanvasFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var tabAdapter: KategoriTabAdapter
    private val listFragment = mutableListOf<Fragment>()
    private val listTitle = mutableListOf<String>()
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.orderbarang)
        return inflater.inflate(R.layout.fragment_order_canvas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnDetail.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.frame, DetailOrderFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.listKategoriLP.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                layoutNoData.visibility = View.VISIBLE
                return@Observer
            }
            listFragment.clear()
            listTitle.clear()
            layoutNoData.visibility = View.GONE
            it.forEach { kat ->
                val frag = DaftarProdukCnvsFragment()
                val bundle = Bundle()
                bundle.putParcelable("model", kat)
                frag.arguments = bundle
                listFragment.add(frag)
                listTitle.add(kat.cATEGORYNAME.toString())
            }

            tabAdapter = KategoriTabAdapter(childFragmentManager, lifecycle, listFragment, listTitle)
            viewPager.adapter = tabAdapter
            viewPager.offscreenPageLimit = listFragment.size
            TabLayoutMediator(tabLayout, viewPager){ tab, position ->
                tab.text = listTitle[position]
                viewPager.setCurrentItem(tab.position, true)
            }.attach()
        })

        viewModel.loadingKategori.observe(viewLifecycleOwner, Observer {
            if(it){
                progress.show(activity!!)
            }else{
                progress.dismiss()
            }
        })

        viewModel.listCart.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                layoutSummary.visibility = View.GONE
            }else{
                layoutSummary.visibility = View.VISIBLE
                updateSummary(it)
            }
        })

        if(viewModel.listKategoriLP.value == null){
            viewModel.getKategoriLP()
        }
    }

    private fun updateSummary(data: List<CartProdukModel>){
        var jml = 0
        var jmlItem = 0
        data.forEach {
            jml = it.tOTALAMT.toString().toIntOrNull() ?: jml + (it.pRODQTY.toString().toInt() * it.pRODPRICE.toString().toInt())
            jmlItem += it.tOTALQTY.toString().toIntOrNull() ?: it.pRODQTY.toString().toInt()
        }
        txtJmlItem.text = convertCurrencyNo(jmlItem.toString(), 3, '.')
        txtTotal.text = convertCurrency(jml.toString(), 3, '.', UtilsPref.loadString(getString(R.string.currencySymbol)))
    }
}