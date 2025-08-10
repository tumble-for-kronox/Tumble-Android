package com.tumble.kronoxtoapp.presentation.screens.account.user.resources.booking.resources

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.tumble.kronoxtoapp.presentation.navigation.UriBuilder
import com.tumble.kronoxtoapp.presentation.components.buttons.BackButton
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.general.Info
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceSelectionState
import com.tumble.kronoxtoapp.presentation.viewmodels.ResourceSelectionViewModel
import com.tumble.kronoxtoapp.utils.isoDateFormatter
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ResourceBookings(
    viewModel: ResourceSelectionViewModel = hiltViewModel(),
    navController: NavController,
    setTopNavState: (AppBarState) -> Unit
) {
    val pageTitle = stringResource(R.string.resources)
    val backTitle = stringResource(R.string.account)
    var selectedPickerDate by remember { mutableStateOf<Date>(Date()) }

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = pageTitle,
                navigationAction = {
                    BackButton(backTitle) {
                        navController.popBackStack()
                    }
                }
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ResourceDatePicker(
            onDateChange = { date ->
                selectedPickerDate = date
                viewModel.getAllResources(date)
            }
        )
        HorizontalDivider()

        when (viewModel.resourceSelectionState) {
            is ResourceSelectionState.Loading -> {
                CustomProgressIndicator()
            }
            is ResourceSelectionState.Error -> {
                val errorMessage = (viewModel.resourceSelectionState as ResourceSelectionState.Error).message
                Column (
                    modifier = Modifier.padding(10.dp)
                ) {
                    Info("No resources available for this date. You may be attempting to access them on a weekend.")
                }
            }
            is ResourceSelectionState.Loaded -> {
                val allResources = (viewModel.resourceSelectionState as ResourceSelectionState.Loaded).allResources
                ResourceLocationsList(
                    allResources = allResources,
                    selectedPickerDate = selectedPickerDate,
                    navigateToResourceSelection = { resource, date ->
                        navController.navigate(
                            UriBuilder.buildAccountResourceDetailsUri(
                                resourceId = resource.id.toString(),
                                isoDateString = isoDateFormatter.format(date)
                            ).toUri())
                    }
                )
            }
        }
    }

    // Initial call to get today's resources
    LaunchedEffect(Unit) {
        viewModel.getAllResources(selectedPickerDate)
    }
}

fun isWeekend(date: Date): Boolean{
    val calendar = Calendar.getInstance().apply { time = date }
    val day = calendar.get(Calendar.DAY_OF_WEEK)
    return day == 1 || day  == 7
}

