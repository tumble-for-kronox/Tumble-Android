package tumble.app.tumble.presentation.views.account.User.ProfileSection

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserAvatar(name: String, collapsedHeader: Boolean){
    val abbreviation = name.abbreviate()
    Text(
        text = abbreviation,
        fontSize = if (collapsedHeader) 20.sp else 40.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colors.onPrimary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .animateContentSize ()
            .padding(16.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)
    )
}

fun String.abbreviate(): String {
    return this.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
}