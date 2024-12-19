package com.example.b_rich.data.dataModel

import com.example.b_rich.data.entities.AddAccount

data class ApiResponse(
    val message: String,
    val data: AddAccount
)