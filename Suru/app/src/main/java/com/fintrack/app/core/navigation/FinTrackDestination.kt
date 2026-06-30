package com.fintrack.app.core.navigation

sealed class FinTrackDestination(val route: String) {
    data object Splash : FinTrackDestination("splash")
    data object Dashboard : FinTrackDestination("dashboard")
    data object Transactions : FinTrackDestination("transactions")
    data object Analytics : FinTrackDestination("analytics")
    data object Budgets : FinTrackDestination("budgets")
    data object Goals : FinTrackDestination("goals")
    data object Recurring : FinTrackDestination("recurring")
    data object Settings : FinTrackDestination("settings")
    data object Attendance : FinTrackDestination("attendance")
    data object ProfessorLogin : FinTrackDestination("professor_login")
}
