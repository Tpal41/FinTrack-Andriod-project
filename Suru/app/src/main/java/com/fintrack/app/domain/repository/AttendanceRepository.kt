package com.fintrack.app.domain.repository

import com.fintrack.app.domain.model.AttendanceRecord
import com.fintrack.app.domain.model.Student
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getAllStudents(): Flow<List<Student>>
    suspend fun addStudent(student: Student)
    suspend fun markAttendance(record: AttendanceRecord)
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceRecord>>
    fun getStudentAttendancePercentage(studentId: Long): Flow<Double>
}
