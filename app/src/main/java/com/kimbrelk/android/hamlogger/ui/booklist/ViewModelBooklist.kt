package com.kimbrelk.android.hamlogger.ui.booklist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.ui.BaseViewModel
import kotlinx.coroutines.launch

class ViewModelBooklist(application: Application) : BaseViewModel(application) {

    fun createBook() : Book {
        val book = Book.create(getApplication<Application>().applicationContext)
        viewModelScope.launch {
            appRepository.upsert(book)
        }
        return book
    }

    fun getBooks() : LiveData<List<Book>> {
        return appRepository.getBooks()
    }

}