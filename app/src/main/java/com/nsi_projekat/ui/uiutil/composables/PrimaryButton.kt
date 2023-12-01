package com.nsi_projekat.ui.uiutil.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nsi_projekat.ui.theme.NSI_ProjekatTheme
import com.nsi_projekat.ui.theme.PrimaryButtonBackgroundColor

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = PrimaryButtonBackgroundColor,
            contentColor = MaterialTheme.colors.primary
        ),
        enabled = enabled,
        onClick = onClick
    ) {

        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    NSI_ProjekatTheme {

        PrimaryButton(
            modifier = Modifier
                .width(157.dp)
                .height(57.dp),
            text = "Preview Text",
            onClick = {}
        )
    }
}