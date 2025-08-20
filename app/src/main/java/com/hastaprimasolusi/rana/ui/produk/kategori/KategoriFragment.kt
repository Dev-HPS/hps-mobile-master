package com.hastaprimasolusi.rana.ui.produk.kategori

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.KategoriAdapter
import com.hastaprimasolusi.rana.data.local.KategoriMenuModel
import com.hastaprimasolusi.rana.data.network.response.produk.CategoryModel
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.ui.produk.ProdukFragment
import kotlinx.android.synthetic.main.fragment_kategori.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 2020-04-26
 */
class KategoriFragment: Fragment() {
    private val viewModel: MitraViewModel by inject()
    private lateinit var adapter: KategoriAdapter
    private val tmpList = mutableListOf<CategoryModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.kategori)
        return inflater.inflate(R.layout.fragment_kategori, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter =
            KategoriAdapter{
                viewModel.listProdukCategory.value?.clear()
                viewModel.categorySelected = it
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.frame, ProdukFragment())
                    ?.addToBackStack(null)
                    ?.commit()
            }
        recyclerKategori.layoutManager = LinearLayoutManager(activity)
        recyclerKategori.itemAnimator = DefaultItemAnimator()
        recyclerKategori.adapter = adapter

        initViewModel()
    }

    private fun initViewModel(){
        viewModel.listCategorySemua.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            tmpList.clear()
            tmpList.addAll(it)
            adapter.updateData(it)
        })

        viewModel.loadingProduk.observe(viewLifecycleOwner, Observer {
            progress.visibility = if(it) View.VISIBLE else View.GONE
        })

        if(viewModel.listCategorySemua.value.isNullOrEmpty()){
            viewModel.getKategoryAll()
        }

        etCari.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){
                    val listKategori = tmpList.filter { row -> row.cATEGORYNAME?.toLowerCase()?.contains(s.toString().toLowerCase()) == true }
                    adapter.updateData(listKategori)
                }else{
                    adapter.updateData(tmpList)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }
}