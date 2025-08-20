package com.hastaprimasolusi.rana.ui.canvasser.penjualan.toko

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.ListTokoAdapter
import com.hastaprimasolusi.rana.data.network.response.canvas.ListTokoModel
import com.hastaprimasolusi.rana.helper.LocationHelper
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.canvasser.penjualan.CanvasPenjualanFragment
import com.hastaprimasolusi.rana.ui.common.ProgDialog
import com.hastaprimasolusi.rana.utils.showalertConfirmation
import kotlinx.android.synthetic.main.fragment_list_toko.*
import org.koin.android.ext.android.inject

/**
 * Created by maasrahman on 25/02/22.
 */
class ListTokoFragment: Fragment() {
    private val viewModel: CanvasViewModel by inject()
    private val prog = ProgDialog().getInstance()
    private lateinit var adapter: ListTokoAdapter
    private var tmpData = mutableListOf<ListTokoModel>()
    private lateinit var locationHelper: LocationHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_toko, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        locationHelper = LocationHelper(requireActivity())
        adapter = ListTokoAdapter { toko ->
            showalertConfirmation(requireContext(), "Konfirmasi pemilihan mitra ${toko.nAME}, lanjutkan?"){
                viewModel.selectedTokoJual = toko
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, CanvasPenjualanFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        recyclerToko.layoutManager = LinearLayoutManager(activity)
        recyclerToko.itemAnimator = DefaultItemAnimator()
        recyclerToko.adapter = adapter

        btnTambah.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, RegisterTokoFragment())
                .addToBackStack(null)
                .commit()
        }
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.loadingToko.observe(viewLifecycleOwner, Observer {
            if(it){
                prog.show(requireContext())
            }else{
                prog.dismiss()
            }
        })

        viewModel.listToko.observe(viewLifecycleOwner, Observer {
            if(it == null) return@Observer
            tmpData = it.toMutableList()
            adapter.updateData(tmpData)
        })

        etSearch.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()){
                    adapter.updateData(tmpData)
                }else if(p0.toString().length >= 3){
                    val listData = mutableListOf<ListTokoModel>()
                    tmpData.forEach { toko ->
                        if(toko.nAME?.toLowerCase()?.contains(p0.toString().toLowerCase()) == true){
                            listData.add(toko)
                        }
                    }
                    adapter.updateData(listData)
                }
            }
        })

        if(viewModel.listToko.value.isNullOrEmpty()){
            locationHelper.getLastKnownLocation { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val address = locationHelper.getAddressFromLocation(latitude, longitude)
                    viewModel.setLocationAddress(location, address)
                    viewModel.getListToko()

//                    Toast.makeText(requireContext(), "Latitude: $latitude, Longitude: $longitude, Address: $address", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                    viewModel.getListToko()
                }

//                showalertConfirmation(activity!!, getString(R.string.yakinmelanjutkan)) {
////                startActivityForResult(Intent(activity!!, QrScanActivity::class.java), 888)
//
//                    if (viewModel.userModel?.rOLENAME == "spg" || viewModel.userModel?.rOLENAME == "msr") {
//                        formSpg.visibility = View.VISIBLE
//                    } else {
//                        viewModel.confirmPos(viewModel.selectedTokoJual?.iD.toString()) {
//                            jualBerhasil()
//                        }
//                    }
//                }
            }
        }
    }
}