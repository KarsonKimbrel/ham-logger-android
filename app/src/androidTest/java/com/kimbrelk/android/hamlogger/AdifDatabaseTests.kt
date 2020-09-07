package com.kimbrelk.android.hamlogger

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.kimbrelk.android.hamlogger.data.Adif
import com.kimbrelk.android.hamlogger.data.AppDatabase
import com.kimbrelk.android.hamlogger.data.model.Book
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.io.InputStream
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class AdifDatabaseTests {
    private lateinit var context: Context
    private lateinit var appDatabase: AppDatabase

    @Before
    fun createDb() {
        context = InstrumentationRegistry.getTargetContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun testReadSampleFile() {
        val result = readTestFile("read_test.txt")
        Assert.assertEquals(" Hello world\r\n ! & wow", result)
    }

    @Test
    fun testImportExportImport() {
        val bookId = "book_id"
        val fileNames = arrayOf(
            "small_test.adi",
            "small_test_fd.adi",
            "small_vartype_test.adi",
            //"pu2pop.ADI"
        )
        for (fileName in fileNames) {
            runBlocking {
                // Read in and save test file data
                appDatabase.bookDao().insert(Book(id=bookId))
                var fileContents = readTestFile(fileName)
                var bookEntries = Adif.fromAdi(fileContents)
                for (entry in bookEntries) {
                    entry.bookId = bookId
                }
                appDatabase.entryDao().insert(bookEntries)

                // Export and Clear DB
                val book = appDatabase.bookDao().getNow(bookId)
                val entries = appDatabase.entryDao().getAllNow(bookId)
                val exportedAdi = Adif.toAdi(context, book!!, entries)
                appDatabase.bookDao().delete(bookId)

                // Import exported data
                appDatabase.bookDao().insert(Book(id=bookId))
                fileContents = exportedAdi
                bookEntries = Adif.fromAdi(fileContents)
                for (entry in bookEntries) {
                    entry.bookId = bookId
                }
                appDatabase.entryDao().insert(bookEntries)
                val entriesImportedCheck = appDatabase.entryDao().getAllNow(bookId)

                Assert.assertEquals(entries, entriesImportedCheck)
            }
        }
    }

    fun readTestFile(fileName: String) : String {
        val buffer = StringBuffer("")
        var inputStream: InputStream? = null
        try {
            inputStream = this.javaClass.getResourceAsStream(fileName)!!
            while (true) {
                val char: Int = inputStream.read()
                if (char == -1) break
                buffer.append(char.toChar())
            }
        } finally {
            inputStream?.close()
        }
        return buffer.toString()
    }

}