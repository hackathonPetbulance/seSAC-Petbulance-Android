package com.example.presentation.component.ui.molecule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.feature.hospitals.HospitalDetail
import com.example.domain.utils.getCurrentDayOfWeekAbbreviated
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.theme.PetbulanceTheme.colorScheme
import com.example.presentation.component.theme.emp
import com.example.presentation.component.ui.Dot
import com.example.presentation.component.ui.atom.BasicChip
import com.example.presentation.component.ui.atom.BasicIcon
import com.example.presentation.component.ui.atom.IconResource
import java.util.Locale

@Composable
fun HospitalInfoCard(
    hospital: HospitalDetail,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = hospital.name,
                        style = MaterialTheme.typography.titleMedium.emp(),
                        color = colorScheme.textPrimary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicIcon(
                            iconResource = IconResource.Vector(Icons.Filled.Star),
                            contentDescription = "Star",
                            size = 16.dp,
                            tint = Color(0xFFFFBF0F)
                        )
                        Text(
                            text = "${hospital.reviewAvg}",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.textSecondary
                        )
                        Text(
                            text = "(${hospital.reviewCount})",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.caption
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hospital.openNow) {
                        Text(
                            text = "진료 중",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF067DFD)
                        )
                    } else {
                        Text(
                            text = "진료 마감",
                            style = MaterialTheme.typography.labelLarge,
                            color = colorScheme.disabled
                        )
                    }

                    Text(
                        text = "·",
                        style = MaterialTheme.typography.titleLarge.emp(),
                        color = colorScheme.disabled
                    )

                    Text(
                        text = hospital.openHours.first { it.day == getCurrentDayOfWeekAbbreviated() }.hours,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.textPrimary
                    )

                    Dot()

                    Text(
                        text = String.format(
                            Locale.US,
                            "%.1f",
                            hospital.distanceKm
                        ) + " km",
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.caption
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color =
                        if (hospital.openNow) Color(0xFF067DFD) else colorScheme.caption
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Filled.Phone),
                        contentDescription = "phone",
                        size = 16.dp,
                        tint = color
                    )
                    Text(
                        text = hospital.phone,
                        style = MaterialTheme.typography.labelLarge.emp(),
                        color = color
                    )
                    BasicIcon(
                        iconResource = IconResource.Vector(Icons.Default.ContentCopy),
                        contentDescription = "copy",
                        size = 16.dp,
                        tint = color
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                hospital.acceptedAnimals.forEach { elem ->
                    BasicChip(elem, backgroundColor = Color(0XFFF6EF89))
                }
            }
        }
    }
}

@Preview(apiLevel = 34)
@Composable
private fun HospitalCardPreview() {
    PetbulanceTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HospitalInfoCard(HospitalDetail.stub)
            HospitalInfoCard(HospitalDetail.stub.copy(openNow = false))
        }
    }
}
