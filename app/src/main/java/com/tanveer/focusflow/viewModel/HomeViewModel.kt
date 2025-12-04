package com.tanveer.focusflow.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanveer.focusflow.data.datastore.SettingDataStore
import com.tanveer.focusflow.data.firestore.SessionRepository
import com.tanveer.focusflow.data.model.FocusSession
import com.tanveer.focusflow.data.model.SessionMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val settings: SettingDataStore,
    private val sessionRepo: SessionRepository
) : ViewModel() {

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _isFocusMode = MutableStateFlow(true)
    val isFocusMode: StateFlow<Boolean> = _isFocusMode

    private val _timeLeftSec = MutableStateFlow(25 * 60)
    val timeLeftSec: StateFlow<Int> = _timeLeftSec

    private var totalSec = 25 * 60
    private val internalRunning = AtomicBoolean(false)

    init {
        viewModelScope.launch {
            settings.focusDurationFlow.collect { minutes ->
                if (_isFocusMode.value) {
                    totalSec = minutes * 60
                    _timeLeftSec.value = totalSec
                }
            }
        }
        viewModelScope.launch {
            settings.breakDurationFlow.collect { minutes ->
                if (!_isFocusMode.value) {
                    totalSec = minutes * 60
                    _timeLeftSec.value = totalSec
                }
            }
        }
    }

    fun toggleStartStop() {
        if (_isRunning.value) stopTimer() else startTimer()
    }

    private fun startTimer() {
        _isRunning.value = true
        internalRunning.set(true)
        viewModelScope.launch {
            while (internalRunning.get() && _timeLeftSec.value > 0) {
                delay(1000L)
                _timeLeftSec.value = (_timeLeftSec.value - 1).coerceAtLeast(0)
            }
            if (_timeLeftSec.value == 0) {
                // session complete
                _isRunning.value = false
                internalRunning.set(false)
                onSessionComplete()
            }
        }
    }

    private fun stopTimer() {
        internalRunning.set(false)
        _isRunning.value = false
    }

    fun resetTimer() {
        viewModelScope.launch {
            val minutes = if (_isFocusMode.value) settings.focusDurationFlow else settings.breakDurationFlow
            // pick latest
            minutes.collect { m ->
                totalSec = m * 60
                _timeLeftSec.value = totalSec
                cancel() // stop collector
            }
        }
    }

    private fun onSessionComplete() {
        viewModelScope.launch {
            // save session to firestore (stub) - replace with actual uid
            val session = FocusSession(
                userId = "current_uid",
                mode = if (_isFocusMode.value) SessionMode.FOCUS else SessionMode.BREAK,
                durationSec = totalSec
            )
            try {
                sessionRepo.saveSession(session.userId, session)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
            // flip mode
            _isFocusMode.value = !_isFocusMode.value
            // set new total sec
            val newMinutes = if (_isFocusMode.value) settings.focusDurationFlow else settings.breakDurationFlow
            newMinutes.collect { m ->
                totalSec = m * 60
                _timeLeftSec.value = totalSec
                cancel()
            }
        }
    }
    fun currentFocusSec(): Int {
        // return focus duration in seconds from SettingDataStore or cached value
        return _focusDurationSec ?: 25*60 // define _focusDurationSec var
    }

    fun currentBreakSec(): Int = _breakDurationSec ?: 5*60

    private var _focusDurationSec: Int? = null
    private var _breakDurationSec: Int? = null

    init {
        viewModelScope.launch {
            settings.focusDurationFlow.collect { minutes ->
                _focusDurationSec = minutes * 60
                if (_isFocusMode.value) {
                    totalSec = _focusDurationSec ?: (25*60)
                    _timeLeftSec.value = totalSec
                }
            }
        }
        viewModelScope.launch {
            settings.breakDurationFlow.collect { minutes ->
                _breakDurationSec = minutes * 60
                if (!_isFocusMode.value) {
                    totalSec = _breakDurationSec ?: (5*60)
                    _timeLeftSec.value = totalSec
                }
            }
        }
    }
}
