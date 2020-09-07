package com.kimbrelk.android.hamlogger.ui.entryedit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.data.model.ModeSubModePair
import com.kimbrelk.android.hamlogger.ui.BaseViewModel
import kotlinx.coroutines.launch

class ViewModelEntryedit(application: Application) : BaseViewModel(application) {

    fun getBook(bookId: String) : LiveData<Book?> {
        return appRepository.getBook(bookId)
    }

    fun getEntry(bookId: String, entryId: String) : LiveData<Entry?> {
        return appRepository.getEntry(bookId, entryId)
    }

    fun saveEntry(entry: Entry, mode: ModeSubModePair) {
        viewModelScope.launch {
            appRepository.update(entry)
        }
        prefLastFrequency = entry.frequency
        prefLastMode = mode
        prefLastOperator = entry.callOp
    }

    fun deleteEntry(bookId: String, entryId: String) {
        viewModelScope.launch {
            appRepository.deleteEntry(bookId, entryId)
        }
    }

}