package com.kimbrelk.android.hamlogger.data

import android.content.Context
import android.net.Uri
import android.util.Log
import com.kimbrelk.android.hamlogger.data.model.AdiLexeme
import com.kimbrelk.android.hamlogger.data.model.Book
import com.kimbrelk.android.hamlogger.data.model.Entry
import com.kimbrelk.android.hamlogger.utils.Utils
import com.kimbrelk.android.hamlogger.utils.UtilsLegacy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.jvm.Throws

class Adif {

    companion object {

        private object Keys {

            const val VERSION = "3.0.8"
            const val PROGRAM_ID = "ANDROID_HAM_LOGGER"

            object Header {
                const val TIMESTAMP = "CREATED_TIMESTAMP"
                const val END_OF_HEADER = "EOH"
                const val PROGRAM_ID = "PROGRAMID"
                const val PROGRAM_VER = "PROGRAMVERSION"
                const val VERSION = "ADIF_VER"
                const val BOOK_NAME = "APP_${Keys.PROGRAM_ID}_BOOK_NAME"
                const val BOOK_ID = "APP_${Keys.PROGRAM_ID}_BOOK_ID"
                const val BOOK_PREFS_OWNER = "APP_${Keys.PROGRAM_ID}_OWNER_CALLSIGN"
                const val BOOK_PREFS_GRIDSQUARE = "APP_${Keys.PROGRAM_ID}_GRIDSQUARE"
                const val BOOK_PREFS_HAS_MULTIPLE_OPS = "APP_${Keys.PROGRAM_ID}_HAS_MULTIPLE_OPS"
                const val BOOK_PREFS_SHOW_CONTEST_FIELDDAY = "APP_${Keys.PROGRAM_ID}_SHOW_CONTEST_FIELDDAY"
                const val BOOK_PREFS_SHOW_LOC_CITY = "APP_${Keys.PROGRAM_ID}_SHOW_LOC_CITY"
                const val BOOK_PREFS_SHOW_LOC_GRID = "APP_${Keys.PROGRAM_ID}_SHOW_LOC_GRID"
                const val BOOK_PREFS_SHOW_POWER = "APP_${Keys.PROGRAM_ID}_SHOW_POWER"
                const val BOOK_PREFS_SHOW_SIGNAL = "APP_${Keys.PROGRAM_ID}_SHOW_SIGNAL"
                const val BOOK_PREFS_SHOW_TIME_AS_UTC = "APP_${Keys.PROGRAM_ID}_SHOW_TIME_AS_UTC"
            }

            object QSO {
                const val BAND = "BAND"
                const val BOOK_ID = "APP_${PROGRAM_ID}_BOOK_ID"
                const val CALL_OP = "OPERATOR"
                const val CALL_TX = "STATION_CALLSIGN"
                const val CALL_RX = "CALL"
                const val COMMENT_RX = "COMMENT"
                const val COMMENT_TX = "NOTES"
                const val COMMENT_RX_INTL = "COMMENT_INTL"
                const val COMMENT_TX_INTL = "NOTES_INTL"
                const val CONTEST_CLASS = "CLASS"
                const val CONTEST_CHECK = "CHECK"
                const val CONTEST_ID = "CONTEST_ID"
                const val CONTEST_SECTION = "ARRL_SECT"
                const val DATE_START = "QSO_DATE"
                const val DATE_END = "QSO_DATE_OFF"
                const val DISTANCE = "DISTANCE"
                const val END_OF_RECORD = "EOR"
                const val FREQ_TX = "FREQ"
                const val FREQ_RX = "FREQ_RX"
                const val ID = "APP_${PROGRAM_ID}_ID"
                const val LOC_GRIDSQUARE_TX = "MY_GRIDSQUARE"
                const val LOC_GRIDSQUARE_RX = "GRIDSQUARE"
                const val LOC_ADDRESS_TX = "MY_ADDRESS"
                const val LOC_ADDRESS_RX = "ADDRESS"
                const val LOC_LAT_RX = "LAT"
                const val LOC_LAT_TX = "MY_LAT"
                const val LOC_LON_RX = "LON"
                const val LOC_LON_TX = "MY_LON"
                const val MODE = "MODE"
                const val MODE_SUB = "SUBMODE"
                const val NAME = "NAME"
                const val POWER_TX = "TX_PWR"
                const val POWER_RX = "RX_PWR"
                const val SIGNAL_TX = "RST_SENT"
                const val SIGNAL_RX = "RST_RCVD"
                const val TIME_START = "TIME_ON"
                const val TIME_END = "TIME_OFF"
            }

        }

        suspend fun export(context: Context, appDatabase: AppDatabase, bookId: String) : File {
            val book = appDatabase.bookDao().getNow(bookId)
            val entries = appDatabase.entryDao().getAllNow(bookId)
            val adiStr = toAdi(context, book!!, entries)
            return writeAdiToFile(context, adiStr)
        }

        suspend fun fromAdi(strAdi: String) : List<Entry> {
            val adiLexemes: List<AdiLexeme> = lexAdi(strAdi)
            return compileAdi(adiLexemes)
        }

        suspend fun lexAdi(strAdi: String) : List<AdiLexeme> {
            return withContext(Dispatchers.Default) {
                val adiLexemes = mutableListOf<AdiLexeme>()
                var tagNameBuf: StringBuffer? = null
                var tagSizeBuf: StringBuffer? = null
                var tagValueBuf: StringBuffer? = null
                var tagSize: Int? = null
                for (char in strAdi.toCharArray()) {
                    val isReadingValue: Boolean = tagValueBuf != null
                    val isReadingValueSize: Boolean = !isReadingValue && tagSizeBuf != null
                    val isReadingTagName: Boolean = !isReadingValue && !isReadingValueSize && tagNameBuf != null
                    val isNotReadingAnything = !isReadingTagName && !isReadingValueSize && !isReadingValue
                    var isLexemeComplete = false

                    if (isNotReadingAnything) {
                        if (char == '<') {
                            tagNameBuf = StringBuffer("")
                        }
                    } else if (isReadingTagName) {
                        when (char) {
                            ':' -> tagSizeBuf = StringBuffer("")
                            '>' -> isLexemeComplete = true
                            else -> tagNameBuf!!.append(char)
                        }
                    } else if (isReadingValueSize) {
                        when (char) {
                            '>' -> {
                                tagValueBuf = StringBuffer("")
                                tagSize = tagSizeBuf.toString().split(":")[0].toInt()
                            }
                            else -> tagSizeBuf!!.append(char)
                        }
                    } else if (isReadingValue) {
                        if (tagSize!! > 0) {
                            tagValueBuf!!.append(char)
                            if (tagSize == 1) {
                                isLexemeComplete = true
                            }
                        }
                        tagSize = tagSize!! - 1
                    }
                    if (isLexemeComplete) {
                        adiLexemes.add(AdiLexeme(tagNameBuf!!.toString().toUpperCase(Locale.US), tagValueBuf?.toString()))
                        tagNameBuf = null
                        tagSizeBuf = null
                        tagValueBuf = null
                        tagSize = null
                    }
                }
                adiLexemes
            }
        }

        suspend fun compileAdi(adiLexemes: List<AdiLexeme>) : List<Entry> {
            val adiLexemeGroups = groupAdiLexemes(adiLexemes)
            return compileAdiLexemeGroups(adiLexemeGroups)
        }

        suspend fun groupAdiLexemes(adiLexemes: List<AdiLexeme>) : List<Map<String, String>> {
            return withContext(Dispatchers.Default) {
                val adiLexemeGroups = mutableListOf<Map<String, String>>()
                var adiLexemeGroup: MutableMap<String, String>? = null
                for (adiLexeme in adiLexemes) {
                    if (adiLexemeGroup == null) {
                        adiLexemeGroup = mutableMapOf()
                    }
                    when (adiLexeme.tagName ) {
                        Keys.QSO.END_OF_RECORD -> {
                            adiLexemeGroups.add(adiLexemeGroup)
                            adiLexemeGroup = null
                        }
                        Keys.Header.END_OF_HEADER -> {
                            adiLexemeGroups.add(adiLexemeGroup)
                            adiLexemeGroup = null
                        }
                        else -> {
                            if (adiLexeme.tagValue != null) {
                                adiLexemeGroup[adiLexeme.tagName] = adiLexeme.tagValue
                            } else {
                                Log.w(this::class.simpleName, "ADI tag missing value ${adiLexeme.tagName}")
                            }
                        }
                    }
                }
                adiLexemeGroups
            }
        }

        suspend fun compileAdiLexemeGroups(adiLexemeGroups: List<Map<String, String>>) : List<Entry> {
            return withContext(Dispatchers.Default) {
                val entries = mutableListOf<Entry>()
                var isFirst = true
                for (adiLexemeGroup in adiLexemeGroups) {
                    if (isFirst) {
                        isFirst = false
                        continue
                    }
                    val entry = Entry(
                        id = adiLexemeGroup[Keys.QSO.ID]
                            ?: UtilsLegacy.createNewUUID(),
                        bookId = adiLexemeGroup[Keys.QSO.BOOK_ID]
                            ?: UtilsLegacy.createNewUUID(),
                        band = adiLexemeGroup[Keys.QSO.BAND],
                        callOp = adiLexemeGroup[Keys.QSO.CALL_OP],
                        callRx = adiLexemeGroup[Keys.QSO.CALL_RX]
                            ?: adiLexemeGroup[Keys.QSO.CALL_OP],
                        callTx = adiLexemeGroup[Keys.QSO.CALL_TX],
                        commentsRx = adiLexemeGroup[Keys.QSO.COMMENT_RX_INTL]
                            ?: adiLexemeGroup[Keys.QSO.COMMENT_RX],
                        commentsTx = adiLexemeGroup[Keys.QSO.COMMENT_TX_INTL]
                            ?: adiLexemeGroup[Keys.QSO.COMMENT_TX],
                        contestCheck = adiLexemeGroup[Keys.QSO.CONTEST_CHECK],
                        contestClass = adiLexemeGroup[Keys.QSO.CONTEST_CLASS],
                        contestId = adiLexemeGroup[Keys.QSO.CONTEST_ID],
                        contestSection = adiLexemeGroup[Keys.QSO.CONTEST_SECTION],
                        frequency = adiLexemeGroup[Keys.QSO.FREQ_RX]?.toDouble()
                            ?: adiLexemeGroup[Keys.QSO.FREQ_TX]?.toDouble()
                            ?: 0.0,
                        locGridRx = adiLexemeGroup[Keys.QSO.LOC_GRIDSQUARE_RX],
                        locGridTx = adiLexemeGroup[Keys.QSO.LOC_GRIDSQUARE_TX],
                        mode = adiLexemeGroup[Keys.QSO.MODE],
                        modeSub = adiLexemeGroup[Keys.QSO.MODE_SUB],
                        powerReportRx = adiLexemeGroup[Keys.QSO.POWER_RX],
                        powerReportTx = adiLexemeGroup[Keys.QSO.POWER_TX],
                        signalReportRx = adiLexemeGroup[Keys.QSO.SIGNAL_RX],
                        signalReportTx = adiLexemeGroup[Keys.QSO.SIGNAL_TX],
                        timestamp = Utils.getLegacyTimestamp(
                            adiLexemeGroup[Keys.QSO.DATE_START],
                            adiLexemeGroup[Keys.QSO.TIME_START])
                    )
                    val contestClass = entry.contestClass
                    if (entry.contestClass != null && contestClass!!.length >= 2) {
                        try {
                            entry.contestClassTransmitters =
                                contestClass.toCharArray()[contestClass.length - 1].toString()
                                    .toInt()
                            entry.contestClass =
                                contestClass.toCharArray(0, contestClass.length - 1).toString()
                        } catch(e: NumberFormatException) {

                        }
                    }
                    entries.add(entry)
                }
                entries
            }
        }

        suspend fun import(context: Context, uri: Uri, appDatabase: AppDatabase, bookId: String) {
            val adiStr = readAdiFromUri(context, uri)
            val bookEntries = fromAdi(adiStr)
            for (entry in bookEntries) {
                entry.bookId = bookId
            }
            appDatabase.entryDao().insert(bookEntries)
        }

        suspend fun readAdiFromUri(context: Context, uri: Uri) : String {
            return withContext(Dispatchers.IO) {
                val buffer = StringBuffer("")
                var inputStream: InputStream? = null
                try {
                    inputStream = context.contentResolver.openInputStream(uri)
                    var char: Char
                    while (inputStream.read().also { char = it.toChar() } != -1) {
                        buffer.append(char)
                    }
                }
                finally {
                    inputStream?.close()
                }
                buffer.toString()
            }
        }

        suspend fun toAdi(context: Context, book: Book, entries: List<Entry>) : String {
            return withContext(Dispatchers.Default) {
                val adiBuffer = StringBuffer("")
                addAdiHeader(context, adiBuffer, book)
                for (record in entries) {
                    addAdiRecord(context, adiBuffer, record)
                }
                adiBuffer.toString()
            }
        }

        private suspend fun writeAdiToFile(context: Context, adiStr: String) : File {
            val file = writeToTempFile(
                context,
                File(context.cacheDir, "/export/"),
                UtilsLegacy.createNewUUID() + ".adi",
                adiStr,
                false
            )
            Log.i(this::class.qualifiedName, "Saved export to " + file.name + " (" + file.length() + " bytes)")
            return file
        }

        @Throws(IOException::class)
        private suspend fun writeToTempFile(context: Context, fileBase: File, fileName: String, fileContents: String, isTempFile: Boolean) : File {
            return withContext(Dispatchers.IO) {
                var file: File
                var outputStream: FileOutputStream? = null
                try {
                    fileBase.mkdirs()
                    if (isTempFile) {
                        file = File.createTempFile(fileName, null, fileBase)
                    } else {
                        file = File(fileBase, fileName)
                        file.createNewFile()
                    }
                    outputStream = FileOutputStream(file)
                    outputStream.write(fileContents.toByteArray())
                    outputStream.flush()
                } finally {
                    outputStream?.close()
                }
                file
            }
        }

        fun addAdiRecord(context: Context, adiBuffer: StringBuffer, record: Entry) {
            addAdi(adiBuffer, Keys.QSO.ID, record.id)
            addAdi(adiBuffer, Keys.QSO.BAND, record.band)
            addAdi(adiBuffer, Keys.QSO.BOOK_ID, record.bookId)
            addAdi(adiBuffer, Keys.QSO.CALL_RX, record.callRx)
            addAdi(adiBuffer, Keys.QSO.CALL_TX, record.callTx)
            addAdi(adiBuffer, Keys.QSO.CALL_OP, record.callOp)
            addAdi(adiBuffer, Keys.QSO.COMMENT_RX, record.commentsRx)
            addAdi(adiBuffer, Keys.QSO.COMMENT_TX, record.commentsTx)
            addAdi(adiBuffer, Keys.QSO.COMMENT_RX_INTL, record.commentsRx)
            addAdi(adiBuffer, Keys.QSO.COMMENT_TX_INTL, record.commentsTx)
            addAdi(adiBuffer, Keys.QSO.CONTEST_CHECK, record.contestCheck)
            addAdi(adiBuffer, Keys.QSO.CONTEST_CLASS, (record.contestClassTransmitters ?: "").toString() + (record.contestClass ?: "").toString())
            addAdi(adiBuffer, Keys.QSO.CONTEST_ID, record.contestId)
            addAdi(adiBuffer, Keys.QSO.CONTEST_SECTION, record.contestSection)
            addAdi(adiBuffer, Keys.QSO.DATE_START, UtilsLegacy.formatDate(record.timestamp))
            addAdi(adiBuffer, Keys.QSO.FREQ_RX, record.frequency.toString())
            addAdi(adiBuffer, Keys.QSO.FREQ_TX, record.frequency.toString())
            addAdi(adiBuffer, Keys.QSO.LOC_GRIDSQUARE_RX, record.locGridRx)
            addAdi(adiBuffer, Keys.QSO.LOC_GRIDSQUARE_TX, record.locGridTx)
            addAdi(adiBuffer, Keys.QSO.MODE, record.mode)
            addAdi(adiBuffer, Keys.QSO.MODE_SUB, record.modeSub)
            addAdi(adiBuffer, Keys.QSO.POWER_RX, record.powerReportRx)
            addAdi(adiBuffer, Keys.QSO.POWER_TX, record.powerReportTx)
            addAdi(adiBuffer, Keys.QSO.SIGNAL_RX, record.signalReportRx)
            addAdi(adiBuffer, Keys.QSO.SIGNAL_TX, record.signalReportTx)
            addAdi(adiBuffer, Keys.QSO.TIME_START, UtilsLegacy.formatTime(record.timestamp))
            addAdi(adiBuffer, Keys.QSO.END_OF_RECORD)
            adiBuffer.append('\n');
        }

        fun addAdiHeader(context: Context, adiBuffer: StringBuffer, book: Book) {
            adiBuffer.append("${book.title}\n${book.callsign}\n\n")
            addAdi(adiBuffer, Keys.Header.VERSION, Keys.VERSION)
            addAdi(adiBuffer, Keys.Header.TIMESTAMP, "${UtilsLegacy.formatDate(System.currentTimeMillis())} ${UtilsLegacy.formatTime(System.currentTimeMillis())}")
            addAdi(adiBuffer, Keys.Header.PROGRAM_ID, Keys.PROGRAM_ID)
            addAdi(adiBuffer, Keys.Header.PROGRAM_VER, Utils.getAppVersion(context))
            addAdi(adiBuffer, Keys.Header.BOOK_ID, book.id)
            addAdi(adiBuffer, Keys.Header.BOOK_NAME, book.title)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_OWNER, book.callsign)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_GRIDSQUARE, book.gridSquare)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_HAS_MULTIPLE_OPS, book.hasMultipleOps)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_CONTEST_FIELDDAY,book.showContestFieldday)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_LOC_CITY, book.showLocCity)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_LOC_GRID, book.showLocGrid)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_POWER, book.showPower)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_SIGNAL, book.showSignal)
            addAdi(adiBuffer, Keys.Header.BOOK_PREFS_SHOW_TIME_AS_UTC, book.showTimeAsUTC)
            addAdi(adiBuffer, Keys.Header.END_OF_HEADER)
            adiBuffer.append('\n');
        }

        fun addAdi(buffer: StringBuffer, key: String) =
            addAdi(buffer, key, true, null, null)

        fun addAdi(buffer: StringBuffer, key: String, value: Boolean?) =
            addAdi(buffer, key, false, "" + value, null)

        fun addAdi(buffer: StringBuffer, key: String, value: String?) =
            addAdi(buffer, key, false, value, null)

        fun addAdi(buffer: StringBuffer, key: String, value: String?, dataType: Char?) =
            addAdi(buffer, key, false, value, dataType)

        fun addAdi(buffer: StringBuffer, key: String, isValuelessTag: Boolean, value: String?, dataType: Char?) {
            val hasValue = value != null && value.isNotEmpty() && value != "0.0"
            if (isValuelessTag || hasValue) {
                buffer.append('<')
                buffer.append(key)
                if (!isValuelessTag) {
                    buffer.append(':')
                    buffer.append(value!!.length.toString())
                }
                if (dataType != null) {
                    buffer.append(':')
                    buffer.append(dataType.toString())
                }
                buffer.append('>')
                if (!isValuelessTag) {
                    buffer.append(value)
                }
                buffer.append('\n')
            }
        }
    }

}