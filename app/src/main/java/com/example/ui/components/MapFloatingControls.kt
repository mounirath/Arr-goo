package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.MapStyle
import com.example.ui.theme.FrutigerAquaDeep
import com.example.ui.theme.FrutigerDeepNavy
import com.example.ui.theme.FrutigerGrassGreen
import com.example.ui.theme.FrutigerSkyBlue
import com.example.ui.theme.GlassTokens

@Composable
fun MapFloatingControls(
    onCenterLocation: () -> Unit,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    currentMapStyle: MapStyle,
    onMapStyleChange: (MapStyle) -> Unit,
    isNativeMap: Boolean = false,
    onToggleMapEngine: (Boolean) -> Unit = {},
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    var isLayersMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Center on GPS Location Glass Button
        FloatingGlassMapButton(
            icon = Icons.Default.MyLocation,
            contentDescription = "Center on my location",
            testTag = "btn_center_location",
            tint = FrutigerAquaDeep,
            onClick = onCenterLocation
        )

        // 2. Fullscreen / Focus Mode Glass Button
        FloatingGlassMapButton(
            icon = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
            contentDescription = "Toggle full map view",
            testTag = "btn_toggle_fullscreen",
            tint = FrutigerSkyBlue,
            onClick = onToggleFullscreen
        )

        // 3. Layers / Map Style Glass Button
        Box {
            FloatingGlassMapButton(
                icon = Icons.Default.Layers,
                contentDescription = "Map Style Layers",
                testTag = "btn_map_layers",
                tint = FrutigerGrassGreen,
                onClick = { isLayersMenuOpen = true }
            )

            DropdownMenu(
                expanded = isLayersMenuOpen,
                onDismissRequest = { isLayersMenuOpen = false },
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.96f))
                    .border(BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush), RoundedCornerShape(12.dp))
            ) {
                MapStyle.values().forEach { style ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (style == currentMapStyle) {
                                    Text("✓ ", color = FrutigerAquaDeep, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> style.labelAr
                                        AppLanguage.EN -> style.labelEn
                                        AppLanguage.FR -> style.labelFr
                                    },
                                    color = if (style == currentMapStyle) FrutigerAquaDeep else FrutigerDeepNavy,
                                    fontWeight = if (style == currentMapStyle) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 14.sp
                                )
                            }
                        },
                        onClick = {
                            onMapStyleChange(style)
                            isLayersMenuOpen = false
                        }
                    )
                }

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!isNativeMap) {
                                Text("✓ ", color = FrutigerAquaDeep, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "🌐 Google Maps Web",
                                color = if (!isNativeMap) FrutigerAquaDeep else FrutigerDeepNavy,
                                fontWeight = if (!isNativeMap) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    },
                    onClick = {
                        onToggleMapEngine(false)
                        isLayersMenuOpen = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isNativeMap) {
                                Text("✓ ", color = FrutigerAquaDeep, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "⚡ Google Maps SDK Natif",
                                color = if (isNativeMap) FrutigerAquaDeep else FrutigerDeepNavy,
                                fontWeight = if (isNativeMap) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    },
                    onClick = {
                        onToggleMapEngine(true)
                        isLayersMenuOpen = false
                    }
                )
            }
        }

        // 4. Zoom In Glass Button (+)
        FloatingGlassMapButton(
            icon = Icons.Default.Add,
            contentDescription = "Zoom in",
            testTag = "btn_zoom_in",
            tint = FrutigerDeepNavy,
            onClick = onZoomIn
        )

        // 5. Zoom Out Glass Button (-)
        FloatingGlassMapButton(
            icon = Icons.Default.Remove,
            contentDescription = "Zoom out",
            testTag = "btn_zoom_out",
            tint = FrutigerDeepNavy,
            onClick = onZoomOut
        )
    }
}

@Composable
private fun FloatingGlassMapButton(
    icon: ImageVector,
    contentDescription: String,
    testTag: String,
    tint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF0284C7).copy(alpha = 0.35f),
                ambientColor = Color.White
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xF8FFFFFF),
                        Color(0xE6E0F2FE),
                        Color(0xD9DCFCE7)
                    )
                )
            )
            .border(
                BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        // Specular Curved Gloss Cap
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(GlassTokens.GlossCapBrush)
        )

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}
