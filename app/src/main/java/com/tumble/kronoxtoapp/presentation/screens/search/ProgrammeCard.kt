package com.tumble.kronoxtoapp.presentation.screens.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tumble.kronoxtoapp.domain.models.network.NetworkResponse

@Composable
fun ProgrammeCard(
    programme: NetworkResponse.Programme,
    universityImage: Int?,
    onOpenProgramme: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "card_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button
            ) { onOpenProgramme(programme.id, programme.subtitle) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UniversityLogo(
                universityImage = universityImage,
                modifier = Modifier.align(Alignment.Top)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = programme.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = programme.subtitle.trim(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (programme.id.isNotBlank() && !programme.title.contains(programme.id, ignoreCase = true)) {
                    Text(
                        text = programme.id,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View programme",
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun UniversityLogo(
    universityImage: Int?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (universityImage != null) {
            Image(
                painter = painterResource(id = universityImage),
                contentDescription = "University logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
            )
        } else {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "University",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
