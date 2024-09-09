package tumble.app.tumble.presentation.views.account.User.ResourceSection

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ResourceType{
    EVENT, RESOURCE
}

@Composable
fun ResourceSectionDivider(
    title: String,
    resourceType: ResourceType? = null,
    destination: (() -> Unit)? = null,
    onBook: (() -> Unit)? = null,
    content: @Composable () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 7.5.dp, horizontal = 17.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground
            )
            if(destination != null){
                Log.e("error", "in if")
                when(resourceType){
                    ResourceType.EVENT -> ResourceNavigationItem(title = "See all", destination)
                    ResourceType.RESOURCE -> ResourceNavigationItem(title = "Book more", destination)
                    null -> {}
                }
            }
        }
        content()
    }
}

@Composable
fun ResourceNavigationItem(
    title: String,
    destination:  () -> Unit
){

    Button(
        onClick = { destination() },
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(20.dp)),
        elevation = null

    ) {

        Row (verticalAlignment = Alignment.CenterVertically){
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(14.dp)
            )
        }
    }

}