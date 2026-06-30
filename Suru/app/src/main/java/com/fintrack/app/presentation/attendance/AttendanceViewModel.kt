package com.fintrack.app.presentation.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fintrack.app.domain.model.AttendanceRecord
import com.fintrack.app.domain.model.Student
import com.fintrack.app.domain.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val repository: AttendanceRepository
) : ViewModel() {

    private val _students = repository.getAllStudents().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    val students = _students

    fun addStudent(name: String, rollNumber: String) {
        viewModelScope.launch {
            repository.addStudent(Student(name = name, rollNumber = rollNumber))
        }
    }

    fun markAttendance(studentId: Long, isPresent: Boolean) {
        viewModelScope.launch {
            repository.markAttendance(
                AttendanceRecord(
                    studentId = studentId,
                    date = System.currentTimeMillis(),
                    isPresent = isPresent
                )
            )
        }
    }

    fun getAttendancePercentage(studentId: Long) = repository.getStudentAttendancePercentage(studentId)
}
