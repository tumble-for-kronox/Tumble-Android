package tumble.app.tumble.presentation.views.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.domain.models.network.NetworkResponse

@Composable
fun ProgrammeCard(
    programme: NetworkResponse.Programme,
    universityImage: Int?,
    onOpenProgramme: (String) -> Unit
){
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenProgramme(programme.id) }
            .padding(5.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.surface,
    ) {
        Row (
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = programme.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface
                )
                Row (verticalAlignment = Alignment.CenterVertically) {
                    universityImage?.let { image ->
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(2.5.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = programme.subtitle.trim(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 15.sp,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}