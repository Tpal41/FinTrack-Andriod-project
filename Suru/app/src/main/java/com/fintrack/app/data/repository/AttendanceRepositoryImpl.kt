package com.fintrack.app.data.repository

import com.fintrack.app.data.local.dao.AttendanceDao
import com.fintrack.app.data.local.entity.AttendanceEntity
import com.fintrack.app.data.local.entity.StudentEntity
import com.fintrack.app.domain.model.AttendanceRecord
import com.fintrack.app.domain.model.Student
import com.fintrack.app.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceDao: AttendanceDao
) : AttendanceRepository {

    override fun getAllStudents(): Flow<List<Student>> {
        return attendanceDao.getAllStudents().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addStudent(student: Student) {
        attendanceDao.insertStudent(student.toEntity())
    }

    override suspend fun markAttendance(record: AttendanceRecord) {
        attendanceDao.markAttendance(record.toEntity())
    }

    override fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceRecord>> {
        return attendanceDao.getAttendanceForStudent(studentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStudentAttendancePercentage(studentId: Long): Flow<Double> {
        return combine(
            attendanceDao.getPresentCount(studentId),
            attendanceDao.getTotalCount(studentId)
        ) { present, total ->
            if (total == 0) 0.0 else (present.toDouble() / total.toDouble()) * 100.0
        }
    }

    private fun StudentEntity.toDomain() = Student(
        id = id,
        name = name,
        rollNumber = rollNumber
    )

    private fun Student.toEntity() = StudentEntity(
        id = id,
        name = name,
        rollNumber = rollNumber
    )

    private fun AttendanceEntity.toDomain() = AttendanceRecord(
        id = id,
        studentId = studentId,
        date = date,
        isPresent = isPresent
    )

    private fun AttendanceRecord.toEntity() = AttendanceEntity(
        id = id,
        studentId = studentId,
        date = date,
        isPresent = isPresent
    )
}
