package com.kimbrelk.android.hamlogger.ui.booklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.data.model.Book
import kotlinx.android.synthetic.main.item_book.view.*

class AdapterBooks : RecyclerView.Adapter<AdapterBooks.VH> {

    private val TYPE_ITEM_EMPTY = 0
    private val TYPE_ITEM = 1

    private val context: Context
    private var items: List<Book>

    var callbackBookPressed: ((Book) -> Unit)? = null
    var callbackBookLongPressed: ((Book) -> Unit)? = null

    constructor(context: Context) : super() {
        this.context = context
        items = ArrayList(0)
    }

    @UiThread
    fun updateItems(items: List<Book>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() : Int =
        if (items.isEmpty()) 1
        else items.size

    override fun getItemViewType(position: Int) : Int =
        if (items.isEmpty()) TYPE_ITEM_EMPTY
        else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : VH {
        var viewId = R.layout.item_book
        if (viewType == TYPE_ITEM_EMPTY) {
            viewId = R.layout.item_book_empty
        }
        val view = LayoutInflater.from(parent.context).inflate(viewId, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val book: Book = items[position]
            holder.txtTitle.text = book.title
            if (book.title == null) {
                holder.txtTitle.text = context.resources.getString(R.string.hint_untitled_book)
            }
            holder.txtDescription.text = book.callsign
            if (book.callsign == null) {
                holder.txtDescription.text = context.resources.getString(R.string.hint_no_call)
            }
            holder.root.setOnClickListener {
                if (callbackBookPressed != null) {
                    callbackBookPressed!!(book)
                }
            }
            holder.root.setOnLongClickListener {
                if (callbackBookLongPressed != null) {
                    callbackBookLongPressed!!(book)
                }
                true
            }
        }
    }

    class VH : RecyclerView.ViewHolder {
        val root: View
        val txtTitle: TextView
        val txtDescription: TextView

        constructor(view: View) : super(view) {
            root = view
            txtTitle = view.txt_title
            txtDescription = view.txt_desc
        }
    }

}