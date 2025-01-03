package com.example.b_rich.ui

import ExchangeRate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.components.CustomTopAppBar
import com.example.b_rich.ui.currency_converter.CurrencyConverter
import com.example.b_rich.ui.currency_converter.CurrencyConverterViewModel
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.listAccounts.ListAccountsScreen
import com.example.b_rich.ui.listAccounts.ListAccountsViewModel
import com.example.b_rich.ui.profil.ProfilePage
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.wallets.WalletsViewModel
import com.example.b_rich.ui.wallets.components.walletsSection.WalletsScreen
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable


enum class NavigationBarItems(val icon: ImageVector){
    Home(icon = Icons.Default.Home),
    Convert(icon= Icons.Default.CurrencyExchange),
    Wallet(icon = Icons.Default.Wallet),
    Account(icon = Icons.Default.AccountBalance),
    Person(icon=Icons.Default.Person),
}

fun Modifier.noRippleClickable (onClick: ()-> Unit):Modifier = composed{
    clickable (
        indication = null,
        interactionSource = remember { MutableInteractionSource() }){
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
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFF2196F3)
        )
    )

    Scaffold(
        topBar = { CustomTopAppBar() },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier
                    .height(100.dp)
                    .padding(vertical = 20.dp)
                    .offset(y = (-50).dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = Color(0xFF3D5AFE), // Couleur principale de la barre (bleu ou votre choix)
                ballColor = Color(0xFF3D5AFE), // Couleur de la balle animée
            ) {
                navigationBarItems.forEachIndexed { index, item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(if (selectedIndex == index) 36.dp else 32.dp)
                                    .background(
                                        if (selectedIndex == index) Color(0xFFFFFFFF).copy(alpha = 0.1f)
                                        else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .padding(8.dp),
                                imageVector = item.icon,
                                contentDescription = "Bottom bar Icon",
                                tint = if (selectedIndex == index)
                                    Color(0xFFFFFFFF) // Icône active en blanc
                                else
                                    Color(0xFFBBDEFB) // Icône inactive bleu clair
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.name,
                                fontSize = 12.sp,
                                fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedIndex == index) Color(0xFFFFFFFF) else Color(0xFFBBDEFB)
                            )
                        }
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
                NavigationBarItems.Wallet.ordinal -> WalletsScreen(walletsViewModel, currencyConverterViewModel)
                NavigationBarItems.Account.ordinal ->  ListAccountsScreen(
                    viewModel = listAccountsViewModel,
                    onAddAccountClick = { navHostController.navigate("addAccount") }
                )
                NavigationBarItems.Person.ordinal -> ProfilePage(user,viewModel)
            }
        }
    }
}





