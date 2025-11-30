package com.example.presentation.component.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.CustomGreenLoader
import com.example.presentation.component.ui.atom.IconResource

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    topBarInfo: TopBarInfo,
    background: Color = colorScheme.bgFrameDefault,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                clip = false
            )
            .background(color = background)
    ) {
        if (topBarInfo.isLoading) {
            Box(modifier = Modifier.size(48.dp)){
                CustomGreenLoader(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            if (topBarInfo.isLeadingIconAvailable) {
                TopBarIcon(
                    iconResource = topBarInfo.leadingIconResource,
                    contentDescription = "Leading icon",
                    onIconClicked = topBarInfo.onLeadingIconClicked
                )
            } else {
                Spacer(modifier = Modifier.padding(24.dp))
            }
        }

        Row(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = topBarInfo.text,
                color = if (topBarInfo.shouldEmphasized) colorScheme.primary else colorScheme.textPrimary,
                style = MaterialTheme.typography.titleMedium.emp(),
                textAlign = if (topBarInfo.textAlignment == TopBarAlignment.CENTER) TextAlign.Center
                else TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .fillMaxWidth()
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (topBarInfo.trailingIcons.isEmpty()) {
                Spacer(modifier = Modifier.padding(24.dp))
            } else {
                topBarInfo.trailingIcons.forEach { (icon, onClick) ->
                    TopBarIcon(
                        iconResource = icon,
                        contentDescription = "trailing icon : $icon",
                        onIconClicked = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBarIcon(
    iconResource: IconResource,
    contentDescription: String,
    onIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicIcon(
            iconResource = iconResource,
            contentDescription = contentDescription,
            size = 24.dp,
            tint = colorScheme.defaultIcon,
            modifier = modifier.clickable { onIconClicked() }
        )
    }
}

enum class TopBarAlignment {
    START, CENTER
}

data class TopBarInfo(
    val text: String,
    val textAlignment: TopBarAlignment = TopBarAlignment.CENTER,
    val shouldEmphasized: Boolean = false,
    val isLeadingIconAvailable: Boolean = false,
    val onLeadingIconClicked: () -> Unit = {},
    val isTrailingIconAvailable: Boolean = false,
    val leadingIconResource: IconResource = IconResource.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
    val trailingIcons: List<Pair<IconResource, () -> Unit>> = emptyList(),
    val isLoading: Boolean = false,
)

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
                        isLoading = true,
                        text = text,
                        textAlignment = TopBarAlignment.CENTER,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.CENTER,
                        isLeadingIconAvailable = false,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = emptyList()
                    ),
                )
                AppTopBar(
                    topBarInfo = TopBarInfo(
                        text = text,
                        textAlignment = TopBarAlignment.START,
                        isLeadingIconAvailable = true,
                        onLeadingIconClicked = { },
                        trailingIcons = listOf(Pair(IconResource.Vector(Icons.Filled.NotificationsNone)) { })
                    ),
                )
            }
        }
    }
}