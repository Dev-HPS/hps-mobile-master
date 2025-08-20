package com.hastaprimasolusi.rana.ui.canvasser.penjualan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hastaprimasolusi.rana.R
import kotlinx.android.synthetic.main.fragment_pos_waiting.*
import java.util.*


/**
 * Created By maasrahman on 6/7/20
 */
class PosWaitingFragment: Fragment() {
    private var count = 1
    val t = Timer()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pos_waiting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                txtCount.text = count.toString()
                count += 1
            }
        }, 1000, 1000)

        btnSelesai.setOnClickListener {
            t.cancel()
            t.purge()
            activity?.supportFragmentManager?.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        t.cancel()
        t.purge()
    }
}