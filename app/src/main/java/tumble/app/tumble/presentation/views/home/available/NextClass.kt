package tumble.app.tumble.presentation.views.home.available

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import tumble.app.tumble.presentation.extensions.borderRadius
import tumble.app.tumble.presentation.extensions.formatDate
import tumble.app.tumble.presentation.extensions.toColor

@Composable
fun NextClass(nextClass: Event?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(100.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            nextClass?.let {
                Text(
                    text = it.from.formatDate() ?: "",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.next_class),
                color = MaterialTheme.colors.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (nextClass?.course != null) {
            val color = nextClass.course?.color?.toColor() ?: Color.White // Assume toColor() extension function exists
            CompactEventButtonLabel( // Assume CompactEventButtonLabel composable exists
                event = nextClass,
                color = color
            ).apply {
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(bottom = 10.dp)
                    .borderRadius(15.dp)
            }
        } else {
            Text(
                text = stringResource(R.string.no_upcoming_class),
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp
            )
        }
    }
}
