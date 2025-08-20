package com.hastaprimasolusi.rana.ui.common

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.hastaprimasolusi.rana.R

/**
 * Created by maasrahman
 */

class ProgDialog {
    private var dialog: Dialog? = null
    private var mInstance: ProgDialog? = null

    @Synchronized
    fun getInstance(): ProgDialog {
        if (mInstance == null) {
            mInstance = ProgDialog()
        }
        return mInstance as ProgDialog
    }

    fun show(context: Context) {
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        dialog = Dialog(context, R.style.DialogBounceAnim)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.custom_progress_dialog)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    fun dismiss() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
}