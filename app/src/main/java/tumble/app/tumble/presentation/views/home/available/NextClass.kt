package tumble.app.tumble.presentation.views.home.available

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.realm.Event
import tumble.app.tumble.presentation.components.buttons.CompactEventButtonLabel
import tumble.app.tumble.extensions.presentation.borderRadius
import tumble.app.tumble.extensions.presentation.formatDate
import tumble.app.tumble.extensions.presentation.toColor

@Composable
fun NextClass(nextClass: Event?, onEventSelection: (Event) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = stringResource(R.string.next_class),
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            nextClass?.let {
                Text(
                    text = it.from.formatDate() ?: "",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (nextClass?.course != null) {
            val color = nextClass.course?.color?.toColor() ?: Color.White
            CompactEventButtonLabel(
                event = nextClass,
                color = color,
                onEventSelection = onEventSelection
            )
        } else {
            Text(
                text = stringResource(R.string.no_upcoming_class),
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            )
        }
    }
}
