package com.example.b_rich.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class user(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("numTel")
    val numTel: String,
    @SerializedName("password")
    val password: String,
) : Serializable



    
