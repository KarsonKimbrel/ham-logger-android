package com.kimbrelk.android.hamlogger.ui.entrylist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.utils.Utils
import kotlinx.android.synthetic.main.item_entry.view.*
import java.util.*
import kotlin.collections.ArrayList

class AdapterEntries : RecyclerView.Adapter<AdapterEntries.VH> {

    private val TYPE_ITEM_EMPTY = 0
    private val TYPE_ITEM = 1

    private val context: Context
    private var book: Book? = null
    private var items: List<Entry>

    var callbackEntryPressed: ((Entry) -> Unit)? = null
    var callbackEntryLongPressed: ((Entry) -> Unit)? = null

    constructor(context: Context) : super() {
        this.context = context
        items = ArrayList(0)
    }

    @UiThread
    fun updateBook(book: Book) {
        this.book = book
        notifyDataSetChanged()
    }

    @UiThread
    fun updateItems(items: List<Entry>) {
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
        var viewId = R.layout.item_entry
        if (viewType == TYPE_ITEM_EMPTY) {
            viewId = R.layout.item_entry_empty
        }
        val view = LayoutInflater.from(parent.context).inflate(viewId, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (getItemViewType(position) == TYPE_ITEM) {
            val entry: Entry = items[position]
            holder.txtBand!!.text = entry.band ?: context.resources.getText(R.string.non_amateur)
            holder.txtCall!!.text = entry.callRx ?: context.getString(R.string.hint_no_call)
            holder.txtFreq!!.text = Utils.formatFrequency(entry.frequency)
            holder.txtMode!!.text = entry.getModeSubModePair().label
            holder.txtBand!!.text = entry.getBandName(context)
            val timeZone = book?.getTimeZone() ?: TimeZone.getDefault()
            holder.txtDate!!.text = Utils.formatDateReadable(entry.timestamp, timeZone)
            holder.txtTime!!.text = Utils.formatTimeReadable(entry.timestamp, timeZone)

            holder.root.setOnClickListener {
                if (callbackEntryPressed != null) {
                    callbackEntryPressed!!(entry)
                }
            }
            holder.root.setOnLongClickListener {
                if (callbackEntryLongPressed != null) {
                    callbackEntryLongPressed!!(entry)
                }
                true
            }
        }
    }

    class VH : RecyclerView.ViewHolder {
        val root: View
        val txtBand: TextView?
        val txtCall: TextView?
        val txtDate: TextView?
        val txtFreq: TextView?
        val txtMode: TextView?
        val txtTime: TextView?

        constructor(view: View) : super(view) {
            root = view
            txtBand = view.txt_band
            txtCall = view.txt_call
            txtDate = view.txt_date
            txtFreq = view.txt_freq
            txtMode = view.txt_mode
            txtTime = view.txt_time
        }
    }

}