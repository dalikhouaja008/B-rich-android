package com.example.b_rich.ui

import ExchangeRate
import ListAccountsViewModel
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
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
import com.example.b_rich.ui.listAccounts.ListAccountsView
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.wallets.WalletsScreen
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable

enum class NavigationBarItems(val icon: ImageVector) {
    Home(icon = Icons.Default.Home),
    Convert(icon = Icons.Default.CurrencyExchange),
    Wallet(icon = Icons.Default.Wallet),
    Account(icon = Icons.Default.AccountBalance),
    ListAccount(icon = Icons.Default.AccountBalance),
    Settings(icon = Icons.Default.Settings)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
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
    listAccountsViewModel: ListAccountsViewModel = viewModel(),
    addAccountViewModel: AddAccountViewModel = viewModel(),
    currencyConverterViewModel: CurrencyConverterViewModel,
    walletsViewModel: WalletsViewModel
) {
    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableStateOf(0) }
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFFFFF), Color(0xFF2196F3))
    )

    LaunchedEffect(Unit) {
        exchangeRateViewModel.fetchExchangeRates()
    }

    Scaffold(
        topBar = { CustomTopAppBar() },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(55.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = Color.White,
                ballColor = Color(0xFF2196F3)
            ) {
                navigationBarItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = item.ordinal },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = if (selectedIndex == item.ordinal) Color(0xFF2196F3) else MaterialTheme.colorScheme.onSurface
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
                .background(gradientBackground)
        ) {
            when (selectedIndex) {
                NavigationBarItems.Home.ordinal -> ExchangeRate(exchangeRateViewModel)
                NavigationBarItems.Convert.ordinal -> CurrencyConverter(currencyConverterViewModel)
                NavigationBarItems.Wallet.ordinal -> WalletsScreen(walletsViewModel)
                NavigationBarItems.Account.ordinal -> AddAccountScreen(viewModel = addAccountViewModel)
                NavigationBarItems.ListAccount.ordinal -> ListAccountsView(
                    viewModel = listAccountsViewModel,
                    onAddAccountClick = { navHostController.navigate("addAccount") }
                )
            }
        }
    }
}
