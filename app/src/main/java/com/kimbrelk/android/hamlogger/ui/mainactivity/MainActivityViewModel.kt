package com.kimbrelk.android.hamlogger.ui.mainactivity

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.kimbrelk.android.hamlogger.ui.BaseViewModel
import kotlinx.coroutines.launch

class MainActivityViewModel : BaseViewModel {

    constructor(application: Application) : super(application) {
        viewModelScope.launch {
            appRepository.upgradeFromLegacyDatabaseIfExists()
        }
    }

}