package com.hastaprimasolusi.rana.ui.bigbos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hastaprimasolusi.rana.R

/**
 * Created By maasrahman on 5/8/20
 */
class BosHomeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bos_home, container, false)
    }
}