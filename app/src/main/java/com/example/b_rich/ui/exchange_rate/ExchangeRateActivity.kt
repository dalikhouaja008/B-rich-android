import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.b_rich.R
import com.example.b_rich.data.entities.user
import com.example.b_rich.navigateToCodeVerification
import com.example.b_rich.ui.resetPassword.ResetPasswordViewModel
import com.example.b_rich.ui.sideBar.NavigationDrawer
import com.example.b_rich.ui.theme.PREF_FILE
import com.google.gson.Gson
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.b_rich.ui.sideBar.NavigationItems
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRate(
    user: user,
    navHostController: NavHostController,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    val context = LocalContext.current
    val mSharedPreferences = remember { context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // List of Navigation Items
    val items= listOf(
        NavigationItems(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        NavigationItems(
            title = "Comptes bancaires",
            selectedIcon = Icons.Filled.AccountBalance,
            unselectedIcon = Icons.Outlined.AccountBalance,
            route = "Comptes_bancaires"
        ),
        NavigationItems(
            title = "Wallets",
            selectedIcon = Icons.Filled.Wallet,
            unselectedIcon = Icons.Outlined.Wallet,
            badgeCount = 105,
            route = "edit"
        ),
        NavigationItems(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "signup"
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            scope.launch {
                              navHostController.navigate(items[selectedItemIndex].route)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Text(text = item.badgeCount.toString())
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "B-Rich")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF3D5AFE), Color(0xFFB39DDB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3D5AFE),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Hello there, sign in to continue",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val logo: Painter = painterResource(id = R.drawable.logo)
                    Image(
                        painter = logo,
                        contentDescription = "Logo",
                        modifier = Modifier.size(250.dp)
                    )

                    ClickableText(
                        text = AnnotatedString("log out"),
                        onClick = {
                            val editor = mSharedPreferences.edit()
                            editor.clear()
                            editor.apply()
                            (context as? Activity)?.finishAffinity()
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF3D5AFE),
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ClickableText(
                        text = AnnotatedString("Change Password"),
                        onClick = {
                            viewModel.requestReset(user.email)
                            navigateToCodeVerification(user, navHostController)
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF3D5AFE),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
/*@Preview(showBackground = true)
@Composable
fun PreviewSignInScreen() {
    ExchangeRate()
}*/

/*NavHost(
navController = navHostController,
startDestination = "home",
modifier = Modifier.padding(paddingValues)
) {
    composable("home") {
        HomeScreen()
    }
    composable("info") {
        InfoScreen()
    }
    composable("edit") {
        EditScreen()
    }
    composable("signup") {
        SettingsScreen()
    }
}*/
