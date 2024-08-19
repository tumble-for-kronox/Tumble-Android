package tumble.app.tumble.presentation.views.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.presentation.School
import tumble.app.tumble.extensions.presentation.noRippleClickable

@Composable
fun SchoolPill(
    school: School,
    selectedSchool: MutableState<School?>
){
    val isSelected = selectedSchool.value == school
    val fontSize = if (isSelected) 18.sp else 16.sp
    val icon = school.logo
    val buttonColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        animationSpec = spring()
    )
    Surface (
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(2.dp)
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
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(fontSize.value.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = school.domain.uppercase(),
                fontSize = fontSize,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}