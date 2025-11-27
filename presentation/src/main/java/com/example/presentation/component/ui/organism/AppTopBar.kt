package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    topBarInfo: TopBarInfo,
    background: Color = PetbulanceTheme.colorScheme.background,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = background)
    ) {
        TopBarLeadingIcon(
            isLeadingIconAvailable = topBarInfo.isLeadingIconAvailable,
            onLeadingIconClicked = topBarInfo.onLeadingIconClicked,
            iconResource = topBarInfo.leadingIconResource,
            modifier = Modifier.weight(1f)
        )

        Box(modifier = Modifier.weight(6f)) {
            Text(
                text = topBarInfo.text,
                color = PetbulanceTheme.colorScheme.commonText,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp)
            )
        }
        TopBarTrailingIcon(
            isTrailingIconAvailable = topBarInfo.isTrailingIconAvailable,
            onTrailingIconClicked = topBarInfo.onTrailingIconClicked,
            iconResource = topBarInfo.trailingIconResource,
            modifier = Modifier.weight(1f)
        )
    }
}

data class TopBarInfo(
    val text: String,
    val isLeadingIconAvailable: Boolean = false,
    val onLeadingIconClicked: () -> Unit = {},
    val leadingIconResource: IconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
    val isTrailingIconAvailable: Boolean = false,
    val onTrailingIconClicked: () -> Unit = {},
    val trailingIconResource: IconResource = IconResource.Vector(Icons.Filled.MoreVert)
)

@Composable
private fun TopBarLeadingIcon(
    isLeadingIconAvailable: Boolean,
    onLeadingIconClicked: () -> Unit,
    iconResource: IconResource,
    modifier: Modifier = Modifier
) {
    if (isLeadingIconAvailable) {
        BasicIcon(
            iconResource = iconResource,
            tint = PetbulanceTheme.colorScheme.iconTint,
            contentDescription = "Leading Icon",
            modifier = modifier
                .clickable { onLeadingIconClicked() }
                .padding(start = 8.dp)
                .size(36.dp)
        )
    } else {
        Spacer(modifier = modifier)
    }
}

@Composable
private fun TopBarTrailingIcon(
    isTrailingIconAvailable: Boolean,
    onTrailingIconClicked: () -> Unit,
    iconResource: IconResource,
    modifier: Modifier = Modifier
) {
    if (isTrailingIconAvailable) {
        BasicIcon(
            iconResource = iconResource,
            tint = PetbulanceTheme.colorScheme.iconTint,
            contentDescription = "Trailing Icon",
            modifier = modifier
                .clickable { onTrailingIconClicked() }
                .padding(start = 8.dp)
                .size(36.dp)
        )
    } else {
        Spacer(modifier = modifier)
    }
}

@Preview(apiLevel = 34)
@Composable
private fun AppTopBarPreview() {
    PetbulanceTheme {
        val textList = listOf(
            "Short example",
            "LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONG Example",
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            textList.forEach { text ->
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        isTrailingIconAvailable = true,
                        onTrailingIconClicked = {}
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        isLeadingIconAvailable = false,
                        onLeadingIconClicked = { },
                        isTrailingIconAvailable = true,
                        onTrailingIconClicked = {}
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        isTrailingIconAvailable = false,
                        onTrailingIconClicked = {}
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        isLeadingIconAvailable = false,
                        onLeadingIconClicked = { },
                        isTrailingIconAvailable = false,
                        onTrailingIconClicked = {}
                    ),
                )
            }
        }
    }
}