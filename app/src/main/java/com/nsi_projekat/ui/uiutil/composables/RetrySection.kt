package com.nsi_projekat.ui.uiutil.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nsi_projekat.R
import com.nsi_projekat.ui.theme.spacing

@Composable
fun RetrySection(
    modifier: Modifier = Modifier,
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = error,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary
        )

        PrimaryButton(
            modifier = Modifier
                .padding(
                    top = MaterialTheme.spacing.large,
                    start = MaterialTheme.spacing.large,
                    end = MaterialTheme.spacing.large
                )
                .align(Alignment.CenterHorizontally)
                .height(57.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.error_retry),
            onClick = { onRetry() }
        )
    }
}