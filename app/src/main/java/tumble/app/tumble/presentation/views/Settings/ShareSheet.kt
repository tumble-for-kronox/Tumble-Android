package tumble.app.tumble.presentation.views.Settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import tumble.app.tumble.R
import tumble.app.tumble.presentation.components.buttons.CloseCoverButton
import tumble.app.tumble.presentation.views.general.CustomProgressIndicator

@Composable
fun ShareSheet(context: Context, onDismiss: () -> Unit) {
    var qrCodeImage by remember { mutableStateOf<Bitmap?>(null) }
    val websiteLink = "tumble.hkr.se"
    val appStoreLink = "https://apps.apple.com/se/app/tumble-for-kronox/id1617642864?l=en"

    LaunchedEffect(Unit) {
        val qrImage = generateQRCode(appStoreLink)
        qrCodeImage = qrImage
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            CloseCoverButton {
                onDismiss()
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.share_app),
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .background(Color.Transparent)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CopyButton(websiteLink, context)

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                //.fillMaxWidth()
                //.padding(horizontal = 50.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(MaterialTheme.colors.background, shape = RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.qr_code),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            qrCodeImage?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(250.dp)
                        .background(Color.Transparent)
                )
            } ?: CustomProgressIndicator()
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun CopyButton(link: String, context: Context) {
    Button(
        onClick = {
            val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
            clipboard?.setPrimaryClip(ClipData.newPlainText("Link", link))
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 25.dp, horizontal = 30.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface),
        elevation = null,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Copy link", fontSize = 16.sp)
                Text(text = link, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.Default.FileCopy,
                contentDescription = null
            )
        }
    }
}

suspend fun generateQRCode(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=$url").openConnection() as HttpURLConnection
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}
