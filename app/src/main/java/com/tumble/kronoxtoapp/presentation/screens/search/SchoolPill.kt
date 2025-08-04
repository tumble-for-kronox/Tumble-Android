package com.tumble.kronoxtoapp.presentation.screens.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.domain.models.presentation.School
import com.tumble.kronoxtoapp.extensions.presentation.noRippleClickable

@Composable
fun SchoolPill(
    school: School,
    selectedSchool: MutableState<School?>
){
    val isSelected = selectedSchool.value == school
    val fontSize by animateFloatAsState(
        targetValue = if (isSelected) 18f else 16f,
        animationSpec = tween(durationMillis = 300)
    )
    val icon = school.logo
    val buttonColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300)
    )
    Surface (
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(2.dp)
            .shadow(2.dp, RoundedCornerShape(12.dp), clip = false)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .noRippleClickable {
                if (isSelected) {
                    selectedSchool.value = null
                } else {
                    selectedSchool.value = school
                }
            },
        color = buttonColor,
    ) {
        Row (
            modifier = Modifier
                .padding(8.dp)
                .padding(start = 3.dp)
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(fontSize.dp)
                    .clip(CircleShape)
                    .then(
                        if (isSelected) {
                            Modifier
                                .background(
                                    MaterialTheme.colorScheme.onPrimary,
                                    CircleShape
                                )
                                .padding(2.5.dp)
                        } else {
                            Modifier
                        }
                    ),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = school.domain.uppercase(),
                fontSize = fontSize.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}