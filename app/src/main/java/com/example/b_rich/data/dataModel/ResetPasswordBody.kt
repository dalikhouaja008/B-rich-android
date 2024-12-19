package com.example.b_rich.data.dataModel

data class ResetPasswordBody(
    val email: String,
    val code: String,
    val newPassword: String
)