package com.example.b_rich.ui

import ExchangeRate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.ui.AddAccount.AddAccountScreen
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.components.CustomTopAppBar
import com.example.b_rich.ui.currency_converter.CurrencyConverter
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.wallets.Wallets
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius

enum class NavigationBarItems(val icon: ImageVector) {
    Home(icon = Icons.Default.Home),
    Convert(icon = Icons.Default.CurrencyExchange),
    Wallet(icon = Icons.Default.Wallet),
    Person(icon = Icons.Default.Person),
    Account(icon = Icons.Default.AccountBalance),
    Settings(icon = Icons.Default.Settings)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    user: user,
    navHostController: NavHostController,
    viewModel: ResetPasswordViewModel = viewModel(),
    exchangeRateViewModel: ExchangeRateViewModel = viewModel(),
    addAccountViewModel: AddAccountViewModel = viewModel(),
    currencyConverterViewModel: CurrencyConverterViewModel,
    walletsViewModel: WalletsViewModel = viewModel()
) {
    // Fetch data on screen load
    LaunchedEffect(key1 = true) {
        walletsViewModel.fetchWallets()
        walletsViewModel.fetchRecentTransactions()
    }

    // Observing wallets and transactions
    val wallets by walletsViewModel.wallets.collectAsState()
    val recentTransactions by walletsViewModel.recentTransactions.collectAsState()
    val totalBalance by walletsViewModel.totalBalance.collectAsState()

    var selectedIndex by remember { mutableStateOf(0) }
    val navigationBarItems = remember { NavigationBarItems.values() }

    Scaffold(
        topBar = {
            CustomTopAppBar()
        },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier
                    .height(55.dp)
                    .padding(bottom = 16.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = Color(0xFF3D5AFE),
                ballColor = Color(0xFF3D5AFE)
            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = item.ordinal },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = item.icon,
                            contentDescription = "Bottom bar Icon",
                            tint = if (selectedIndex == item.ordinal)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedIndex) {
                NavigationBarItems.Home.ordinal -> ExchangeRate(exchangeRateViewModel)
                NavigationBarItems.Convert.ordinal -> CurrencyConverter(currencyConverterViewModel)
                NavigationBarItems.Wallet.ordinal -> {
                    Wallets(
                        totalBalance = totalBalance,
                        wallets = wallets,
                        recentTransactions = recentTransactions
                    )
                }
                NavigationBarItems.Account.ordinal -> AddAccountScreen(
                    navHostController = navHostController,
                    drawerState = rememberDrawerState(DrawerValue.Closed),
                    viewModel = addAccountViewModel
                )
                NavigationBarItems.Settings.ordinal -> {
                    // Placeholder for settings screen if needed
                }
            }
        }
    }
}
