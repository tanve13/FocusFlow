package com.tanveer.focusflow.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WeeklyData(val day: String, val sessions: Int)

@HiltViewModel
class InsightsViewModel @Inject constructor() : ViewModel() {

    private val _dailySessions = MutableStateFlow(0)
    val dailySessions: StateFlow<Int> = _dailySessions

    private val _dailyTasksCompleted = MutableStateFlow(0)
    val dailyTasksCompleted: StateFlow<Int> = _dailyTasksCompleted

    private val _weeklyData = MutableStateFlow<List<WeeklyData>>(emptyList())
    val weeklyData: StateFlow<List<WeeklyData>> = _weeklyData

    fun loadData(uid: String) {
        viewModelScope.launch {
            // TODO: Replace with Firestore query/aggregation
            _dailySessions.value = 3
            _dailyTasksCompleted.value = 5
            _weeklyData.value = listOf(
                WeeklyData("Mon", 2),
                WeeklyData("Tue", 4),
                WeeklyData("Wed", 3),
                WeeklyData("Thu", 6),
                WeeklyData("Fri", 7),
                WeeklyData("Sat", 4),
                WeeklyData("Sun", 5)
            )
        }
    }
}
