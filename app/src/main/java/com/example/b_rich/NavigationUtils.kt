package com.example.b_rich


import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.google.gson.Gson
import android.net.Uri

fun navigateToExchangeRate(user: user, navController: NavHostController) {
    val userJson = Uri.encode(Gson().toJson(user))
    navController.navigate("exchangeRate/$userJson")
}

fun navigateToCodeVerification(user: user, navController: NavHostController) {
    val userJson = Uri.encode(Gson().toJson(user))
    navController.navigate("codeVerification/$userJson")
}