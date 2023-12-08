package com.nsi_projekat.ui.screens.main.investments

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsi_projekat.R
import com.nsi_projekat.models.Investment
import com.nsi_projekat.ui.screens.main.investments.InvestmentsScreenViewModel.Events
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.AutoResizedText
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.util.ComponentSizes

@Composable
fun InvestmentsScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<InvestmentsScreenViewModel>()

    EventsHandler(viewModel)

    InvestmentsScreenView(viewModel)

    IsLoadingState(viewModel)
}

@Composable
private fun InvestmentsScreenView(
    viewModel: InvestmentsScreenViewModel
) {

    val listOfInvestments by viewModel.listOfInvestments.collectAsState()

    val isLoading by remember { viewModel.isLoading }

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.medium
                    ),
                text = stringResource(id = R.string.investments_screen_title),
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )

            if (listOfInvestments.isEmpty() && !isLoading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = stringResource(id = R.string.investments_screen_make_investment),
                        style = MaterialTheme.typography.h4
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = MaterialTheme.spacing.large,
                            bottom = ComponentSizes.bottomNavBarHeight.dp + MaterialTheme.spacing.medium,
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium
                        )
                ) {

                    items(
                        listOfInvestments.sortedByDescending { it.cash }
                    ) { investment ->

                        InvestmentDataRow(investment = investment)
                    }
                }
            }
        }
    }
}

@Composable
private fun InvestmentDataRow(
    modifier: Modifier = Modifier,
    investment: Investment
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val rowWidth = screenWidth - MaterialTheme.spacing.medium * 2

    Row(
        modifier = modifier
            .padding(top = MaterialTheme.spacing.medium)
            .width(rowWidth)
            .height(57.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier.width(rowWidth.times(0.7f))
        ) {

            AutoResizedText(
                modifier = Modifier
                    .padding(end = MaterialTheme.spacing.small),
                text = "${investment.companyName} (${investment.companySymbol})",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        }

        Text(
            text = investment.cash.toString(),
            style = MaterialTheme.typography.overline,
            color = MaterialTheme.colors.onSurface
        )

    }
}

@Composable
private fun IsLoadingState(
    viewModel: InvestmentsScreenViewModel
) {

    val isLoading by remember { viewModel.isLoading }

    if (isLoading)
        BoxWithBackgroundPattern(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.onBackground
            )
        }
}

@Composable
private fun EventsHandler(
    viewModel: InvestmentsScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            Events.MakeGenericErrorToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.error_generic),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

            else -> {}
        }
    }
}