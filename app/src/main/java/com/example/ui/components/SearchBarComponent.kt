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
import com.example.ui.theme.FrutigerMeadowDark
import com.example.ui.theme.FrutigerSkyBlue
import com.example.ui.theme.FrutigerSlate
import com.example.ui.theme.GlassTokens

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Top Header: Hamburger Glass Button + ARRIVA Brand Glass Pill
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Hamburger Menu Aero Glass Button
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
                    .clickable(onClick = onOpenMenu)
                    .testTag("btn_menu"),
                contentAlignment = Alignment.Center
            ) {
                // Top Gloss Sheen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(GlassTokens.GlossCapBrush)
                )

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = FrutigerDeepNavy,
                    modifier = Modifier.size(24.dp)
                )
            }

            // ARRIVA Brand Badge with Specular Aero Glass
            Box(
                modifier = Modifier
                    .height(46.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                        ambientColor = Color.White
                    )
                    .clip(RoundedCornerShape(24.dp))
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
                        RoundedCornerShape(24.dp)
                    )
            ) {
                // Top Gloss Sheen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .align(Alignment.TopCenter)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(GlassTokens.GlossCapBrush)
                )

                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GoogleColorsIcon(modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "ARRIVA",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            color = FrutigerDeepNavy
                        )
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "خرائط جوجل • منبه GPS الذكي"
                                AppLanguage.EN -> "Google Maps • Smart GPS Alarm"
                                AppLanguage.FR -> "Google Maps • Alarme GPS Réveil"
                            },
                            fontSize = 9.sp,
                            color = FrutigerMeadowDark,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Aero Navigation Gel Circle
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .shadow(4.dp, CircleShape, spotColor = Color(0xFF0284C7))
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(FrutigerSkyBlue, FrutigerGrassGreen)
                                )
                            )
                            .border(BorderStroke(1.dp, Color.White), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = "Navigation",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Floating Aero Glass Search Capsule
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = RoundedCornerShape(26.dp),
                    spotColor = Color(0xFF0284C7).copy(alpha = 0.3f),
                    ambientColor = Color.White
                )
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White,
                            Color(0xF0E0F2FE),
                            Color(0xE6F0FDF4)
                        )
                    )
                )
                .border(
                    BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                    RoundedCornerShape(26.dp)
                )
        ) {
            // Specular Top Gloss Sheen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                    .background(GlassTokens.GlossCapBrush)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = FrutigerSkyBlue,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "ابحث عن محطة، عنوان، موقف أو مكان..."
                                AppLanguage.EN -> "Search station, address, stop or place..."
                                AppLanguage.FR -> "Rechercher gare, adresse, arrêt ou lieu..."
                            },
                            fontSize = 13.sp,
                            color = FrutigerSlate,
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
                        focusedTextColor = FrutigerDeepNavy,
                        unfocusedTextColor = FrutigerDeepNavy
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("google_search_input")
                )

                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = FrutigerSkyBlue
                    )
                } else if (query.isNotBlank()) {
                    IconButton(
                        onClick = { onQueryChange("") },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = FrutigerSlate,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Sub-filter pill row with Frutiger Aero Glass Surfaces
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google Maps Layer selector glass pill
            Box {
                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color(0xFF0284C7).copy(alpha = 0.2f),
                            ambientColor = Color.White
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.White,
                                    Color(0xE6E0F2FE)
                                )
                            )
                        )
                        .border(
                            BorderStroke(1.dp, GlassTokens.GlassBorderBrush),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { isMapStyleDropdownOpen = true }
                        .testTag("pill_map_style")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🗺️",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> currentMapStyle.labelAr
                                AppLanguage.EN -> currentMapStyle.labelEn
                                AppLanguage.FR -> currentMapStyle.labelFr
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrutigerDeepNavy
                        )
                    }
                }

                DropdownMenu(
                    expanded = isMapStyleDropdownOpen,
                    onDismissRequest = { isMapStyleDropdownOpen = false },
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.96f))
                        .border(BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush), RoundedCornerShape(12.dp))
                ) {
                    MapStyle.values().forEach { style ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> style.labelAr
                                        AppLanguage.EN -> style.labelEn
                                        AppLanguage.FR -> style.labelFr
                                    },
                                    color = if (style == currentMapStyle) FrutigerSkyBlue else FrutigerDeepNavy,
                                    fontWeight = if (style == currentMapStyle) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            },
                            onClick = {
                                onMapStyleChange(style)
                                isMapStyleDropdownOpen = false
                            }
                        )
                    }
                }
            }

            // GPS Signal Aero Status Pill
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color(0xFF10B981).copy(alpha = 0.2f),
                        ambientColor = Color.White
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color(0xE6DCFCE7)
                            )
                        )
                    )
                    .border(
                        BorderStroke(1.dp, GlassTokens.GlassBorderBrush),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (userLocation.isGpsActive) FrutigerGrassGreen else Color(0xFFF59E0B))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (userLocation.isGpsActive) {
                            when (currentLanguage) {
                                AppLanguage.AR -> "إشارة GPS نشطة"
                                AppLanguage.EN -> "GPS Active"
                                AppLanguage.FR -> "GPS Actif"
                            }
                        } else {
                            when (currentLanguage) {
                                AppLanguage.AR -> "جاري تحديد الموقع"
                                AppLanguage.EN -> "Locating..."
                                AppLanguage.FR -> "Recherche GPS"
                            }
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FrutigerDeepNavy
                    )
                }
            }
        }

        // Live Search Results Dropdown with Frutiger Aero Glass Panel
        AnimatedVisibility(
            visible = searchResults.isNotEmpty() && query.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color(0xFF0284C7).copy(alpha = 0.35f),
                        ambientColor = Color.White
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color(0xF5E0F2FE),
                                Color(0xEDDCFCE7)
                            )
                        )
                    )
                    .border(
                        BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                        RoundedCornerShape(20.dp)
                    )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(searchResults) { place ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectPlace(place) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(FrutigerSkyBlue, FrutigerGrassGreen)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = place.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = FrutigerDeepNavy
                                )
                                if (place.address.isNotBlank()) {
                                    Text(
                                        text = place.address,
                                        fontSize = 11.sp,
                                        color = FrutigerSlate,
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

@Composable
fun GoogleColorsIcon(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF4285F4)))
        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFFEA4335)))
        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFFFBBC05)))
        Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color(0xFF34A853)))
    }
}
