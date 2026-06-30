package com.fintrack.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fintrack.app.data.local.entity.AttendanceEntity
import com.fintrack.app.data.local.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)

    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance WHERE studentId = :studentId")
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE date = :date")
    fun getAttendanceByDate(date: Long): Flow<List<AttendanceEntity>>
    
    @Query("SELECT COUNT(*) FROM attendance WHERE studentId = :studentId AND isPresent = 1")
    fun getPresentCount(studentId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM attendance WHERE studentId = :studentId")
    fun getTotalCount(studentId: Long): Flow<Int>
}
