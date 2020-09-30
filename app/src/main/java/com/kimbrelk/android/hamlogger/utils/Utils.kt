package com.kimbrelk.android.hamlogger.utils

import android.content.Context
import android.database.Cursor
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.kimbrelk.android.hamlogger.data.model.Band
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {

        private val DF_FREQUENCY = DecimalFormat("###,###,###,###,##0.####")
        val BANDS: Band.Bands = Band.Bands()

        fun emptyStringToNull(str: String?) : String? {
            if (str == null || str.isEmpty() || str == "null") {
                return null
            }
            return str
        }

        fun formatFrequency(freq: Double) : String {
            var measurement: String? = null
            var value = freq
            when {
                freq < 0.001 -> {
                    value = freq / 0.000001
                    measurement = "Hz"
                }
                freq < 1.0 -> {
                    value = freq / 0.001
                    measurement = "kHz"
                }
                freq < 1000.0 -> {
                    value = freq / 1.0
                    measurement = "MHz"
                }
                freq < 1000000.0 -> {
                    value = freq / 1000.0
                    measurement = "GHz"
                }
                freq < 1000000000.0 -> {
                    value = freq / 1000000.0
                    measurement = "THz"
                }
            }
            return "${DF_FREQUENCY.format(value)} $measurement"
        }

        fun formatDateReadable(timestamp: Long, timeZone: TimeZone = TimeZone.getDefault()) : String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = timeZone
            return dateFormat.format(Date(timestamp))
        }

        fun formatTimeReadable(timestamp: Long, timeZone: TimeZone = TimeZone.getDefault()) : String {
            val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
            dateFormat.timeZone = timeZone
            return dateFormat.format(Date(timestamp))
        }

        fun getAppVersion(context: Context) : String {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        }

        fun getAppVersionCode(context: Context) : Int {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionCode
        }

        fun getCursorBoolean(cursor: Cursor, colName: String) : Boolean {
            val columnIndex = cursor.getColumnIndexOrThrow(colName)
            return (cursor.getIntOrNull(columnIndex) ?: 0) == 1
        }

        fun getCursorDouble(cursor: Cursor, colName: String) : Double? {
            val columnIndex = cursor.getColumnIndexOrThrow(colName)
            return cursor.getDoubleOrNull(columnIndex)
        }

        fun getCursorLong(cursor: Cursor, colName: String) : Long? {
            val columnIndex = cursor.getColumnIndexOrThrow(colName)
            return cursor.getLongOrNull(columnIndex)
        }

        fun getCursorString(cursor: Cursor, colName: String) : String? {
            val columnIndex = cursor.getColumnIndexOrThrow(colName)
            return cursor.getStringOrNull(columnIndex)
        }

        fun getLegacyTimestamp(date: String?, time: String?) : Long {
            if (date == null || time == null || date.isEmpty() || time.isEmpty()) {
                return System.currentTimeMillis()
            }
            val year = date.substring(0, 4).toInt()
            val month = date.substring(4, 6).toInt() - 1
            val day = date.substring(6, 8).toInt()
            val hour = time.substring(0, 2).toInt()
            val min = time.substring(2, 4).toInt()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.set(year, month, day, hour, min, 0)
            val msInMin = 1000 * 60
            return (calendar.timeInMillis / msInMin) * msInMin
        }

        fun stringToDouble(str: String?) : Double {
            return str?.toDoubleOrNull() ?: return 0.0
        }
    }
}