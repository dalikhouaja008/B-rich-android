package com.example.b_rich.ui.sideBar

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItems(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: String = ""  // Route pour la navigation
)