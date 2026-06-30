package com.fintrack.app.domain.model

data class Student(
    val id: Long = 0,
    val name: String,
    val rollNumber: String,
    val attendancePercentage: Double = 0.0
)

data class AttendanceRecord(
    val id: Long = 0,
    val studentId: Long,
    val date: Long,
    val isPresent: Boolean
)
