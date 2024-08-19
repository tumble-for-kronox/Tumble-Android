package tumble.app.tumble.presentation.views.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.presentation.views.general.FlowStack

@Composable
fun SearchInfo(
    schools: List<School>,
    selectedSchool: MutableState<School?>
){
    Column (modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,){

        val textAlpha by animateFloatAsState(
            targetValue = if(selectedSchool.value == null) 1f else 0f
        )
        Text(
            text = stringResource(id = R.string.choose_university),
            fontSize = 20.sp,
            color = MaterialTheme.colors.onBackground.copy(alpha = textAlpha)
        )
        Spacer(modifier = Modifier.height(16.dp))
        FlowStack<School>(items = schools, viewGenerator = {SchoolPill(school = it, selectedSchool = selectedSchool)})
        selectedSchool.value?.let { selectedSchool ->
            if (schools.any{it.loginRq && it == selectedSchool}){
                Row {
                    Image(imageVector = Icons.Default.Clear , contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Important: ")
                    Text(text = "This university requires you to log in to their institution before you can see some of their schedules")
                }
            }
        }
    }
}