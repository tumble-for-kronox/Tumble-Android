package tumble.app.tumble.presentation.views.account.User.ResourceSection.Booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionDivider(
    title: String,
    image: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .background(MaterialTheme.colors.background)
        ) {
            Icon(
                imageVector = image,
                contentDescription = title,
                modifier = Modifier.size(21.dp),
                tint = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 17.sp,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Divider(
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            )
        }
        content()
    }
}