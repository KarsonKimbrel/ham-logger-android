package com.kimbrelk.android.hamlogger.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.kimbrelk.android.hamlogger.data.AppRepository
import com.kimbrelk.android.hamlogger.data.model.ModeSubModePair
import com.kimbrelk.android.hamlogger.data.legacydatabase.model.Mode
import com.kimbrelk.android.hamlogger.utils.Constants

abstract class BaseViewModel : AndroidViewModel {

    protected val prefs: SharedPreferences
    protected val appRepository: AppRepository

    constructor(application: Application) : super(application) {
        prefs = application.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        appRepository = AppRepository.getInstance(application)
    }

    fun isPro() : Boolean {
        return true
    }

    var prefLastFrequency: Double
        get() = prefs.getString(Constants.Prefs.LAST_FREQ, "0.0")!!.toDouble()
        set(value) = prefs.edit().putString(Constants.Prefs.LAST_FREQ, value.toString()).apply()

    var prefLastMode: ModeSubModePair
        get() =
            ModeSubModePair(
                Mode.valueOf(prefs.getString(Constants.Prefs.LAST_MODE, Mode.AM.name)!!),
                prefs.getString(Constants.Prefs.LAST_MODE_SUB, null)
            )
        set(value) =
            prefs.edit()
                .putString(Constants.Prefs.LAST_MODE, value.mode.name)
                .putString(Constants.Prefs.LAST_MODE_SUB, value.subMode)
                .apply()

    var prefLastOperator: String?
        get() {
            val saved = prefs.getString(Constants.Prefs.LAST_OPERATOR, null)
            return if (saved == "null") null else saved
        }
        set(value) = prefs.edit().putString(Constants.Prefs.LAST_OPERATOR, value).apply()

}