package com.nsi_projekat.ui.uiutil.composables.contextmenu

import androidx.compose.ui.graphics.vector.ImageVector

data class ContextMenuItem(
    val text: String,
    val icon: ImageVector?,
    val function: ContextMenuItemFunctionEnum
)