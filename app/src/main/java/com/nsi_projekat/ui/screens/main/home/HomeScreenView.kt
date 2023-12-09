package com.nsi_projekat.ui.screens.main.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.nsi_projekat.R
import com.nsi_projekat.models.CompanyListing
import com.nsi_projekat.ui.navigation.Routes
import com.nsi_projekat.ui.uiutil.composables.BoxWithBackgroundPattern
import com.nsi_projekat.ui.screens.main.home.HomeScreenViewModel.Events
import com.nsi_projekat.ui.theme.spacing
import com.nsi_projekat.ui.uiutil.composables.SearchBar
import com.nsi_projekat.util.ComponentSizes

@Composable
fun HomeScreen(
    navController: NavHostController
) {

    val viewModel = hiltViewModel<HomeScreenViewModel>()

    EventsHandler(navController, viewModel)

    HomeScreenView(viewModel)

    IsLoadingState(viewModel)
}

@Composable
private fun HomeScreenView(
    viewModel: HomeScreenViewModel
) {

    val focusManager = LocalFocusManager.current

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.isRefreshing.value
    )

    val isLoading by remember { viewModel.isLoading }

    val searchQuery by remember { viewModel.searchQuery }

    val companies by remember { viewModel.companies }

    BoxWithBackgroundPattern(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SearchBar(
                modifier = Modifier
                    .padding(
                        vertical = MaterialTheme.spacing.large
                    )
                    .height(70.dp)
                    .width(screenWidth.times(0.8f)),
                searchText = searchQuery,
                onSearchTextChange = { viewModel.onSearchQueryChange(it) },
                placeholderText = stringResource(id = R.string.home_screen_search_bar_placeholder)
            )

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = viewModel::onSwipeToRefresh
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = ComponentSizes.bottomNavBarHeight.dp)
                ) {

                    items(companies) { company ->

                        CompanyListingRow(
                            company = company,
                            onClick = { viewModel.onCompanyListingClick(company) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanyListingRow(
    modifier: Modifier = Modifier,
    company: CompanyListing,
    onClick: () -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.small)
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium)
            .height(75.dp)
            .clip(MaterialTheme.shapes.medium)
            .shadow(
                elevation = 15.dp,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = MaterialTheme.colors.surface
            )
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(screenWidth.times(0.60f)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.extraSmall,
                        start = MaterialTheme.spacing.medium
                    ),
                text = company.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onSurface
            )

            Text(
                modifier = Modifier
                    .padding(
                        bottom = MaterialTheme.spacing.extraSmall,
                        start = MaterialTheme.spacing.medium
                    ),
                text = "(${company.symbol})",
                style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colors.onSurface
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            modifier = Modifier
                .padding(
                    end = MaterialTheme.spacing.medium
                ),
            text = company.exchange,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun IsLoadingState(
    viewModel: HomeScreenViewModel
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
    viewModel: HomeScreenViewModel
) {

    val context = LocalContext.current

    val event = viewModel.events.collectAsState(initial = null)

    LaunchedEffect(key1 = event.value) {

        when (event.value) {

            is Events.NavigateToCompanyInfoScreen -> {
                navController.navigate(Routes.COMPANY_INFO_SCREEN)
            }

            Events.MakeLoadingErrorToast -> {
                Toast.makeText(context, context.getText(R.string.error_invalid_login), Toast.LENGTH_SHORT).show()
                viewModel.clearEventChannel()
            }

            else -> {}
        }
    }
}