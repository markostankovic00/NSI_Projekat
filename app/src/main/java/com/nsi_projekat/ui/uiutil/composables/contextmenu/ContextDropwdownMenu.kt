package com.nsi_projekat.ui.uiutil.composables.contextmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nsi_projekat.ui.theme.BackgroundPatternColor
import com.nsi_projekat.ui.theme.ContextMenuSeparatorColor
import com.nsi_projekat.ui.theme.spacing

@Composable
fun ContextDropdownMenu(
    modifier: Modifier = Modifier,
    dropDownItems: List<ContextMenuItem>,
    isContextMenuVisible: Boolean,
    onIconClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onItemClick: (ContextMenuItem) -> Unit
) {

    Box(
        modifier = modifier
    ) {

        Icon(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.small)
                .scale(1.5f)
                .clip(MaterialTheme.shapes.small)
                .clickable { onIconClick() }
                .align(Alignment.Center),
            tint = MaterialTheme.colors.onSurface,
            imageVector = Icons.Filled.MoreHoriz,
            contentDescription = "Dropdown menu indicator"
        )


        DropdownMenu(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            BackgroundPatternColor
                        )
                    ),
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 2.dp),
            expanded = isContextMenuVisible,
            onDismissRequest = { onDismissRequest() }
        ) {

            dropDownItems.forEachIndexed {index, item ->

                DropdownMenuItem(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small),
                    onClick = { onItemClick(item) }
                ) {

                    Text(
                        text = item.text,
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.button
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (item.icon != null) {
                        Icon(
                            modifier = Modifier
                                .padding(start = MaterialTheme.spacing.large),
                            imageVector = item.icon,
                            contentDescription = "Dropdown menu item icon"
                        )
                    }
                }

                if (index != dropDownItems.lastIndex)
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ContextMenuSeparatorColor)
                    )
            }
        }
    }
}