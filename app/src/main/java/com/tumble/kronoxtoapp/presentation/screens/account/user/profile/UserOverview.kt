package com.tumble.kronoxtoapp.presentation.screens.account.user.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tumble.kronoxtoapp.presentation.viewmodels.AccountViewModel
import com.tumble.kronoxtoapp.presentation.screens.account.user.resources.Resources
import kotlinx.coroutines.launch

@Composable
fun UserOverview(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController
){
    val collapsedHeader = remember {
        mutableStateOf(false)
    }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var scrollOffset by remember {
        mutableFloatStateOf(0f)
    }
    val user = viewModel.user.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background),
    ) {
        user.value?.name?.let { name ->
            user.value?.username?.let { username ->
                Surface (
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                )  {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth()
                    ) {
                        UserAvatar(name = name, collapsedHeader = collapsedHeader.value)
                        Spacer(modifier = Modifier.width(5.dp))
                        Column(
                            modifier = Modifier.padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = name,
                                fontSize = if (collapsedHeader.value) 20.sp else 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (!collapsedHeader.value){
                                Text(text = username,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = 5.dp)
                                )
                                Text(text = viewModel.getSchoolName()?.name?: "",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Resources(
            parentViewModel = viewModel,
            getResourcesAndEvents = { getResourcesAndEvents(viewModel) },
            navController = navController
        )
    }

    LaunchedEffect(key1 = scrollState.value) {
        scrollOffset = scrollState.value.toFloat()
        if(scrollOffset >= 80){
            coroutineScope.launch {
                collapsedHeader.value = true
            }
        } else if (scrollOffset == 0f){
            coroutineScope.launch {
                collapsedHeader.value = false
            }
        }
    }
}

private fun getResourcesAndEvents(viewModel: AccountViewModel){
    viewModel.getUserBookingsForSection()
    viewModel.getUserEventsForSection()
}