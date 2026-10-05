package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.ui.theme.LiquidGlassButton
import com.example.ui.theme.LiquidGlassTokens

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
    val isDarkTerrain = currentMapStyle == MapStyle.SATELLITE || currentMapStyle == MapStyle.DARK

    val defaultIconTint = if (isDarkTerrain) LiquidGlassTokens.DarkTextPrimary else LiquidGlassTokens.LightTextPrimary

    Column(
        modifier = modifier.padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Center on GPS Location Liquid Glass Capsule Button
        LiquidGlassButton(
            onClick = onCenterLocation,
            isDarkTerrain = isDarkTerrain,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_center_location"),
            contentDescription = "Center on my location"
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Center on my location",
                tint = if (isDarkTerrain) Color(0xFF38BDF8) else FrutigerAquaDeep,
                modifier = Modifier.size(22.dp)
            )
        }

        // 2. Fullscreen / Focus Mode Liquid Glass Capsule Button
        LiquidGlassButton(
            onClick = onToggleFullscreen,
            isDarkTerrain = isDarkTerrain,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_toggle_fullscreen"),
            contentDescription = "Toggle full map view"
        ) {
            Icon(
                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = "Toggle full map view",
                tint = defaultIconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        // 3. Layers / Map Style Liquid Glass Capsule Button
        Box {
            LiquidGlassButton(
                onClick = { isLayersMenuOpen = true },
                isDarkTerrain = isDarkTerrain,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("btn_map_layers"),
                contentDescription = "Map Style Layers"
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Map Style Layers",
                    tint = if (isDarkTerrain) Color(0xFF4ADE80) else FrutigerGrassGreen,
                    modifier = Modifier.size(22.dp)
                )
            }

            DropdownMenu(
                expanded = isLayersMenuOpen,
                onDismissRequest = { isLayersMenuOpen = false },
                modifier = Modifier
                    .background(if (isDarkTerrain) Color(0xF20F172A) else Color(0xFAF8FAFC))
                    .border(
                        BorderStroke(
                            1.25.dp,
                            if (isDarkTerrain) LiquidGlassTokens.DarkRefractionBorder else LiquidGlassTokens.LightRefractionBorder
                        ),
                        RoundedCornerShape(16.dp)
                    )
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
                                    color = if (style == currentMapStyle) FrutigerAquaDeep else defaultIconTint,
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
                                color = if (!isNativeMap) FrutigerAquaDeep else defaultIconTint,
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
                                color = if (isNativeMap) FrutigerAquaDeep else defaultIconTint,
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

        // 4. Zoom In Liquid Glass Button (+)
        LiquidGlassButton(
            onClick = onZoomIn,
            isDarkTerrain = isDarkTerrain,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_zoom_in"),
            contentDescription = "Zoom in"
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Zoom in",
                tint = defaultIconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        // 5. Zoom Out Liquid Glass Button (-)
        LiquidGlassButton(
            onClick = onZoomOut,
            isDarkTerrain = isDarkTerrain,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_zoom_out"),
            contentDescription = "Zoom out"
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Zoom out",
                tint = defaultIconTint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
