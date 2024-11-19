package com.example.b_rich.ui.sideBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Composant pour le contenu du drawer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    items: List<NavigationItems>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header du drawer
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Menu",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Liste des items
        items.forEachIndexed { index, item ->
            DrawerItem(
                item = item,
                selected = index == selectedItemIndex,
                onItemClick = { onItemSelected(index) }
            )
        }
    }
}