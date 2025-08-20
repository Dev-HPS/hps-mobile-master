package com.hastaprimasolusi.rana.ui.canvasser.kembalibarang

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
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.utils.showalertInformation
import kotlinx.android.synthetic.main.fragment_canvas_list_opname.*
import kotlinx.android.synthetic.main.fragment_canvas_list_opname.swipe
import org.koin.android.ext.android.inject

/**
 * Created by maasrahman on 20/09/20.
 */
class CanvasListOpnameFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private lateinit var adapter: ProdukKembaliStokAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.listProdukKembali.postValue(emptyList())
        (activity as AppCompatActivity).supportActionBar?.title = "Pengembalian Barang"
        return inflater.inflate(R.layout.fragment_canvas_list_opname, container, false)
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
                if (dy > 0 && btnSubmit.visibility == View.VISIBLE) {
                    btnSubmit.hide()
                } else if (dy < 0 && btnSubmit.visibility != View.VISIBLE) {
                    btnSubmit.show()
                }
            }
        })
        btnSubmit.setOnClickListener {
            konfirmasiPengembalian()
        }
        initViewModel()
    }

    private fun konfirmasiPengembalian(){
        viewModel.konfirmasiPengembalian {
            showalertInformation(activity!!, it){
                activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }

    private fun initViewModel(){
        viewModel.loadingProses.observe(viewLifecycleOwner, Observer {
            swipe.isRefreshing = it
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