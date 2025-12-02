package com.example.presentation.component.ui.atom

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.presentation.R

/**
 * Displays an image with a circular progress indicator while it is loading.
 * Use AsyncImage from Coil.
 *
 * @param modifier The modifier to apply to this composable.
 * @param size The size of the image box. The default value is 128.dp.
 * @param galleryUri The [Uri] of the image to display. If null, a gray box will be displayed instead.
 */
@Composable
fun BasicImageBox(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    galleryUri: Uri?,
    errorImageResource: Int = R.drawable.broken_image,
    placeholderImageResource: Int = R.drawable.broken_image, //TODO : Placeholder 교체
    isClearable: Boolean = false,
    onDeletionIconClicked: () -> Unit = {}
) {
    var imageState by remember { mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty) }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(galleryUri ?: placeholderImageResource)
                .crossfade(true)
//                .error(errorImageResource)
//                .placeholder(placeholderImageResource)
                .build(),
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp)),
            contentDescription = "이미지 콘텐츠",
            contentScale = ContentScale.Crop,
            onState = { state ->
                imageState = state
            }
        )

        if (imageState is AsyncImagePainter.State.Loading) {
            CustomGreenLoader()
        }

        if (isClearable && imageState is AsyncImagePainter.State.Success) {
            IconButton(
                onClick = onDeletionIconClicked,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Composable
fun BasicImageBox(
    modifier: Modifier = Modifier,
    size: Dp = 128.dp,
    imageResource: Int,
    errorImageResource: Int = R.drawable.broken_image,
    placeholderImageResource: Int = R.drawable.broken_image,
    isClearable: Boolean = false,
    onDeletionIconClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageResource)
                .crossfade(true)
                .error(errorImageResource)
                .placeholder(placeholderImageResource)
                .build(),
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = "이미지 콘텐츠",
            contentScale = ContentScale.Crop,
        )

        if (isClearable) {
            IconButton(
                onClick = onDeletionIconClicked,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(2.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = Color.White,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
    }
}
