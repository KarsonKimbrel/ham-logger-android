package com.kimbrelk.android.hamlogger.ui.entrylist

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.ui.BaseViewModel
import com.kimbrelk.android.hamlogger.utils.Constants
import com.kimbrelk.android.hamlogger.utils.SortMode
import kotlinx.coroutines.launch

class ViewModelEntrylist(application: Application) : BaseViewModel(application) {

    private var entriesListData: MediatorLiveData<List<Entry>>? = MediatorLiveData<List<Entry>>()
    private var entryLiveDataSources: Map<SortMode, LiveData<List<Entry>>>? = null

    lateinit var bookId: String
    var searchQuery: String = ""
        set(query) {
            field = query
            updateEntryData()
        }
    var sortMode : SortMode
        get() = SortMode.valueOf(prefs.getString(Constants.Prefs.SORT_MODE, SortMode.Date.name)!!)
        set(value) {
            prefs.edit()
                .putString(Constants.Prefs.SORT_MODE, value.name)
                .apply()
            updateEntryData()
        }


    fun createEntry(book: Book) : Entry {
        val entry = Entry(
            bookId = book.id,
            callOp = if (book.hasMultipleOps) prefLastOperator else null,
            callTx = book.callsign,
            frequency = prefLastFrequency,
            locGridTx = book.gridSquare,
            mode = prefLastMode.mode.name,
            modeSub = prefLastMode.subMode
        )
        viewModelScope.launch {
            appRepository.upsert(entry)
        }
        return entry
    }

    fun getBook() : LiveData<Book?> {
        return appRepository.getBook(bookId)
    }

    fun getEntries() : LiveData<List<Entry>> {
        updateEntryData()
        return entriesListData as LiveData<List<Entry>>
    }

    fun adiExport() {
        viewModelScope.launch {
            appRepository.adiExport(bookId)
        }
    }

    fun adiImport(context: Context, uri: Uri) {
        viewModelScope.launch {
            appRepository.adiImport(context, uri, bookId)
        }
    }
    private fun updateEntryData() {
        entryLiveDataSources = appRepository.getEntries(bookId, "%$searchQuery%")
        for (pair in entryLiveDataSources!!) {
            entriesListData!!.removeSource(pair.value)
        }
        val source: LiveData<List<Entry>> = entryLiveDataSources!![sortMode] ?: error("Unknown sort mode")
        entriesListData!!.addSource(source, Observer { value -> entriesListData!!.setValue(value) })
    }

}