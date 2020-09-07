package com.kimbrelk.android.hamlogger.data.model

import android.content.Context
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.data.legacydatabase.model.Mode
import com.kimbrelk.android.hamlogger.utils.Utils
import com.kimbrelk.android.hamlogger.utils.UtilsLegacy

@Entity(
    primaryKeys = [
        "id",
        "bookId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = CASCADE
        )
    ]
)
data class Entry(
    var id: String = UtilsLegacy.createNewUUID(),
    var band: String? = null,
    var bookId: String,
    var callOp: String? = null,
    var callTx: String? = null,
    var callRx: String? = null,
    var commentsRx: String? = null,
    var commentsTx: String? = null,
    var contestCheck: String? = null,
    var contestClass: String? = null,
    var contestClassTransmitters: Int? = null,
    var contestId: String? = null,
    var contestSection: String? = null,
    var frequency: Double = 0.0,
    var locGridTx: String? = null,
    var locGridRx: String? = null,
    var mode: String? = null,
    var modeSub: String? = null,
    var powerReportRx: String? = null,
    var powerReportTx: String? = null,
    var signalReportRx: String? = null,
    var signalReportTx: String? = null,
    var timestamp: Long = System.currentTimeMillis()
) {

    fun getBandName(context: Context) : String {
        return Utils.BANDS.fromFrequency(frequency)?.name ?: context.resources.getString(R.string.non_amateur)
    }

    fun getModeSubModePair() : ModeSubModePair {
        try {
            if (mode != null) {
                return ModeSubModePair(
                    Mode.valueOf(mode!!),
                    modeSub
                )
            }
        } catch (e: Exception) {}
        return ModeSubModePair(
            Mode.AM,
            null
        )
    }

}