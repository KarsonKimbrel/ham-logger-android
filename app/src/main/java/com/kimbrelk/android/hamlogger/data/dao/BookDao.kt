package com.kimbrelk.android.hamlogger.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kimbrelk.android.hamlogger.data.model.Book

@Dao
interface BookDao : BaseDao<Book> {

    @Query("DELETE FROM book WHERE id = :bookId")
    suspend fun delete(bookId: String)

    @Query("SELECT * FROM book WHERE id = :bookId LIMIT 1")
    fun get(bookId: String) : LiveData<Book?>

    @Query("SELECT * FROM book ORDER BY title ASC, callsign ASC")
    fun getAll() : LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE id = :bookId LIMIT 1")
    suspend fun getNow(bookId: String) : Book?

    @Transaction
    suspend fun upsert(book: Book) {
        val id = insert(book)
        if (id == -1L) {
            update(book)
        }
    }
}