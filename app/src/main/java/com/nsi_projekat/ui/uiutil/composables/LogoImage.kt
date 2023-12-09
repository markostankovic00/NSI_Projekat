package com.nsi_projekat.ui.uiutil.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nsi_projekat.R
import com.nsi_projekat.ui.theme.BackgroundPatternColor
import com.nsi_projekat.ui.theme.NSI_ProjekatTheme

@Composable
fun LogoImage(
    modifier: Modifier = Modifier,
    width: Dp = 230.dp,
    height: Dp = 200.dp
) {

    Image(
        modifier = modifier
            .width(width)
            .height(height)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    listOf(
                        BackgroundPatternColor,
                        Color.Transparent,
                        BackgroundPatternColor
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp)),
        contentScale = ContentScale.FillBounds,
        painter = painterResource(id = R.drawable.black_bull),
        contentDescription = "Splash screen logo"
    )
}

@Preview
@Composable
private fun LogoImagePreview() {
    NSI_ProjekatTheme {

        LogoImage()
    }
}