package com.example.b_rich.ui.sideBar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/**
 * Composant représentant un item individuel dans le drawer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItem(
    item: NavigationItems,
    selected: Boolean,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        label = { Text(text = item.title) },
        selected = selected,
        onClick = onItemClick,
        icon = {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = "${item.title} Icon"
            )
        },
        badge = {
            item.badgeCount?.let {
                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        modifier = modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}