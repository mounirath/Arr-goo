package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.LocationPoint
import com.example.model.MapStyle
import com.example.model.UserLocation
import com.example.ui.theme.FrutigerAquaDeep
import com.example.ui.theme.FrutigerDeepNavy
import com.example.ui.theme.FrutigerGrassGreen
import com.example.ui.theme.FrutigerSkyBlue
import com.example.ui.theme.LiquidGlassButton
import com.example.ui.theme.LiquidGlassCapsule
import com.example.ui.theme.LiquidGlassTokens

@Composable
fun GoogleMapsTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<LocationPoint>,
    isSearching: Boolean,
    onSelectPlace: (LocationPoint) -> Unit,
    currentLanguage: AppLanguage,
    currentMapStyle: MapStyle,
    onMapStyleChange: (MapStyle) -> Unit,
    userLocation: UserLocation,
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMapStyleDropdownOpen by remember { mutableStateOf(false) }
    val isDarkTerrain = currentMapStyle == MapStyle.SATELLITE || currentMapStyle == MapStyle.DARK

    val textColor = if (isDarkTerrain) LiquidGlassTokens.DarkTextPrimary else LiquidGlassTokens.LightTextPrimary
    val textSecondaryColor = if (isDarkTerrain) LiquidGlassTokens.DarkTextSecondary else LiquidGlassTokens.LightTextSecondary
    val iconTint = if (isDarkTerrain) LiquidGlassTokens.DarkIconTint else LiquidGlassTokens.LightIconTint

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // 1. Unified Floating Liquid Glass Header Bar (Concentric Capsule)
        LiquidGlassCapsule(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            isDarkTerrain = isDarkTerrain,
            shape = CircleShape,
            elevation = 14.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Menu Button (concentric inner circular capsule)
                LiquidGlassButton(
                    onClick = onOpenMenu,
                    isDarkTerrain = isDarkTerrain,
                    modifier = Modifier.size(38.dp),
                    contentDescription = "Menu",
                    testTag = "btn_menu"
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Brand Pill: ARRIVA in concentric lens alignment
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        FrutigerAquaDeep,
                                        FrutigerSkyBlue
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "ARRIVA",
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        letterSpacing = 1.sp,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Small status dot
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(FrutigerGrassGreen)
                    )
                }

                // GPS Indicator Pill (concentric inner capsule)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isDarkTerrain) Color(0x3010B981) else Color(0x2510B981))
                        .border(
                            BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
                            CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "GPS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTerrain) Color(0xFF86EFAC) else Color(0xFF15803D)
                    )
                }
            }
        }

        // 2. Liquid Glass Floating Search Bar (Capsule Geometry)
        LiquidGlassCapsule(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            isDarkTerrain = isDarkTerrain,
            shape = CircleShape,
            elevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Icon in concentric circular refraction lens
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDarkTerrain) Color(0x20FFFFFF) else Color(0x150284C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "ابحث عن محطة، عنوان، موقف أو وجهة..."
                                AppLanguage.EN -> "Search station, address or destination..."
                                AppLanguage.FR -> "Rechercher gare, arrêt ou adresse..."
                            },
                            color = textSecondaryColor,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_input_field")
                )

                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 6.dp),
                        strokeWidth = 2.dp,
                        color = iconTint
                    )
                } else if (query.isNotBlank()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = textSecondaryColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 3. Search Results Overlay (Floats cleanly over map, opaque items with refractive rim)
        AnimatedVisibility(
            visible = searchResults.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0xFF0284C7).copy(alpha = 0.35f),
                        ambientColor = Color.White
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (isDarkTerrain) Color(0xF20F172A) else Color(0xFAF8FAFC)
                    )
                    .border(
                        BorderStroke(
                            1.25.dp,
                            if (isDarkTerrain) LiquidGlassTokens.DarkRefractionBorder else LiquidGlassTokens.LightRefractionBorder
                        ),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    items(searchResults) { place ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectPlace(place) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkTerrain) Color(0x3038BDF8) else Color(0x1F0284C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = iconTint,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (place.address.isNotBlank()) {
                                    Text(
                                        text = place.address,
                                        fontSize = 12.sp,
                                        color = textSecondaryColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
