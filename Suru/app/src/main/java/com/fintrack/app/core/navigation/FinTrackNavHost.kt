package com.fintrack.app.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fintrack.app.presentation.analytics.AnalyticsRoute
import com.fintrack.app.presentation.attendance.AttendanceScreen
import com.fintrack.app.presentation.attendance.ProfessorLoginScreen
import com.fintrack.app.presentation.budget.BudgetRoute
import com.fintrack.app.presentation.dashboard.DashboardRoute
import com.fintrack.app.presentation.goals.GoalsRoute
import com.fintrack.app.presentation.recurring.RecurringRoute
import com.fintrack.app.presentation.settings.SettingsRoute
import com.fintrack.app.presentation.splash.SplashRoute
import com.fintrack.app.presentation.transaction.TransactionsRoute

@Composable
fun FinTrackNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = currentDestination?.route != FinTrackDestination.Splash.route && 
                       currentDestination?.route != FinTrackDestination.ProfessorLogin.route
    
    val bottomItems = listOf(
        BottomNavItem(FinTrackDestination.Dashboard, "Home", Icons.Rounded.Home),
        BottomNavItem(FinTrackDestination.ProfessorLogin, "Attendance", Icons.Rounded.School),
        BottomNavItem(FinTrackDestination.Transactions, "Transactions", Icons.Rounded.List),
        BottomNavItem(FinTrackDestination.Analytics, "Analytics", Icons.Rounded.BarChart),
        BottomNavItem(FinTrackDestination.Budgets, "Budgets", Icons.Rounded.AccountBalanceWallet),
        BottomNavItem(FinTrackDestination.Goals, "Goals", Icons.Rounded.Flag),
        BottomNavItem(FinTrackDestination.Recurring, "Recurring", Icons.Rounded.Repeat),
        BottomNavItem(FinTrackDestination.Settings, "Settings", Icons.Rounded.Settings)
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true ||
                                     (item.destination == FinTrackDestination.ProfessorLogin && currentDestination?.route == FinTrackDestination.Attendance.route)

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.destination.route) {
                                    popUpTo(FinTrackDestination.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = FinTrackDestination.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(FinTrackDestination.Splash.route) {
                SplashRoute(
                    onSplashFinished = {
                        navController.navigate(FinTrackDestination.Dashboard.route) {
                            popUpTo(FinTrackDestination.Splash.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(FinTrackDestination.Dashboard.route) {
                DashboardRoute(onAddTransaction = { navController.navigate(FinTrackDestination.Transactions.route) })
            }
            composable(FinTrackDestination.Transactions.route) {
                TransactionsRoute()
            }
            composable(FinTrackDestination.Analytics.route) {
                AnalyticsRoute()
            }
            composable(FinTrackDestination.Budgets.route) {
                BudgetRoute()
            }
            composable(FinTrackDestination.Goals.route) {
                GoalsRoute()
            }
            composable(FinTrackDestination.Recurring.route) {
                RecurringRoute()
            }
            composable(FinTrackDestination.Settings.route) {
                SettingsRoute()
            }
            composable(FinTrackDestination.ProfessorLogin.route) {
                ProfessorLoginScreen(
                    onLoginSuccess = {
                        navController.navigate(FinTrackDestination.Attendance.route) {
                            popUpTo(FinTrackDestination.ProfessorLogin.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(FinTrackDestination.Attendance.route) {
                AttendanceScreen()
            }
        }
    }
}

private data class BottomNavItem(
    val destination: FinTrackDestination,
    val label: String,
    val icon: ImageVector
)
