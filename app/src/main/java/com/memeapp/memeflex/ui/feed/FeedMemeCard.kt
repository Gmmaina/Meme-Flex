package com.memeapp.memeflex.ui.feed

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memeapp.memeflex.data.Meme
import com.memeapp.memeflex.data.sampleMemes
import com.memeapp.memeflex.ui.theme.MemeFlexTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedMemeCard(
    meme: Meme,
    modifier: Modifier = Modifier,
    onLikeClick: (Meme) -> Unit = {},
    onDownloadClick: (Meme) -> Unit = {},
    onShareClick: (Meme) -> Unit = {},
    onMemeClick: (Meme) -> Unit = {},
    onLongPress: (Meme) -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(meme.isLiked) }
    var likeCount by remember { mutableStateOf(meme.likes) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header with user info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                UserProfile(
                    profileImage = meme.profileImage,
                    profileName = meme.profileName
                )

                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }

            // Meme Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { onMemeClick(meme) },
                        onLongClick = { onLongPress(meme) }
                    )
            ) {
                Image(
                    painter = painterResource(meme.image),
                    contentDescription = "Meme: ${meme.caption}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            // Caption and Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Caption
                Text(
                    text = meme.caption,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like Button
                    IconTextButton(
                        icon = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        text = likeCount.toString(),
                        tint = if (isLiked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        onClick = {
                            isLiked = !isLiked
                            likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                            onLikeClick(meme.copy(isLiked = isLiked, likes = likeCount))
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Download Button
                    IconTextButton(
                        icon = Icons.Default.Download,
                        text = meme.downloads.toString(),
                        tint = MaterialTheme.colorScheme.onSurface,
                        onClick = { onDownloadClick(meme) }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Share Button
                    IconButton(
                        onClick = { onShareClick(meme) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserProfile(
    @DrawableRes profileImage: Int,
    profileName: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(profileImage),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = profileName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun IconTextButton(
    icon: ImageVector,
    text: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = tint
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedMemeCardPreview() {
    MemeFlexTheme {
        FeedMemeCard(
            meme = sampleMemes[0],
            modifier = Modifier.padding(16.dp)
        )
    }
}