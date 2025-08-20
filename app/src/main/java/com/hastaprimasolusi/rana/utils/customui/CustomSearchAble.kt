package com.hastaprimasolusi.rana.utils.customui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.textfield.TextInputEditText
import com.hastaprimasolusi.rana.R

/**
 * Created By maasrahman on 7/1/20
 */
class CustomSearchAble @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0):
    TextInputEditText(context, attrs, defStyle),
    View.OnTouchListener, SearchableListDialog.SearchableItem<Any> {

    val NO_ITEM_SELECTED = -1
    private var _context: Context? = null
    private var _items = mutableListOf<Any>()
    private var _searchableListDialog: SearchableListDialog? = null

    private var _isDirty = false
    private var _strHintText: String? = null
    private var _isFromInit = false
    private var selectedPosition = -1
    private var _title = "lain"

    init {
        _context = context
        isCursorVisible = false

        attrs?.let { attr ->
            val a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner)
            val N = a.indexCount
            for (i in 0 until N) {
                val attr = a.getIndex(i)
                if (attr == R.styleable.SearchableSpinner_hintText) {
                    _strHintText = a.getString(attr)
                }
            }
        }
        init()
    }

    private fun init() {
        this.compoundDrawablePadding = -24
        _searchableListDialog = _context?.let { SearchableListDialog.newInstance(it) }
        _searchableListDialog?.setOnSearchableItemClickListener(this)
        _searchableListDialog?.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme_TransparentOutside)
        setOnTouchListener(this)
    }

    fun setDefaultItem(position: Int) {
        if (_items.isNotEmpty()) {
            selectedPosition = position
            val item = _items[position]
            updateValue(item)
        }
    }

    fun updateValue(item: Any?) {
        this.setText(item.toString())
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        println("MASUK TOUCH")
        if (_searchableListDialog!!.isAdded) {
            return true
        }
        if (event.action == MotionEvent.ACTION_UP) {
            println("MASUK ACTION UP")
            if (_items.isNotEmpty()) {
                println("ON TOUCH COOOOYY")
                scanForActivity(context)?.supportFragmentManager?.let {
                    _searchableListDialog?.show(
                        it, "TAG")
                }
            }
        }
        return true
    }

    fun setAdapter(listData: MutableList<Any>, listener:(Any) -> Unit) {
        _items.clear()
        listData.forEach {
            _items.add(it)
        }
        _searchableListDialog?.setData(_items, listener)
    }


    override fun onSearchableItemClicked(item: Any, position: Int) {
        selectedPosition = _items.indexOf(item.toString())
        if (!_isDirty) {
            _isDirty = true
            selectedPosition = _items.indexOf(item.toString())
        }
        if (selectedPosition >= 0) {
            updateValue(item)
        }
        if (selectedPosition < 0) {
            for (i in _items.indices) {
                if (_items[i].toString() == item.toString()) {
                    selectedPosition = i
                    updateValue(item)
                    return
                }
            }
        }
    }

    fun setTitle(strTitle: String) {
        _title = strTitle
        _strHintText = strTitle
        this.hint = strTitle
    }

    fun setPositiveButton(strPositiveButtonText: String?) {
        _searchableListDialog!!.setPositiveButton(strPositiveButtonText)
    }

    fun setPositiveButton(
        strPositiveButtonText: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        _searchableListDialog!!.setPositiveButton(strPositiveButtonText, onClickListener)
    }

    fun setOnSearchTextChangedListener(onSearchTextChanged: SearchableListDialog.OnSearchTextChanged?) {
        _searchableListDialog!!.setOnSearchTextChangedListener(onSearchTextChanged)
    }

    private fun scanForActivity(cont: Context?): AppCompatActivity? {
        when (cont) {
            null -> return null
            is AppCompatActivity -> return cont
            is ContextWrapper -> return scanForActivity(
                cont.baseContext
            )
            else -> return null
        }
    }

    fun setSelectedItemPosition(position: Int) {
        selectedPosition = position
    }

    fun getSelectedItemPosition(): Int {
        /*   if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            //return super.getSelectedItemPosition();
            return 0;
        }*/
        return selectedPosition
    }

    fun getSelectedItem(): Any? {
        return if (selectedPosition < 0) {
            null
        } else _items!![selectedPosition]
        /*if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            //return super.getSelectedItem();
            return null;
        }*/
    }
}