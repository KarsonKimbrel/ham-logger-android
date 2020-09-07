package com.kimbrelk.android.hamlogger.ui.bookedit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.ui.BaseViewModel
import kotlinx.coroutines.launch

class ViewModelBookedit(application: Application) : BaseViewModel(application) {

    fun getBook(bookId: String) : LiveData<Book?> {
        return appRepository.getBook(bookId)
    }

    fun saveBook(book: Book) {
        viewModelScope.launch {
            appRepository.update(book)
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            appRepository.deleteBook(bookId)
        }
    }

}