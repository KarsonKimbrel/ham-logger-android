package com.kimbrelk.android.hamlogger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kimbrelk.android.hamlogger.data.dao.BookDao
import com.kimbrelk.android.hamlogger.data.dao.EntryDao
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry

@Database(
    entities = [
        Book::class,
        Entry::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
    abstract fun entryDao() : EntryDao

}