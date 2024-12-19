package com.example.b_rich.ui.listAccounts

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.b_rich.data.entities.CustomAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAccountsScreen(
    viewModel: ListAccountsViewModel,
    onAddAccountClick: () -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()
    val selectedAccount by viewModel.selectedAccount.collectAsState()
    var currentDotIndex by remember { mutableStateOf(0) }

    val primaryGradient = listOf(
        Color(0xFF6200EE), // Primary Purple
        Color(0xFF3700B3)  // Darker Purple
    )

    // Create a LazyListState to track the scroll position of the LazyRow
    val lazyListState = rememberLazyListState()

    // Update the current dot index based on the scroll position
    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        currentDotIndex = lazyListState.firstVisibleItemIndex
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            AnimatedHeader(
                onAddAccountClick = onAddAccountClick,
                gradientColors = primaryGradient
            )

            AccountsCarousel(
                accounts = accounts,
                selectedAccount = selectedAccount,
                onAccountSelected = { viewModel.selectAccount(it) },
                currentDotIndex = currentDotIndex,
                lazyListState = lazyListState // Pass the state to the carousel
            )

            AnimatedAccountDetails(
                selectedAccount = selectedAccount,
                onToggleDefault = { viewModel.toggleDefault(it) },
                gradientColors = primaryGradient
            )
        }
    }
}

@Composable
private fun AnimatedHeader(
    onAddAccountClick: () -> Unit,
    gradientColors: List<Color>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = gradientColors[0].copy(alpha = 0.25f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Changed from SpaceEvenly
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text content
            Column {
                Text(
                    "Your Accounts",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Manage your finances",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // FAB
            FloatingActionButton(
                onClick = onAddAccountClick,
                containerColor = gradientColors[0],
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        }
    }
}

@Composable
private fun AccountsCarousel(
    accounts: List<CustomAccount>,
    selectedAccount: CustomAccount?,
    onAccountSelected: (CustomAccount) -> Unit,
    currentDotIndex: Int,
    lazyListState: LazyListState
) {
    // Changed from Box to Column
    Column(
        modifier = Modifier
            .height(260.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Center the dots
    ) {
        // LazyRow for cards
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .weight(1f), // This will take up available space
            state = lazyListState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accounts) { account ->
                EnhancedAccountCard(
                    account = account,
                    isSelected = selectedAccount?.id == account.id,
                    onClick = { onAccountSelected(account) }
                )
            }
        }

        // Dots indicator will now appear below the cards
        DotsIndicator(
            totalDots = accounts.size.coerceAtMost(3),
            selectedIndex = currentDotIndex
        )
    }
}

@Composable
private fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(totalDots) { index ->
            val size by animateDpAsState(
                targetValue = if (index == selectedIndex) 12.dp else 8.dp,
                animationSpec = tween(durationMillis = 200),
                label = "dot size"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(size)
                    .clip(CircleShape)
                    .then(
                        if (index == selectedIndex) {
                            // For selected dot - use brush with shape
                            Modifier.background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                                ),
                                shape = CircleShape
                            )
                        } else {
                            // For unselected dot - use solid color
                            Modifier.background(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                        }
                    )
            )
        }
    }
}


@Composable
private fun EnhancedAccountCard(
    account: CustomAccount,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cornerRadius = 24.dp
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 4.dp,
        label = "card elevation"
    )

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(160.dp)
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = Color(0xFF9C27B0).copy(alpha = 0.3f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        if (isSelected) {
                            listOf(Color(0xFF6200EE), Color(0xFF3700B3))
                        } else {
                            listOf(Color(0xFF9C27B0), Color(0xFF6200EE))
                        }
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top section with icon and nickname
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(32.dp)
                    )

                    // Display nickname with RIB fallback
                    Text(
                        text = account.nickname ?: "Account ${account.rib.takeLast(4)}",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                // Bottom section with RIB and balance
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Display masked RIB
                    Text(
                        text = "RIB: ****${account.rib.takeLast(4)}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    // Display balance
                    Text(
                        text = "${account.balance ?: 0.0} TND",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Default account indicator if applicable
                    if (account.isDefault == true) {
                        Text(
                            text = "Default Account",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedAccountDetails(
    selectedAccount: CustomAccount?,
    onToggleDefault: (CustomAccount) -> Unit,
    gradientColors: List<Color>
) {
    AnimatedVisibility(
        visible = selectedAccount != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        selectedAccount?.let { account ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Account Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    account.balance?.let { BalanceSection(balance = it) }

                    DefaultAccountToggle(
                        isDefault = account.isDefault ?: false,
                        onToggle = { onToggleDefault(account) }
                    )

                    TopUpButton(gradientColors)
                }
            }
        }
    }
}

@Composable
private fun BalanceSection(balance: Double) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "Current Balance",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            "$balance TND",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
private fun DefaultAccountToggle(
    isDefault: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Default Account",
            style = MaterialTheme.typography.bodyLarge
        )
        Switch(
            checked = isDefault,
            onCheckedChange = { onToggle() }
        )
    }
}

@Composable
private fun TopUpButton(gradientColors: List<Color>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { Log.d("TopUp", "Top-up clicked!") },  // Debugging the click
        color = Color(0xFF6200EE)  // Temporarily set to a visible color for troubleshooting
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Top Up",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Top-Up Wallet",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
