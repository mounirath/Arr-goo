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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.MapStyle
import com.example.ui.theme.Y2KGelButton
import com.example.ui.theme.Y2KTokens

@Composable
fun MapFloatingControls(
    onCenterLocation: () -> Unit,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit,
    currentMapStyle: MapStyle,
    onMapStyleChange: (MapStyle) -> Unit,
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
        // 1. Center on GPS Location - Electric Blue Gel Button
        Y2KGelButton(
            onClick = onCenterLocation,
            brush = Y2KTokens.ElectricBlueGelBrush,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_center_location"),
            contentDescription = "Center on my location"
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Center on my location",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // 2. Fullscreen / Focus Mode - Liquid Chrome Mirror Button
        Y2KGelButton(
            onClick = onToggleFullscreen,
            brush = Y2KTokens.LiquidChromeBrush,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_toggle_fullscreen"),
            contentDescription = "Toggle full map view"
        ) {
            Icon(
                imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = "Toggle full map view",
                tint = Y2KTokens.CyberChromeDark,
                modifier = Modifier.size(22.dp)
            )
        }

        // 3. Layers / Map Style - Holographic Cyan-Magenta Gel Button
        Box {
            Y2KGelButton(
                onClick = { isLayersMenuOpen = true },
                brush = Y2KTokens.HolographicGelBrush,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("btn_map_layers"),
                contentDescription = "Map Style Layers"
            ) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Map Style Layers",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            DropdownMenu(
                expanded = isLayersMenuOpen,
                onDismissRequest = { isLayersMenuOpen = false },
                modifier = Modifier
                    .background(Y2KTokens.CyberChromeDark.copy(alpha = 0.96f))
                    .border(
                        BorderStroke(1.5.dp, Y2KTokens.ChromeBorderBrush),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                MapStyle.values().forEach { style ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (style == currentMapStyle) {
                                    Text(
                                        text = "▶ ",
                                        color = Y2KTokens.TextCyanGlow,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> style.labelAr
                                        AppLanguage.EN -> style.labelEn
                                        AppLanguage.FR -> style.labelFr
                                    },
                                    fontFamily = FontFamily.SansSerif,
                                    fontStyle = FontStyle.Italic,
                                    color = if (style == currentMapStyle) Y2KTokens.TextCyanGlow else Y2KTokens.TextSilver,
                                    fontWeight = if (style == currentMapStyle) FontWeight.Black else FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        },
                        onClick = {
                            onMapStyleChange(style)
                            isLayersMenuOpen = false
                        }
                    )
                }
            }
        }

        // 4. Zoom In - Liquid Chrome Mirror Button (+)
        Y2KGelButton(
            onClick = onZoomIn,
            brush = Y2KTokens.LiquidChromeBrush,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_zoom_in"),
            contentDescription = "Zoom in"
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Zoom in",
                tint = Y2KTokens.CyberChromeDark,
                modifier = Modifier.size(22.dp)
            )
        }

        // 5. Zoom Out - Liquid Chrome Mirror Button (-)
        Y2KGelButton(
            onClick = onZoomOut,
            brush = Y2KTokens.LiquidChromeBrush,
            modifier = Modifier
                .size(48.dp)
                .testTag("btn_zoom_out"),
            contentDescription = "Zoom out"
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Zoom out",
                tint = Y2KTokens.CyberChromeDark,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
