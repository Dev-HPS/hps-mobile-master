package com.hastaprimasolusi.rana.ui.produk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukLoadMoreAdapter
import com.hastaprimasolusi.rana.data.network.response.produk.ProdukModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import kotlinx.android.synthetic.main.fragment_produk.*
import org.jetbrains.anko.support.v4.startActivity
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class ProdukFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
    private val listData = mutableListOf<ProdukModel?>()
    private lateinit var adapter: ProdukLoadMoreAdapter
    private var limit = 20
    private var offset = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.categorySelected?.cATEGORYNAME
        return inflater.inflate(R.layout.fragment_produk, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ProdukLoadMoreAdapter(listData, {
            viewModel.produkDetailModel.postValue(null)
            startActivity<ProdukDetailActivity>("model" to it)
        }, {

        })
        recyclerProduk.layoutManager = GridLayoutManager(activity, 2)
        recyclerProduk.itemAnimator = DefaultItemAnimator()
        recyclerProduk.adapter = adapter
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.listProdukCategory.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            listData.addAll(it)
            offset += limit
            adapter.notifyDataSetChanged()
        })

        viewModel.loadingProduk.observe(viewLifecycleOwner, Observer {
            progress.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.getProdukByCategori(limit.toString(), offset.toString())
    }
}