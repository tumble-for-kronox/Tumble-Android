package tumble.app.tumble.presentation.views.bookmarks.Week

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R

@Composable
fun EmptyEvent(){
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
        ){
            Spacer(modifier = Modifier.width(15.dp))
            Box(modifier = Modifier
                .size(7.dp)
                .background(MaterialTheme.colors.onSurface, CircleShape)
                .padding(start = 15.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.no_events_for_this_day),
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}