package com.tanveer.focusflow.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.datastore.SettingDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerSettingsViewModel @Inject constructor(
    private val dataStore: SettingDataStore
) : ViewModel() {

    val focusMinutes = dataStore.focusDurationFlow
    val breakMinutes = dataStore.breakDurationFlow

    fun setFocusMinutes(min: Int) {
        viewModelScope.launch {
            dataStore.setFocusMinutes(min)
        }
    }

    fun setBreakMinutes(min: Int) {
        viewModelScope.launch {
            dataStore.setBreakMinutes(min)
        }
    }
}

