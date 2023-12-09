package com.nsi_projekat.ui.uiutil.composables.alertdialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.nsi_projekat.R
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.PrimaryOutlinedTextField

@Composable
fun CashDialog(
    modifier: Modifier = Modifier,
    title: String,
    amountOfCash: String,
    onDismissRequest: () -> Unit,
    onValueChange: (amountOfCash: String) -> Unit,
    onCancelClicked: () -> Unit,
    onConfirmClicked: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(title)
        },
        text = {
            Text(stringResource(id = R.string.alert_dialog_cash_amount_dialog_text))
        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                PrimaryOutlinedTextField(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                    textStateValue = amountOfCash,
                    onValueChange = onValueChange,
                    label = stringResource(id = R.string.alert_dialog_currency_label),
                    keyboardType = KeyboardType.Number
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onCancelClicked() }
                            .padding(vertical = MaterialTheme.spacing.medium),
                        text = stringResource(id = R.string.alert_dialog_cancel),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = stringResource(id = R.string.alert_dialog_confirm),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onConfirmClicked() }
                            .padding(vertical = MaterialTheme.spacing.medium)
                    )
                }

            }
        }
    )
}