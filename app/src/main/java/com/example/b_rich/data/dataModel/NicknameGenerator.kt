package com.example.b_rich.data.dataModel

object NicknameGenerator {
    private val savingsNicknames = listOf(
        "My Savings",
        "Emergency Fund",
        "Future Projects",
        "Personal Savings",
        "Rainy Day Fund"
    )

    private val checkingNicknames = listOf(
        "Daily Expenses",
        "Main Account",
        "Personal Account",
        "Regular Use",
        "Budget Account"
    )

    private val businessNicknames = listOf(
        "Business Account",
        "Professional Use",
        "Work Account",
        "Company Funds",
        "Business Operations"
    )

    fun getSuggestionsForAccount(type: String): List<String> {
        return when (type.toLowerCase()) {
            "savings" -> savingsNicknames
            "checking" -> checkingNicknames
            "business" -> businessNicknames
            else -> emptyList()
        }
    }
}