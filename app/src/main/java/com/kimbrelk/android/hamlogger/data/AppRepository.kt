package com.kimbrelk.android.hamlogger.data

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.utils.SortMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository {
    private val context: Context
    private val database: AppDatabase

    companion object {
        private var instance: AppRepository? = null

        fun getInstance(context: Context) : AppRepository {
            if (instance == null) {
                instance = AppRepository(context)
            }
            return instance!!
        }
    }

    private constructor(context: Context) {
        this.context = context
        this.database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-db"
        )
        .build()
    }

    suspend fun adiExport(bookId: String) {
        val exportAdiFile = Adif.export(context, database, bookId)
        withContext(Dispatchers.Main) {
            val exportIntent = Intent(Intent.ACTION_SEND)
            val contentUri = FileProvider.getUriForFile(
                context,
                "com.kimbrelk.android.hamlogger.fileprovider",
                exportAdiFile
            )
            exportIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            exportIntent.type = "application/adi"
            exportIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(Intent.createChooser(exportIntent, "Export to..."))
        }
    }

    suspend fun adiImport(context: Context, uri: Uri, bookId: String) {
        Adif.import(context, uri, database, bookId)
    }

    suspend fun deleteBook(bookId: String) {
        withContext(Dispatchers.IO) {
            database.bookDao().delete(bookId)
        }
    }

    suspend fun deleteEntry(bookId: String, entryId: String) {
        withContext(Dispatchers.IO) {
            database.entryDao().delete(bookId, entryId)
        }
    }

    fun getBook(bookId: String) : LiveData<Book?> {
        return database.bookDao().get(bookId)
    }

    fun getBooks() : LiveData<List<Book>> {
        return database.bookDao().getAll()
    }

    fun getEntries(bookId: String, searchQuery: String) : Map<SortMode, LiveData<List<Entry>>> {
        return database.entryDao().getAll(bookId, searchQuery)
    }

    fun getEntry(bookId: String, entryId: String) : LiveData<Entry?> {
        return database.entryDao().get(bookId, entryId)
    }

    suspend fun update(book: Book) {
        withContext(Dispatchers.IO) {
            database.bookDao().update(book)
        }
    }

    suspend fun update(entry: Entry) {
        withContext(Dispatchers.IO) {
            database.entryDao().update(entry)
        }
    }

    suspend fun upgradeFromLegacyDatabaseIfExists() {
        LegacyDatabaseUpdater(database).upgradeFromLegacyDatabaseIfExists(context)
    }

    suspend fun upsert(book: Book) {
        withContext(Dispatchers.IO) {
            database.bookDao().upsert(book)
        }
    }

    suspend fun upsert(entry: Entry) {
        withContext(Dispatchers.IO) {
            database.entryDao().upsert(entry)
        }
    }

    fun getShouldShowAds() : LiveData<Boolean> {
        val result = MutableLiveData<Boolean>(false)
        // TODO Coroutine start
        return result
    }

}