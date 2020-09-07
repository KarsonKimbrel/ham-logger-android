package com.kimbrelk.android.hamlogger.data.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.utils.UtilsLegacy
import java.util.*

@Entity
data class Book(
    @PrimaryKey
    val id: String = UtilsLegacy.createNewUUID(),
    var title: String? = null,
    var callsign: String? = null,
    var gridSquare: String? = null,
    var hasMultipleOps: Boolean = false,
    var showComments: Boolean = false,
    var showContestFieldday: Boolean = false,
    var showLocGrid: Boolean = false,
    var showLocCity: Boolean = false,
    var showPower: Boolean = false,
    var showSignal: Boolean = false,
    var showTimeAsUTC: Boolean = false
) {

    companion object {

        fun create(context: Context) : Book {
            val book = Book()
            book.title = context.resources.getString(R.string.default_book_title)
            return book
        }

    }

    fun getTimeZone() : TimeZone {
        if (showTimeAsUTC) {
            return TimeZone.getTimeZone("UTC")
        }
        return TimeZone.getDefault()
    }

}