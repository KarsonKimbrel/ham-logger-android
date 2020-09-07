package com.kimbrelk.android.hamlogger.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.data.legacydatabase.LegacyDatabaseContract
import com.kimbrelk.android.hamlogger.data.legacydatabase.LegacyDatabaseHelper
import com.kimbrelk.android.hamlogger.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LegacyDatabaseUpdater {

    private val database: AppDatabase

    constructor(database: AppDatabase) {
        this.database = database
    }

    private suspend fun deleteLegacyDatabase(context: Context) {
        withContext(Dispatchers.IO) {
            context.deleteDatabase(LegacyDatabaseHelper.DATABASE_NAME)
        }
    }

    private suspend fun legacyDatabaseExists(context: Context) : Boolean {
        return withContext(Dispatchers.IO) {
            context.getDatabasePath(LegacyDatabaseHelper.DATABASE_NAME).exists()
        }
    }

    private suspend fun preformLegacyDatabaseUpgrade(context: Context) {
        withContext(Dispatchers.IO) {
            val legacyDatabaseHelper = LegacyDatabaseHelper(context)
            val legacyDatabase = legacyDatabaseHelper.readableDatabase
            val legacyDatabaseContents = readLegacyDatabase(legacyDatabase)
            legacyDatabase.close()
            database.bookDao().insert(legacyDatabaseContents.first)
            database.entryDao().insert(legacyDatabaseContents.second)
        }
    }

    private suspend fun readLegacyDatabase(legacyDatabase: SQLiteDatabase) : Pair<List<Book>, List<Entry>> {
        return withContext(Dispatchers.IO) {
            val books = readLegacyDatabaseBooks(legacyDatabase)
            val entries = readLegacyDatabaseEntries(legacyDatabase)
            Pair(books, entries)
        }
    }

    private suspend fun readLegacyDatabaseBooks(legacyDatabase: SQLiteDatabase) : List<Book> {
        return withContext(Dispatchers.IO) {
            val books = mutableListOf<Book>()
            val cursorBooks = legacyDatabase.rawQuery("SELECT * FROM ${LegacyDatabaseContract.BookEntry.TABLE_NAME}", null)
            cursorBooks.moveToFirst()
            while(!cursorBooks.isAfterLast) {
                books.add(
                    Book(
                        id = Utils.getCursorString(cursorBooks, LegacyDatabaseContract.BookEntry._ID)!!,
                        callsign = Utils.getCursorString(cursorBooks, LegacyDatabaseContract.BookEntry.CALLSIGN)!!,
                        gridSquare = Utils.getCursorString(cursorBooks, LegacyDatabaseContract.BookEntry.GRID_SQUARE)!!,
                        hasMultipleOps = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.HAS_MULTIPLE_OPS),
                        showComments = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_COMMENTS),
                        showContestFieldday = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_CONTEST_FIELDDAY),
                        showLocCity = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_LOC_CITY),
                        showLocGrid = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_LOC_GRID),
                        showPower = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_POWER),
                        showSignal = Utils.getCursorBoolean(cursorBooks, LegacyDatabaseContract.BookEntry.SHOW_SIGNAL),
                        showTimeAsUTC = false,
                        title = Utils.getCursorString(cursorBooks, LegacyDatabaseContract.BookEntry.TITLE)
                    )
                )
                cursorBooks.moveToNext()
            }
            cursorBooks.close()
            books
        }
    }

    private suspend fun readLegacyDatabaseEntries(legacyDatabase: SQLiteDatabase) : List<Entry> {
        return withContext(Dispatchers.IO) {
            val entries = mutableListOf<Entry>()
            val cursorEntries = legacyDatabase.rawQuery("SELECT * FROM ${LegacyDatabaseContract.LogEntry.TABLE_NAME}", null)
            cursorEntries.moveToFirst()
            while(!cursorEntries.isAfterLast) {
                val legacyDate = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.DATE)
                val legacyTime = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.TIME)
                entries.add(
                    Entry(
                        id = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry._ID)!!,
                        bookId = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.BOOK_ID)!!,
                        band = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.BAND),
                        callOp = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CALLSIGN_OP),
                        callRx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CALLSIGN_RX),
                        callTx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CALLSIGN_TX),
                        commentsRx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.COMMENTS_RX),
                        commentsTx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.COMMENTS_TX),
                        contestCheck = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CONTEST_CHECK_RX),
                        contestClass = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CONTEST_CLASS_RX),
                        contestClassTransmitters = Utils.getCursorLong(cursorEntries, LegacyDatabaseContract.LogEntry.CONTEST_CLASS_TRANSMITTERS_RX)?.toInt(),
                        contestId = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CONTEST_ID),
                        contestSection = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.CONTEST_SECTION_RX),
                        frequency = Utils.getCursorDouble(cursorEntries, LegacyDatabaseContract.LogEntry.FREQUENCY) ?: 0.0,
                        locGridRx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.LOC_GRID_RX),
                        locGridTx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.LOC_GRID_TX),
                        mode = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.MODE),
                        modeSub = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.MODE_SUB),
                        powerReportRx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.POWER_RX),
                        powerReportTx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.POWER_TX),
                        signalReportRx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.SIGNAL_RX),
                        signalReportTx = Utils.getCursorString(cursorEntries, LegacyDatabaseContract.LogEntry.SIGNAL_TX),
                        timestamp = Utils.getLegacyTimestamp(legacyDate, legacyTime)
                    )
                )
                cursorEntries.moveToNext()
            }
            cursorEntries.close()
            entries
        }
    }

    suspend fun upgradeFromLegacyDatabaseIfExists(context: Context) {
        if (legacyDatabaseExists(context)) {
            Log.i(this::class.simpleName, "Starting legacy database upgrade")
            preformLegacyDatabaseUpgrade(context)
            deleteLegacyDatabase(context)
            Log.i(this::class.simpleName, "Legacy database upgrade complete")
        }
    }

}