package tumble.app.tumble.presentation.views.bookmarks.EventDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailsBuilder(
    title: String,
    image: ImageVector,
    content: @Composable () -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .background(color = MaterialTheme.colors.surface, RoundedCornerShape(15.dp))
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Image(
                    imageVector = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(17.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                )
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(
                modifier = Modifier.padding(top = 7.5.dp)
            ) {
                content()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}