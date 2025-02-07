package tumble.app.tumble.presentation.views.account.User.ProfileSection

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
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import tumble.app.tumble.presentation.viewmodels.AccountViewModel
import tumble.app.tumble.presentation.views.account.User.ResourceSection.Resources

@Composable
fun UserOverview(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavHostController
){
    val collapsedHeader = remember {
        mutableStateOf(false)
    }
    val user = viewModel.user.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
    ) {
        user.value?.name?.let { name ->
            user.value?.username?.let { username ->
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
        Divider()
        Resources(
            parentViewModel = viewModel,
            getResourcesAndEvents = { getResourcesAndEvents(viewModel) },
            collapsedHeader = collapsedHeader,
            navController = navController
        )
    }
}

private fun getResourcesAndEvents(viewModel: AccountViewModel){
    viewModel.getUserBookingsForSection()
    viewModel.getUserEventsForSection()
}