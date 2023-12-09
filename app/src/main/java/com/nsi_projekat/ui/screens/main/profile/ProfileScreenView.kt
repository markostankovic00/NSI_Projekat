@file:OptIn(ExperimentalCoroutinesApi::class)

package com.nsi_projekat.ui.screens.main.profile

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsi_projekat.R
import com.nsi_projekat.ui.activities.AccountActivity
import com.nsi_projekat.ui.screens.main.profile.ProfileScreenViewModel.Events
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.AutoResizedText
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.ui.uiutil.composables.PrimaryButton
import com.nsi_projekat.ui.uiutil.composables.PrimaryOutlinedTextField
import com.nsi_projekat.ui.uiutil.composables.contextmenu.ContextDropdownMenu
import com.nsi_projekat.ui.uiutil.composables.contextmenu.ContextMenuItem
import com.nsi_projekat.ui.uiutil.composables.contextmenu.ContextMenuItemFunctionEnum
import com.nsi_projekat.util.ComponentSizes
import com.nsi_projekat.util.findActivity
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
fun ProfileScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<ProfileScreenViewModel>()

    EventsHandler(viewModel)

    ProfileScreenView(viewModel)

    IsLoadingState(viewModel)
}

@Composable
private fun ProfileScreenView(
    viewModel: ProfileScreenViewModel
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    val scrollState = rememberScrollState()

    val userData by viewModel.userData.collectAsState()

    val isUploading by viewModel.isUploading.collectAsState()

    val selectImageLauncher = createSelectImageLauncher(viewModel)

    val isContextMenuVisible by rememberSaveable {
        viewModel.isContextMenuVisible
    }

    val isWithdrawCashDialogVisible by rememberSaveable {
        viewModel.isWithdrawCashDialogVisible
    }

    val isDepositCashDialogVisible by rememberSaveable {
        viewModel.isDepositCashDialogVisible
    }

    var amountOfCash by remember { mutableStateOf("") }

    BoxWithBackgroundPattern {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = MaterialTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isUploading) {

                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(screenWidth.times(0.5f).dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                }

            } else {

                GlideImage(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(screenWidth.times(0.5f).dp)
                        .clickable { selectImageLauncher.launch("image/*") },
                    imageModel = { userData?.photoUrl ?: "" },
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colors.primary
                        )
                    },
                    failure = {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small)
                                .size(screenWidth.times(0.5f).dp)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.scale(2f),
                                imageVector = Icons.Outlined.ErrorOutline,
                                contentDescription = "Icon representing image fetch failure",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                )
            }

            Box {

                ProfileDataRow(
                    label = stringResource(id = R.string.profile_screen_cash_label),
                    data = userData?.cash.toString() + " $"
                )

                ContextDropdownMenu(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    dropDownItems = initializeDropdownMenuItems(),
                    isContextMenuVisible = isContextMenuVisible,
                    onIconClick = viewModel::onContextDropdownMenuIconClick,
                    onDismissRequest = viewModel::onContextDropdownMenuDismissRequest,
                    onItemClick = { item ->
                        viewModel.onContextDropdownMenuItemClick(item)
                    }
                )
            }

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_name_label),
                data = userData?.name ?: ""
            )

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_surname_label),
                data = userData?.surname ?: ""
            )

            ProfileDataRow(
                label = stringResource(id = R.string.profile_screen_email_label),
                data = userData?.email ?: ""
            )

            PrimaryButton(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.extraLarge,
                        bottom = ComponentSizes.bottomNavBarHeight.dp
                    )
                    .height(57.dp)
                    .width(150.dp),
                text = stringResource(id = R.string.profile_screen_log_out_button),
                onClick = viewModel::onLogOut
            )
        }

        if (isWithdrawCashDialogVisible) {

            AlertDialog(
                onDismissRequest = { viewModel.onWithdrawDialogDismissed() },
                title = {
                    Text(stringResource(id = R.string.alert_dialog_withdraw_cash_dialog_title))
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
                            onValueChange = { amountOfCash = it },
                            label = stringResource(id = R.string.alert_dialog_currency_label),
                            keyboardType = KeyboardType.Number
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.onWithdrawDialogDismissed()
                                        amountOfCash = ""
                                    }
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
                                    .clickable {
                                        viewModel.withdrawCash(amountOfCash.toDoubleOrNull())
                                        amountOfCash = ""
                                        viewModel.onWithdrawDialogDismissed()
                                    }
                                    .padding(vertical = MaterialTheme.spacing.medium)
                            )
                        }

                    }
                }
            )
        }

        if (isDepositCashDialogVisible) {

            AlertDialog(
                onDismissRequest = {
                    viewModel.onDepositDialogDismissed()
                    amountOfCash = ""
                },
                title = {
                    Text(stringResource(id = R.string.alert_dialog_deposit_cash_dialog_title))
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
                            onValueChange = { amountOfCash = it },
                            label = stringResource(id = R.string.alert_dialog_currency_label),
                            keyboardType = KeyboardType.Number
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        viewModel.onDepositDialogDismissed()
                                        amountOfCash = ""
                                    }
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
                                    .clickable {
                                        viewModel.depositCash(amountOfCash.toDoubleOrNull())
                                        amountOfCash = ""
                                        viewModel.onDepositDialogDismissed()
                                    }
                                    .padding(vertical = MaterialTheme.spacing.medium)
                            )
                        }

                    }
                }
            )
        }

    }
}

@Composable
private fun ProfileDataRow(
    modifier: Modifier = Modifier,
    label: String,
    data: String
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.medium)
            .height(57.dp)
            .width(screenWidth.times(0.7f).dp)
            .shadow(
                elevation = 15.dp,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AutoResizedText(
            modifier = Modifier
                .padding(
                    start = MaterialTheme.spacing.small,
                    end = MaterialTheme.spacing.medium
                )
                .weight(2f),
            text = label,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onSurface
        )

        AutoResizedText(
            modifier = Modifier
                .padding(end = MaterialTheme.spacing.small)
                .weight(3f),
            text = data,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun IsLoadingState(
    viewModel: ProfileScreenViewModel
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
    viewModel: ProfileScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            Events.NavigateToSplashScreen -> {
                context.startActivity(Intent(context, AccountActivity::class.java))
                context.findActivity()?.finish()
            }
            Events.MakeSuccessfullyChangedProfilePictureToast -> {
                Toast.makeText(context, context.getText(R.string.successful_changed_profile_picture_toast), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            Events.MakeLogOutErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_invalid_logout), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            Events.MakeGenericErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_generic), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }
            else -> {}
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
private fun createSelectImageLauncher(
    viewModel: ProfileScreenViewModel
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri ->
    viewModel.onProfilePhotoClicked(uri)
}

@Composable
private fun initializeDropdownMenuItems(): List<ContextMenuItem> =
    listOf(
        ContextMenuItem(
            text = stringResource(R.string.cash_context_menu_deposit),
            icon = Icons.Default.Add,
            function = ContextMenuItemFunctionEnum.DepositCash
        ),
        ContextMenuItem(
            text = stringResource(R.string.cash_context_menu_withdraw),
            icon = Icons.Default.CreditCard,
            function = ContextMenuItemFunctionEnum.WithdrawCash
        )
    )