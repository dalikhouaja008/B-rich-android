package com.example.b_rich.ui


import ExchangeRate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.b_rich.ui.listAccounts.ListAccountsScreen
import com.example.b_rich.ui.listAccounts.ListAccountsViewModel
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.wallets.WalletsScreen
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius

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
    addAccountViewModel: AddAccountViewModel = viewModel(),
    currencyConverterViewModel: CurrencyConverterViewModel,
    walletsViewModel: WalletsViewModel,
    listAccountsViewModel: ListAccountsViewModel
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
                            contentDescription = item.name,
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
                NavigationBarItems.Account.ordinal -> AddAccountScreen(
                   viewModel = addAccountViewModel,
                    onBackToAccounts = { selectedIndex = NavigationBarItems.ListAccount.ordinal }
                )
                NavigationBarItems.ListAccount.ordinal -> ListAccountsScreen(
                    viewModel = listAccountsViewModel,
                    onAddAccountClick = { selectedIndex = NavigationBarItems.Account.ordinal }
                )
                NavigationBarItems.Settings.ordinal -> {
                    // Handle Settings screen when implemented
                }
            }
        }
    }
}