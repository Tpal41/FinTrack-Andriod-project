package com.fintrack.app.domain.model

enum class DateFilter {
    ALL,
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    CUSTOM
}

enum class TransactionSort {
    NEWEST,
    OLDEST,
    HIGHEST_AMOUNT,
    LOWEST_AMOUNT
}

data class TransactionQuery(
    val search: String = "",
    val type: TransactionType? = null,
    val category: String? = null,
    val dateFilter: DateFilter = DateFilter.ALL,
    val customStart: Long? = null,
    val customEnd: Long? = null,
    val sort: TransactionSort = TransactionSort.NEWEST
)
