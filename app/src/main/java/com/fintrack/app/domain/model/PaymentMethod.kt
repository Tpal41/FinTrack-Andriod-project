package com.fintrack.app.domain.model

enum class PaymentMethod(val label: String) {
    CASH("Cash"),
    UPI("UPI"),
    DEBIT_CARD("Debit Card"),
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer"),
    WALLET("Wallet")
}

fun paymentMethodLabels(): List<String> = PaymentMethod.entries.map { it.label }
