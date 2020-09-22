package com.kimbrelk.android.hamlogger.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.utils.SortMode

@Dao
interface EntryDao : BaseDao<Entry> {

    @Query("DELETE FROM entry WHERE id = :entryId and bookId = :bookId")
    suspend fun delete(bookId: String, entryId: String)

    @Query("SELECT * FROM entry WHERE id = :entryId and bookId = :bookId LIMIT 1")
    fun get(bookId: String, entryId: String) : LiveData<Entry?>

    fun getAll(bookId: String, searchQuery: String) : Map<SortMode, LiveData<List<Entry>>> {
        return mapOf(
            Pair(SortMode.Callsign, getAllByCallsign(bookId, searchQuery)),
            Pair(SortMode.Date, getAllByDate(bookId, searchQuery)),
            Pair(SortMode.Frequency, getAllByFrequency(bookId, searchQuery))
        )
    }

    @Query("SELECT * FROM entry WHERE bookId = :bookId AND (callRx LIKE :searchQuery OR callOp LIKE :searchQuery OR commentsRx LIKE :searchQuery OR commentsTx LIKE :searchQuery OR mode LIKE :searchQuery OR modeSub LIKE :searchQuery) ORDER BY callRx ASC, timestamp DESC")
    fun getAllByCallsign(bookId: String, searchQuery: String) : LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE bookId = :bookId AND (callRx LIKE :searchQuery OR callOp LIKE :searchQuery OR commentsRx LIKE :searchQuery OR commentsTx LIKE :searchQuery OR mode LIKE :searchQuery OR modeSub LIKE :searchQuery) ORDER BY timestamp DESC")
    fun getAllByDate(bookId: String, searchQuery: String) : LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE bookId = :bookId AND (callRx LIKE :searchQuery OR callOp LIKE :searchQuery OR commentsRx LIKE :searchQuery OR commentsTx LIKE :searchQuery OR mode LIKE :searchQuery OR modeSub LIKE :searchQuery) ORDER BY frequency ASC, timestamp DESC")
    fun getAllByFrequency(bookId: String, searchQuery: String) : LiveData<List<Entry>>

    @Query("SELECT * FROM entry WHERE bookId = :bookId ORDER BY timestamp DESC")
    suspend fun getAllNow(bookId: String) : List<Entry>

    @Transaction
    suspend fun upsert(entry: Entry) {
        val id = insert(entry)
        if (id == -1L) {
            update(entry)
        }
    }

}