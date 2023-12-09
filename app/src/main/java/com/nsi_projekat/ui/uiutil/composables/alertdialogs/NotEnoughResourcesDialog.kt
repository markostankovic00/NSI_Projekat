package com.nsi_projekat.ui.uiutil.composables.alertdialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.nsi_projekat.R
import com.nsi_projekat.ui.theme.spacing

@Composable
fun NotEnoughResourcesDialog(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        buttons = {

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.spacing.large,
                        vertical = MaterialTheme.spacing.medium
                    )
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {

                Text(
                    text = stringResource(id = R.string.alert_dialog_confirm),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .clickable {
                            onConfirmClick()
                        }
                )
            }
        }
    )
}