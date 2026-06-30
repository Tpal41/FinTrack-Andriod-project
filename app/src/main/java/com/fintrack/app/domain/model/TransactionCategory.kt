package com.fintrack.app.domain.model

enum class ExpenseCategory(val label: String) {
    FOOD("Food"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    ENTERTAINMENT("Entertainment"),
    EDUCATION("Education"),
    HEALTH("Health"),
    MISCELLANEOUS("Miscellaneous")
}

enum class IncomeCategory(val label: String) {
    SALARY("Salary"),
    FREELANCE("Freelance"),
    BUSINESS("Business"),
    POCKET_MONEY("Pocket Money"),
    INVESTMENTS("Investments"),
    OTHER("Other")
}

fun expenseCategoryLabels(): List<String> = ExpenseCategory.entries.map { it.label }

fun incomeCategoryLabels(): List<String> = IncomeCategory.entries.map { it.label }
