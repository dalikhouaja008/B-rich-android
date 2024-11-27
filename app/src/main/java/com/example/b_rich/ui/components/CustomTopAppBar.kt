package com.example.b_rich.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar() {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "B-Rich", color = Color.White)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF3D5AFE), // Couleur de fond
            titleContentColor = Color.White, // Couleur du titre
            navigationIconContentColor = Color(0xFF442C2E), // Couleur des icônes
            actionIconContentColor = Color(0xFF442C2E) // Couleur des icônes d'action
        ),
        modifier = Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .windowInsetsPadding(WindowInsets.statusBars)

    )
}