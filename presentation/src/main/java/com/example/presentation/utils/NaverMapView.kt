package com.example.presentation.utils

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import com.example.domain.model.common.MapBounds
import com.example.domain.model.feature.hospitals.HospitalMarker
import com.example.presentation.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

@Composable
fun NaverMapView(
    currentLocation: Location,
    cameraPosition: Location,
    places: List<HospitalMarker>,
    selectedHospitalId: Long,
    onMapReady: (NaverMap) -> Unit,
    onMapBoundsChange: (MapBounds) -> Unit,
    onMarkerClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = modifier.background(Color.LightGray).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Map Preview")
        }
        return
    }

    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    val markers = remember { mutableStateListOf<Marker>() }

    LaunchedEffect(naverMap, cameraPosition) {
        naverMap?.let { map ->
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                LatLng(cameraPosition.latitude, cameraPosition.longitude),
                15.5
            ).animate(CameraAnimation.Easing)
            map.moveCamera(cameraUpdate)
        }
    }

    LaunchedEffect(naverMap, currentLocation) {
        naverMap?.let { map ->
            val locationOverlay = map.locationOverlay
            locationOverlay.position = LatLng(currentLocation.latitude, currentLocation.longitude)
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) {
        it.getMapAsync { map ->
            map.mapType = NaverMap.MapType.Basic
            map.uiSettings.isZoomControlEnabled = true
            map.locationOverlay.isVisible = true
            /* TODO : 색상 변경 */

            map.addOnCameraIdleListener {
                val bounds = map.contentBounds
                val mapBounds = MapBounds(
                    minLat = bounds.southWest.latitude,
                    minLng = bounds.southWest.longitude,
                    maxLat = bounds.northEast.latitude,
                    maxLng = bounds.northEast.longitude
                )
                onMapBoundsChange(mapBounds)
            }

            if (naverMap == null) {
                naverMap = map
                onMapReady(map)
            }
        }
    }

    naverMap?.let { map ->
        LaunchedEffect(places, selectedHospitalId) {

            markers.forEach { it.map = null }
            markers.clear()

            places.forEach { place ->
                val isSelected = place.hospitalId == selectedHospitalId

                val iconRes = if (isSelected) {
                    if (place.isOpened) R.drawable.marker_selected
                    else R.drawable.marker_selected_closed
                } else {
                    if (place.isOpened) R.drawable.marker_open
                    else R.drawable.marker_closed
                }

                val newMarker = Marker().apply {
                    position = LatLng(place.latitude, place.longitude)
                    icon = OverlayImage.fromResource(iconRes)
                    this.map = map

                    onClickListener = Overlay.OnClickListener {
                        onMarkerClicked(place.hospitalId)
                        true
                    }
                }
                markers.add(newMarker)
            }
        }
    }
}