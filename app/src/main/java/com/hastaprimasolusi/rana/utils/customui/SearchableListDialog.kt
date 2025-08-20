package com.hastaprimasolusi.rana.utils.customui

import android.app.AlertDialog
import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.DialogFragment
import com.hastaprimasolusi.rana.R
import java.io.Serializable

/**
 * Created by MASRAHMAN on 1/17/2018.
 */
class SearchableListDialog(private val _context: Context) : DialogFragment(),
    SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private var listAdapter: ArrayAdapter<*>? = null
    private var _listViewItems: ListView? = null
    private var _searchableItem: SearchableItem<*>? = null
    private var _onSearchTextChanged: OnSearchTextChanged? = null
    private var _searchView: SearchView? = null
    private var _strTitle: String? = null
    private var _strPositiveButtonText: String? = null
    private var _onClickListener: DialogInterface.OnClickListener? = null
    private val temp = mutableListOf<Any>()
    private val items = mutableListOf<Any>()
    private var rootView: View? = null
    private lateinit var listener:(Any) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("INIT ROOT VIEW")
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        println("INIT CREATE DIALOG")
        val inflater = LayoutInflater.from(_context)
        rootView = inflater.inflate(R.layout.searchable_list_dialog, null)

        val alertDialog =
            AlertDialog.Builder(_context)
        alertDialog.setView(rootView)
        val header = rootView?.findViewById<TextView>(R.id.headerSearchable)
        val strTitle =
            if (_strTitle == null) "Daftar Item" else "Daftar $_strTitle"
        header?.text = strTitle
        val btnTutup = rootView!!.findViewById<Button>(R.id.btnTutup)
        btnTutup!!.setOnClickListener { dismiss() }

        val searchManager =
            _context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        _searchView = rootView?.findViewById(R.id.search)

        //change color of search text
        val id = _searchView?.context?.resources
            ?.getIdentifier("android:id/search_src_text", null, null)
        val searchEditText = id?.let { (_searchView as SearchView).findViewById<EditText>(it) }
        searchEditText?.setTextColor(resources.getColor(R.color.gray50))
        searchEditText?.setHintTextColor(resources.getColor(R.color.gray50))
        searchEditText?.gravity = Gravity.CENTER
        _searchView?.isIconifiedByDefault = false
        _searchView?.setOnQueryTextListener(this)
        _searchView?.setOnCloseListener(this)
        _searchView?.clearFocus()

        if (temp.size < 5) {
            _searchView?.visibility = View.GONE
        } else {
            _searchView?.visibility = View.VISIBLE
        }
        _listViewItems = rootView?.findViewById(R.id.listItems)

        //create the adapter by passing your ArrayList data
        println("CEK ITEM SIZE ${items.size}")
        listAdapter = ArrayAdapter(
            _context, R.layout.custom_textlist,
            items
        )
        //attach the adapter to the list
        _listViewItems?.adapter = listAdapter
        _listViewItems?.isTextFilterEnabled = true
        _listViewItems?.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val mgr =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mgr.hideSoftInputFromWindow(_searchView?.windowToken, 0)
            listener(items[position])
            dialog?.dismiss()
        }

        val dialog = alertDialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return dialog
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("item", _searchableItem)
        super.onSaveInstanceState(outState)
    }

    fun setTitle(strTitle: String?) {
        _strTitle = strTitle
    }

    fun setPositiveButton(strPositiveButtonText: String?) {
        _strPositiveButtonText = strPositiveButtonText
    }

    fun setPositiveButton(
        strPositiveButtonText: String?,
        onClickListener: DialogInterface.OnClickListener?
    ) {
        _strPositiveButtonText = strPositiveButtonText
        _onClickListener = onClickListener
    }

    fun setOnSearchableItemClickListener(searchableItem: SearchableItem<*>?) {
        _searchableItem = searchableItem
    }

    fun setOnSearchTextChangedListener(onSearchTextChanged: OnSearchTextChanged?) {
        _onSearchTextChanged = onSearchTextChanged
    }

    override fun onClose(): Boolean {
        return false
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onQueryTextSubmit(s: String): Boolean {
        _searchView?.clearFocus()
        return true
    }

    fun setData(listData: MutableList<Any>, listener:(Any) -> Unit) {
        if(items.isNotEmpty()){
            items.clear()
            temp.clear()
        }
        items.addAll(listData)
        temp.addAll(listData)
        this.listener = listener
    }

    override fun onQueryTextChange(s: String): Boolean {
        items.clear()
        if (TextUtils.isEmpty(s)) {
            items.addAll(temp)
        } else {
            for (row in temp) {
                if (row.toString().toLowerCase().contains(s)) {
                    items.add(row)
                }
            }
        }
        listAdapter?.notifyDataSetChanged()
        if (null != _onSearchTextChanged) {
            _onSearchTextChanged!!.onSearchTextChanged(s)
        }
        return false
    }

    interface SearchableItem<T> : Serializable {
        fun onSearchableItemClicked(item: T, position: Int)
    }

    interface OnSearchTextChanged {
        fun onSearchTextChanged(strText: String?)
    }

    companion object {
        fun newInstance(context: Context): SearchableListDialog {
            return SearchableListDialog(context)
        }
    }
}