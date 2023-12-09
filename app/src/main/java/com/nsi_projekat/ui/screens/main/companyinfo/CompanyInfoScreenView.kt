package com.nsi_projekat.ui.screens.main.companyinfo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsi_projekat.R
import com.nsi_projekat.models.CompanyInfo
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.ui.uiutil.composables.PrimaryButton
import com.nsi_projekat.ui.uiutil.composables.RetrySection
import com.nsi_projekat.ui.uiutil.composables.StockChart
import com.nsi_projekat.ui.screens.main.companyinfo.CompanyInfoScreenViewModel.Events
import com.nsi_projekat.ui.uiutil.composables.alertdialogs.CashDialog
import com.nsi_projekat.ui.uiutil.composables.alertdialogs.NotEnoughResourcesDialog

@Composable
fun CompanyInfoScreen(
    navController: NavHostController,
    companySymbol: String
) {

    val viewModel = hiltViewModel<CompanyInfoScreenViewModel>()

    EventsHandler(navController, viewModel)

    CompanyInfoScreenView(viewModel)

    IsLoadingState(viewModel)

    IsErrorState(viewModel, companySymbol)
}

@Composable
private fun CompanyInfoScreenView(
    viewModel: CompanyInfoScreenViewModel
) {

    val companyInfo by remember { viewModel.company }

    val stockInfo by remember { viewModel.stockInfoList }

    val isInvestDialogVisible by remember { viewModel.isInvestDialogVisible }

    val isNoCashDialogVisible by remember { viewModel.isNoCashDialogVisible }

    var amountOfCash by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        companyInfo?.let {  company ->

            Column(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.large,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                    )
                    .weight(1f)
            ) {

                CompanyInfoSection(company)

                if (stockInfo.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    StockChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .align(Alignment.CenterHorizontally),
                        infoList = stockInfo
                    )
                }
            }

            PrimaryButton(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .fillMaxWidth()
                    .height(57.dp),
                text = stringResource(id = R.string.company_info_screen_invest_button),
                onClick = viewModel::onInvestButtonClick
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
        }
    }

    if (isNoCashDialogVisible) {

        NotEnoughResourcesDialog(
            title = stringResource(id = R.string.alert_dialog_no_cash_dialog_title),
            text = stringResource(id = R.string.alert_dialog_no_cash_dialog_text),
            onDismissRequest = viewModel::dismissNoCashDialog,
            onConfirmClick = viewModel::dismissNoCashDialog
        )
    }

    if (isInvestDialogVisible) {

        CashDialog(
            title = stringResource(id = R.string.alert_dialog_invest_dialog_title),
            amountOfCash = amountOfCash,
            onDismissRequest = {
                viewModel.dismissInvestDialog()
                amountOfCash = ""
            },
            onValueChange = {
                amountOfCash = it
            },
            onCancelClicked = {
                viewModel.dismissInvestDialog()
                amountOfCash = ""
            },
            onConfirmClicked = {
                viewModel.onConfirmInvestDialogClick(amountOfCash.toDoubleOrNull())
                amountOfCash = ""
                viewModel.dismissInvestDialog()
            }
        )
    }
}

@Composable
private fun CompanyInfoSection(company: CompanyInfo) {

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = company.name,
        style = MaterialTheme.typography.h4,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = company.symbol,
        style = MaterialTheme.typography.overline.copy(fontStyle = FontStyle.Italic)
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Divider(modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.company_info_screen_label_industry) + company.industry,
        style = MaterialTheme.typography.overline,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(id = R.string.company_info_screen_label_country) + company.country,
        style = MaterialTheme.typography.overline,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Divider(modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = company.description,
        style = MaterialTheme.typography.caption
    )
}

@Composable
private fun IsErrorState(
    viewModel: CompanyInfoScreenViewModel,
    symbol: String
) {

    val loadError by remember { viewModel.isError }

    loadError?.let {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {

            RetrySection(
                modifier = Modifier.align(Alignment.Center),
                error = it,
                onRetry = { viewModel.loadInfo(symbol) }
            )
        }
    }
}

@Composable
private fun IsLoadingState(
    viewModel: CompanyInfoScreenViewModel
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
    navController: NavHostController,
    viewModel: CompanyInfoScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            Events.NavigateBack -> {
                navController.popBackStack()
            }

            Events.MakeSuccessfulInvestmentToast -> {
                Toast.makeText(
                    context,
                    context.getText(R.string.company_info_screen_successful_investment_toast),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.clearEventChannel()
            }

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