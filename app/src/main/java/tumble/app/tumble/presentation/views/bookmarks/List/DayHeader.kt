package tumble.app.tumble.presentation.views.bookmarks.List

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.realm.Day

@Composable
fun DayHeader(day: Day){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = day.name?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = day.date?: "",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.onBackground, shape = RoundedCornerShape(20.dp))
                .height(1.dp)
                .weight(1f)
        )
    }
}