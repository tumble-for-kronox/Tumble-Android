package com.tumble.kronoxtoapp.presentation.screens.home.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse
import com.tumble.kronoxtoapp.presentation.components.buttons.CloseCoverButton
import com.tumble.kronoxtoapp.presentation.screens.navigation.AppBarState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewsDetails(
    newsItem: NetworkResponse.NotificationContent,
    onBackToList: () -> Unit,
    onClose: () -> Unit,
    setTopNavState: (AppBarState) -> Unit,
) {
    val title = "Details"
    val scrollState = rememberScrollState()

    LaunchedEffect(true) {
        setTopNavState(
            AppBarState(
                title = title,
                navigationAction = {
                    IconButton(onClick = onBackToList) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    CloseCoverButton { onClose() }
                }
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            if (newsItem.topic.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = newsItem.topic.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            val formattedDate = remember(newsItem.timestamp) {
                try {
                    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    input.timeZone = TimeZone.getTimeZone("UTC")
                    val output = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
                    val parsed = input.parse(newsItem.timestamp)
                    parsed?.let { output.format(it) } ?: ""
                } catch (e: Exception) {
                    ""
                }
            }

            if (formattedDate.isNotEmpty()) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 26.sp,
                    lineHeight = 32.sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(12.dp), clip = false),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    newsItem.longBody?.let {
                        var text = ""
                        text = it.ifEmpty {
                            newsItem.body
                        }
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                lineHeight = 26.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
