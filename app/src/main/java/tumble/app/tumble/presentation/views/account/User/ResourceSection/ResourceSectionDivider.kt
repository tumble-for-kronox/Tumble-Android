package tumble.app.tumble.presentation.views.account.User.ResourceSection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R

enum class ResourceType{
    EVENT, RESOURCE
}

@Composable
fun ResourceSectionDivider(
    title: String,
    resourceType: ResourceType? = null,
    destination: (() -> Unit)? = null,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onBackground
            )
            if(destination != null){
                when(resourceType){
                    ResourceType.EVENT -> ResourceNavigationItem(title = stringResource(R.string.see_all), destination)
                    ResourceType.RESOURCE -> ResourceNavigationItem(title = stringResource(R.string.book_more), destination)
                    null -> {}
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .heightIn(min = 60.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ResourceNavigationItem(
    title: String,
    destination:  () -> Unit
){
    Button(
        onClick = { destination() },
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(15.dp),
        elevation = null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 10.dp, end = 4.dp)
                .padding(vertical = 8.dp)
        ){
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onPrimary,
                letterSpacing = 0.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}