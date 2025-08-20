package com.hastaprimasolusi.rana.ui.lp.kembalibarang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ProdukKembaliStokAdapter
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.fragment_daftar_pengembalian.*
import kotlinx.android.synthetic.main.fragment_daftar_pengembalian.recyclerOpname
import kotlinx.android.synthetic.main.fragment_daftar_pengembalian.swipe
import org.koin.android.ext.android.inject

/**
 * Created by maasrahman on 21/09/20.
 */
class DaftarPengembalianFragment: Fragment() {
    private val viewModel: LpViewModel by inject()
    private lateinit var adapter: ProdukKembaliStokAdapter
    private val progress = ProgDialog().getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.listProdukKembali.postValue(emptyList())
        (activity as AppCompatActivity).supportActionBar?.title = "Pengembalian Produk"
        return inflater.inflate(R.layout.fragment_daftar_pengembalian, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ProdukKembaliStokAdapter {

        }
        recyclerOpname.layoutManager = LinearLayoutManager(activity)
        recyclerOpname.itemAnimator = DefaultItemAnimator()
        recyclerOpname.adapter = adapter
        recyclerOpname.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && btnKonfirmasi.visibility == View.VISIBLE) {
                    btnKonfirmasi.hide()
                } else if (dy < 0 && btnKonfirmasi.visibility != View.VISIBLE) {
                    btnKonfirmasi.show()
                }
            }
        })
        btnKonfirmasi.setOnClickListener {
            konfirmasi()
        }

        initViewModel()
    }

    private fun konfirmasi(){
        viewModel.konfirmasiPengembalian {
            showalertInformation(activity!!, it){
                activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun initViewModel(){
        viewModel.loadingStok.observe(viewLifecycleOwner, Observer {
            swipe.isRefreshing = it
        })

        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            if(it) progress.show(activity!!) else progress.dismiss()
        })

        viewModel.listProdukKembali.observe(viewLifecycleOwner, Observer {
            adapter.updateData(it)
        })

        swipe.setOnRefreshListener {
            viewModel.getPengembalianProduk()
        }

        viewModel.getPengembalianProduk()
    }


}