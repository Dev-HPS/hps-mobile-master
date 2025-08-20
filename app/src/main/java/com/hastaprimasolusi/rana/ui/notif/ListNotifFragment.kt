package com.hastaprimasolusi.rana.ui.notif

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.hastaprimasolusi.rana.R
import com.hastaprimasolusi.rana.adapter.page.NotificationAdapter
import com.hastaprimasolusi.rana.data.MessageDao
import com.hastaprimasolusi.rana.data.local.MessageModel
import com.hastaprimasolusi.rana.ui.canvasser.CanvasViewModel
import com.hastaprimasolusi.rana.ui.canvasser.riwayat.RiwayatDetailCanvasFragment
import com.hastaprimasolusi.rana.ui.lp.LpViewModel
import com.hastaprimasolusi.rana.ui.lp.order.OrderDetailFragment
import com.hastaprimasolusi.rana.ui.mitra.MitraViewModel
import com.hastaprimasolusi.rana.ui.mitra.riwayat.DetailRiwayatActivity
import com.hastaprimasolusi.rana.utils.UtilsPref
import kotlinx.android.synthetic.main.fragment_list_notif.*
import org.koin.android.ext.android.inject

/**
 * Created By maasrahman on 6/23/20
 */
class ListNotifFragment: Fragment() {
    private val dao: MessageDao by inject()
    private lateinit var adapter: NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.notifikasi)
        return inflater.inflate(R.layout.fragment_list_notif, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = NotificationAdapter {
            jumpToHistory(it)
        }
        recyclerNotif.layoutManager = LinearLayoutManager(activity)
        recyclerNotif.itemAnimator = DefaultItemAnimator()
        recyclerNotif.adapter = adapter

        when(UtilsPref.getUserRole()){
            "local_partner" -> {
                val viewModel: LpViewModel by inject()
                viewModel.getNotif().observe(viewLifecycleOwner, Observer {
                    layoutNoData.visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE
                    adapter.updateData(it)
                })
            }
            "canvasser", "spg", "msr" -> {
                val viewModel: CanvasViewModel by inject()
                viewModel.getNotif().observe(viewLifecycleOwner, Observer {
                    layoutNoData.visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE
                    adapter.updateData(it)
                })
            }
            else -> {
                val viewModel: MitraViewModel by inject()
                viewModel.getNotif().observe(viewLifecycleOwner, Observer {
                    layoutNoData.visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE
                    adapter.updateData(it)
                })
            }
        }
    }

    private fun jumpToHistory(model: MessageModel){
        when(UtilsPref.getUserRole()){
            "local_partner" -> {
                val frag = OrderDetailFragment()
                val bundle = Bundle()
                bundle.putString("id", model.id)
                frag.arguments = bundle
                replaceFragment(frag)
            }
            "canvasser", "spg", "msr" -> {
                val frag = RiwayatDetailCanvasFragment()
                val bundle = Bundle()
                bundle.putString("id", model.id)
                frag.arguments = bundle
                replaceFragment(frag)
            }
            else -> {
                var intent = Intent(activity!!, DetailRiwayatActivity::class.java)
                intent.putExtra("id", model.id)
                startActivity(intent)
            }
        }
    }

    private fun replaceFragment(frag: Fragment){
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frame, frag)
            ?.addToBackStack(null)
            ?.commit()
    }
}