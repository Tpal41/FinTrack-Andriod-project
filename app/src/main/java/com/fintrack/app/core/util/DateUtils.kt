package com.fintrack.app.core.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.toDisplayDate(): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(this)

fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1

fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

fun startOfToday(): Long = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

fun startOfWeek(): Long = Calendar.getInstance().apply {
    firstDayOfWeek = Calendar.MONDAY
    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

fun startOfMonth(): Long = Calendar.getInstance().apply {
    set(Calendar.DAY_OF_MONTH, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

fun endOfToday(): Long = startOfToday() + 86_399_999L

fun monthName(month: Int): String = SimpleDateFormat("MMM", Locale.getDefault())
    .format(Calendar.getInstance().apply { set(Calendar.MONTH, month - 1) }.time)
