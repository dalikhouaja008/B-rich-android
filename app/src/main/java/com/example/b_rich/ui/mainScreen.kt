package com.example.b_rich.ui

import ExchangeRate
import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.data.entities.user
import com.example.b_rich.ui.AddAccount.AddAccountScreen
import com.example.b_rich.ui.AddAccount.AddAccountViewModel
import com.example.b_rich.ui.components.ExchangeRateComponents.ExchangeRateList
import com.example.b_rich.ui.components.ExchangeRateComponents.ExpandedDropdownUi
import com.example.b_rich.ui.components.TextfieldsComponenets.InputTextFieldUi
import com.example.b_rich.ui.exchange_rate.ExchangeRateViewModel
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.theme.PREF_FILE
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.ShapeCornerRadius
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable

enum class NavigationBarItems(val icon: ImageVector){
    Home(icon = Icons.Default.Home),
    Wallet(icon = Icons.Default.Wallet),
    Person(icon=Icons.Default.Person),
    Account(icon = Icons.Default.AccountBalance),
    Settings(icon = Icons.Default.Settings)
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
    addAccountViewModel: AddAccountViewModel =viewModel()
) {
    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val uiState by exchangeRateViewModel.uiState.collectAsState()
    val uiStateCurrency by exchangeRateViewModel.uiStateCurrency.collectAsState()
    val mSharedPreferences =
        remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        exchangeRateViewModel.fetchExchangeRates()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "B-Rich", color = Color.White) // Set title text color to white
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Red,
                    //contentColor = Color.White
                )
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier
                    .height(55.dp)
                    .offset(y = (-50).dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor =  Color(0xFFF44336),
                ballColor = Color(0xFFF44336),

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
                NavigationBarItems.Home.ordinal -> ExchangeRate(
                    user,
                    navHostController,
                    viewModel,
                    exchangeRateViewModel
                )
                //NavigationBarItems.Wallet.ordinal -> SettingsScreen()
                NavigationBarItems.Account.ordinal -> AddAccountScreen(
                    navHostController = navHostController,
                    drawerState = drawerState,
                    viewModel = addAccountViewModel
                )
                //NavigationBarItems.Person.ordinal -> SettingsScreen()
                //NavigationBarItems.Settings.ordinal -> SettingsScreen()
            }

        }
    }
}

