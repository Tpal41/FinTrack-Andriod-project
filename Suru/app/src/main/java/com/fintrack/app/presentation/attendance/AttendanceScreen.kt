package com.fintrack.app.presentation.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fintrack.app.domain.model.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val students by viewModel.students.collectAsState()
    var showAddStudentDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Attendance Tracker") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddStudentDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
            }
        }
    ) { padding ->
        if (students.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No students added yet. Click + to add.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(students) { student ->
                    StudentAttendanceItem(
                        student = student,
                        onMarkAttendance = { isPresent ->
                            viewModel.markAttendance(student.id, isPresent)
                        },
                        percentageFlow = viewModel.getAttendancePercentage(student.id)
                    )
                }
            }
        }

        if (showAddStudentDialog) {
            AddStudentDialog(
                onDismiss = { showAddStudentDialog = false },
                onConfirm = { name, roll ->
                    if (name.isNotBlank() && roll.isNotBlank()) {
                        viewModel.addStudent(name, roll)
                        showAddStudentDialog = false
                    }
                }
            )
        }
    }
}

@Composable
fun StudentAttendanceItem(
    student: Student,
    onMarkAttendance: (Boolean) -> Unit,
    percentageFlow: kotlinx.coroutines.flow.Flow<Double>
) {
    val percentage by percentageFlow.collectAsState(initial = 0.0)

    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = student.name, style = MaterialTheme.typography.titleLarge)
                    Text(text = "Roll: ${student.rollNumber}", style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = "${"%.1f".format(percentage)}%",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { onMarkAttendance(false) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Absent")
                }
                Button(onClick = { onMarkAttendance(true) }) {
                    Text("Present")
                }
            }
        }
    }
}

@Composable
fun AddStudentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var roll by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Student") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Student Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = roll,
                    onValueChange = { roll = it },
                    label = { Text("Roll Number") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, roll) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
