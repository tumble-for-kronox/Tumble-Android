package com.tumble.kronoxtoapp.presentation.screens.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.presentation.screens.general.FlowStack

@Composable
fun SearchInfo(
    schools: List<School>,
    selectedSchool: MutableState<School?>
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        val textAlpha by animateFloatAsState(
            targetValue = if (selectedSchool.value == null) 1f else 0f
        )
        Text(
            text = stringResource(id = R.string.choose_university),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = textAlpha),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(32.dp))
        FlowStack<School>(
            items = schools,
            viewGenerator = { SchoolPill(school = it, selectedSchool = selectedSchool) })
    }
}