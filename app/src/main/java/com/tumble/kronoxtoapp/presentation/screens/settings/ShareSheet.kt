package com.tumble.kronoxtoapp.presentation.screens.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.tumble.kronoxtoapp.R
import com.tumble.kronoxtoapp.presentation.components.buttons.CloseCoverButton
import com.tumble.kronoxtoapp.presentation.screens.general.CustomProgressIndicator
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ShareSheet(
    context: Context,
    onDismiss: () -> Unit,
    setTopNavState: (AppBarState) -> Unit
) {
    var qrCodeImage by remember { mutableStateOf<Bitmap?>(null) }
    val websiteLink = "app.tumbleforkronox.com"
    val appStoreLink = "https://play.google.com/store/apps/details?id=com.tumble.kronoxtoapp"
    val title = stringResource(R.string.share_app)

    LaunchedEffect(key1 = true) {
        setTopNavState(
            AppBarState(
                title = title,
                actions = {
                    CloseCoverButton {
                        onDismiss()
                    }
                }
            )
        )
        val qrImage = generateQRCode(appStoreLink)
        qrCodeImage = qrImage
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.notification_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.Transparent)
            )

            CopyButton(websiteLink, context)

            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.qr_code),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
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
        }
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
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.copy_tumble_link),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = link, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            Icon(
                imageVector = Icons.Default.FileCopy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

suspend fun generateQRCode(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection =
                URL("https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=$url").openConnection() as HttpURLConnection
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}
