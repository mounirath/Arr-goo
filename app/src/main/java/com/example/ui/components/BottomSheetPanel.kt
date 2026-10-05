package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FavoritePlace
import com.example.model.AppLanguage
import com.example.model.LocationPoint
import com.example.ui.theme.FrutigerAqua
import com.example.ui.theme.FrutigerAquaDeep
import com.example.ui.theme.FrutigerDeepNavy
import com.example.ui.theme.FrutigerGrassGreen
import com.example.ui.theme.FrutigerMeadowDark
import com.example.ui.theme.FrutigerSkyBlue
import com.example.ui.theme.FrutigerSlate
import com.example.ui.theme.GlassTokens

@Composable
fun GoogleMapsBottomSheet(
    destination: LocationPoint?,
    alertRadiusMeters: Int,
    onRadiusChange: (Int) -> Unit,
    userDistanceMeters: Float,
    isTripActive: Boolean,
    onStartTrip: () -> Unit,
    onStopTrip: () -> Unit,
    onClearDestination: () -> Unit,
    onFocusDestinationOnMap: () -> Unit,
    onSaveToFavorites: () -> Unit,
    favorites: List<FavoritePlace>,
    onSelectFavorite: (FavoritePlace) -> Unit,
    onOpenFavoritesManager: () -> Unit,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    // Frutiger Aero Sliding Bottom Menu
    // Closed by default (64dp height with glossy aqua tab)
    // 1st touch -> OPEN (Slide Up, 350ms)
    // 2nd touch -> CLOSE (Slide Down, 350ms)
    // No "X" button, only the tab toggles open/close
    var isMenuOpen by remember { mutableStateOf(false) }

    val animatedHeight by animateDpAsState(
        targetValue = if (isMenuOpen) 590.dp else 64.dp,
        animationSpec = tween(
            durationMillis = 350,
            easing = FastOutSlowInEasing
        ),
        label = "slide_menu_height"
    )

    val chevronRotation by animateFloatAsState(
        targetValue = if (isMenuOpen) 180f else 0f,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "chevron_rotation"
    )

    // Frutiger Aero Aqua Glass Surface Container
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .shadow(
                elevation = 28.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                spotColor = Color(0xFF0284C7).copy(alpha = 0.40f),
                ambientColor = Color.White.copy(alpha = 0.85f)
            )
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xF5FFFFFF),
                        Color(0xE6E0F2FE),
                        Color(0xD9F0FDF4)
                    )
                )
            )
            .border(
                BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
    ) {
        // Specular Curved Gloss Cap for the entire sheet top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(GlassTokens.GlossCapBrush)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // =========================================================================
            // LA LANGUETTE AERO / POIGNÉE HORIZONTALE (Interrupteur Unique)
            // =========================================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .clickable { isMenuOpen = !isMenuOpen }
                    .testTag("sliding_menu_tab_handle"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Aqua Gloss Pill Bar
                    Box(
                        modifier = Modifier
                            .width(54.dp)
                            .height(6.dp)
                            .shadow(2.dp, RoundedCornerShape(3.dp), spotColor = Color(0xFF0284C7))
                            .clip(RoundedCornerShape(3.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF38BDF8),
                                        Color.White,
                                        Color(0xFF4ADE80)
                                    )
                                )
                            )
                            .border(BorderStroke(0.5.dp, Color.White), RoundedCornerShape(3.dp))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Luminous status label with high contrast (FrutigerDeepNavy >= 7:1)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = if (isMenuOpen) "Fermer le menu" else "Ouvrir le menu",
                            tint = FrutigerAquaDeep,
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(chevronRotation)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isMenuOpen) {
                                when (currentLanguage) {
                                    AppLanguage.AR -> "انقر على اللسان لغلق القائمة ▾"
                                    AppLanguage.EN -> "Tap tab to close menu ▾"
                                    AppLanguage.FR -> "Toucher la languette pour fermer ▾"
                                }
                            } else {
                                when (currentLanguage) {
                                    AppLanguage.AR -> "انقر على اللسان لفتح القائمة ▴"
                                    AppLanguage.EN -> "Tap tab to open menu ▴"
                                    AppLanguage.FR -> "Toucher la languette pour ouvrir ▴"
                                }
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FrutigerDeepNavy
                        )
                    }
                }
            }

            // =========================================================================
            // CONTENU COMPLET DU MENU COULISSANT
            // =========================================================================
            if (isMenuOpen) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(4.dp))

                    // SECTION 1: Destination Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "انقر على الخريطة 📍"
                                AppLanguage.EN -> "Tap on map 📍"
                                AppLanguage.FR -> "Cliquer sur la carte 📍"
                            },
                            fontSize = 12.sp,
                            color = FrutigerSlate,
                            fontWeight = FontWeight.Medium
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.AR -> "وجهة الوصول (المحطة / المكان)"
                                    AppLanguage.EN -> "Destination (Station / Place)"
                                    AppLanguage.FR -> "Destination (Gare / Arrêt)"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = FrutigerDeepNavy
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(FrutigerSkyBlue, FrutigerGrassGreen)
                                        )
                                    )
                                    .border(BorderStroke(1.dp, Color.White), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "1",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Destination Frutiger Aero Glass Card
                    if (destination != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    elevation = 14.dp,
                                    shape = RoundedCornerShape(22.dp),
                                    spotColor = Color(0xFF0284C7).copy(alpha = 0.25f),
                                    ambientColor = Color.White
                                )
                                .clip(RoundedCornerShape(22.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.White,
                                            Color(0xFFE8F6FF),
                                            Color(0xFFDCFCE7)
                                        )
                                    )
                                )
                                .border(
                                    BorderStroke(1.5.dp, GlassTokens.GlassCardBorderBrush),
                                    RoundedCornerShape(22.dp)
                                )
                                .testTag("destination_card")
                        ) {
                            // Top Gloss Specular Sheen
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                                    .background(GlassTokens.GlossCapBrush)
                            )

                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Reset Destination Action (No "X" icon to avoid ambiguity)
                                    IconButton(
                                        onClick = onClearDestination,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFE2E8F0).copy(alpha = 0.8f))
                                            .border(BorderStroke(1.dp, Color.White), CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteSweep,
                                            contentDescription = "Effacer la destination",
                                            tint = FrutigerSlate,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier.weight(4f)
                                    ) {
                                        Text(
                                            text = destination.name,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = FrutigerDeepNavy,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.End
                                        )
                                        Text(
                                            text = String.format("%.5f , %.5f", destination.longitude, destination.latitude),
                                            fontSize = 11.sp,
                                            color = FrutigerSlate,
                                            textAlign = TextAlign.End
                                        )
                                        if (userDistanceMeters < Float.MAX_VALUE && userDistanceMeters > 0f) {
                                            val distStr = if (userDistanceMeters >= 1000f) {
                                                String.format("km %.1f", userDistanceMeters / 1000f)
                                            } else {
                                                "${userDistanceMeters.toInt()} m"
                                            }
                                            Text(
                                                text = distStr,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = FrutigerMeadowDark,
                                                textAlign = TextAlign.End
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    // Aero Nature Pin Icon
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF0284C7))
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(FrutigerSkyBlue, FrutigerAqua)
                                                )
                                            )
                                            .border(BorderStroke(1.5.dp, Color.White), RoundedCornerShape(16.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Focus on Map & Save Favorite Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF0284C7).copy(alpha = 0.3f))
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(Color.White, Color(0xFFE0F2FE))
                                                )
                                            )
                                            .border(
                                                BorderStroke(1.5.dp, Color.White),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable(onClick = onFocusDestinationOnMap)
                                            .testTag("btn_focus_on_map"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (currentLanguage) {
                                                AppLanguage.AR -> "تركيز على الخريطة"
                                                AppLanguage.EN -> "Focus on map"
                                                AppLanguage.FR -> "Centrer sur la carte"
                                            },
                                            color = FrutigerDeepNavy,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .height(44.dp)
                                            .shadow(6.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFFD97706).copy(alpha = 0.3f))
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A))
                                                )
                                            )
                                            .border(
                                                BorderStroke(1.5.dp, Color.White),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable(onClick = onSaveToFavorites)
                                            .testTag("btn_save_favorite"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (currentLanguage) {
                                                AppLanguage.AR -> "محفوظ في المفضلة ★"
                                                AppLanguage.EN -> "Saved in Favorites ★"
                                                AppLanguage.FR -> "Sauvegarder ★"
                                            },
                                            color = Color(0xFF92400E),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color.White.copy(alpha = 0.85f))
                                .border(BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush), RoundedCornerShape(18.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.AR -> "ابحث عن محطة في الأعلى أو انقر مباشرة على الخريطة"
                                    AppLanguage.EN -> "Search station above or tap directly on the map"
                                    AppLanguage.FR -> "Recherchez une station ou touchez directement la carte"
                                },
                                fontSize = 13.sp,
                                color = FrutigerDeepNavy,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Quick Favorites Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "إدارة القائمة"
                                AppLanguage.EN -> "Manage list"
                                AppLanguage.FR -> "Gérer la liste"
                            },
                            fontSize = 12.sp,
                            color = FrutigerAquaDeep,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable(onClick = onOpenFavoritesManager)
                                .padding(4.dp)
                        )

                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "الأماكن المفضلة المسجلة ★"
                                AppLanguage.EN -> "Saved Favorite Places ★"
                                AppLanguage.FR -> "Lieux favoris enregistrés ★"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(favorites) { fav ->
                            Box(
                                modifier = Modifier
                                    .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFFF59E0B).copy(alpha = 0.2f))
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.White, Color(0xFFFEF3C7))
                                        )
                                    )
                                    .border(
                                        BorderStroke(1.dp, Color(0xFFFCD34D)),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onSelectFavorite(fav) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "★ ${fav.name}",
                                    color = Color(0xFF92400E),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // SECTION 2: Pre-alert Distance
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatDistance(alertRadiusMeters),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = FrutigerAquaDeep
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.AR -> "مسافة التنبيه المسبق"
                                    AppLanguage.EN -> "Pre-alert Distance"
                                    AppLanguage.FR -> "Distance de réveil anticipé"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = FrutigerDeepNavy
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(FrutigerSkyBlue, FrutigerGrassGreen)
                                        )
                                    )
                                    .border(BorderStroke(1.dp, Color.White), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "2",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Distance Frutiger Aero Glass Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 14.dp,
                                shape = RoundedCornerShape(22.dp),
                                spotColor = Color(0xFF0284C7).copy(alpha = 0.25f),
                                ambientColor = Color.White
                            )
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White,
                                        Color(0xFFE8F6FF),
                                        Color(0xFFDCFCE7)
                                    )
                                )
                            )
                            .border(
                                BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                                RoundedCornerShape(22.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            val presets = listOf(200, 500, 1000, 2000)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                presets.forEach { dist ->
                                    val isSelected = alertRadiusMeters == dist
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp)
                                            .shadow(if (isSelected) 6.dp else 2.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF0284C7))
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                if (isSelected) {
                                                    Brush.verticalGradient(
                                                        listOf(Color(0xFF38BDF8), Color(0xFF0284C7))
                                                    )
                                                } else {
                                                    Brush.verticalGradient(
                                                        listOf(Color.White, Color(0xFFF1F5F9))
                                                    )
                                                }
                                            )
                                            .border(
                                                BorderStroke(
                                                    1.5.dp,
                                                    if (isSelected) Color.White else Color(0xFFCBD5E1)
                                                ),
                                                RoundedCornerShape(14.dp)
                                            )
                                            .clickable { onRadiusChange(dist) }
                                            .testTag("preset_$dist"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = formatDistance(dist),
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else FrutigerDeepNavy
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Slider(
                                value = alertRadiusMeters.toFloat(),
                                onValueChange = { onRadiusChange(it.toInt()) },
                                valueRange = 100f..5000f,
                                colors = SliderDefaults.colors(
                                    thumbColor = FrutigerAquaDeep,
                                    activeTrackColor = FrutigerGrassGreen,
                                    inactiveTrackColor = Color(0xFFBAE6FD)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("radius_slider")
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> "5 كم (قطار سريع)"
                                        AppLanguage.EN -> "5 km (Express Train)"
                                        AppLanguage.FR -> "5 km (Train rapide)"
                                    },
                                    fontSize = 11.sp,
                                    color = FrutigerSlate,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> "100 م (حافلة / ترام)"
                                        AppLanguage.EN -> "100 m (Bus / Tram)"
                                        AppLanguage.FR -> "100 m (Bus / Tram)"
                                    },
                                    fontSize = 11.sp,
                                    color = FrutigerSlate,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color(0xFFFEF3C7).copy(alpha = 0.90f))
                                    .border(BorderStroke(1.dp, Color(0xFFFCD34D)), RoundedCornerShape(14.dp))
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        AppLanguage.AR -> "🔔 سيرن المنبه عندما تصبح على مسافة ${formatDistance(alertRadiusMeters)} من وجهتك."
                                        AppLanguage.EN -> "🔔 The alarm will sound when you are ${formatDistance(alertRadiusMeters)} from destination."
                                        AppLanguage.FR -> "🔔 Le réveil sonnera lorsque vous serez à ${formatDistance(alertRadiusMeters)} du lieu."
                                    },
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Iconic Frutiger Aero Gel CTA Button (Aqua-to-Grass Green with Specular Gloss Cap)
                    Button(
                        onClick = {
                            if (isTripActive) onStopTrip() else onStartTrip()
                        },
                        enabled = destination != null || isTripActive,
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color(0xFFE2E8F0)
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(22.dp),
                                spotColor = Color(0xFF10B981).copy(alpha = 0.50f)
                            )
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                if (isTripActive) {
                                    Brush.verticalGradient(
                                        listOf(Color(0xFFF43F5E), Color(0xFFBE123C))
                                    )
                                } else if (destination != null) {
                                    Brush.verticalGradient(
                                        listOf(
                                            Color(0xFF38BDF8),
                                            Color(0xFF0284C7),
                                            Color(0xFF10B981)
                                        )
                                    )
                                } else {
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF94A3B8), Color(0xFF64748B))
                                    )
                                }
                            )
                            .border(BorderStroke(1.5.dp, Color.White), RoundedCornerShape(22.dp))
                            .testTag("btn_main_action")
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            // Specular Gel Gloss Cap on top half
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp)
                                    .align(Alignment.TopCenter)
                                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                                    .background(GlassTokens.GlossCapBrush)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                Text(
                                    text = if (isTripActive) {
                                        when (currentLanguage) {
                                            AppLanguage.AR -> "إيقاف تتبع الرحلة"
                                            AppLanguage.EN -> "Stop Trip & Alarm"
                                            AppLanguage.FR -> "Arrêter le trajet & alarme"
                                        }
                                    } else {
                                        when (currentLanguage) {
                                            AppLanguage.AR -> "بدء تتبع الرحلة والتنبيه"
                                            AppLanguage.EN -> "Start Trip & Alarm"
                                            AppLanguage.FR -> "Démarrer le trajet & réveil"
                                        }
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = if (isTripActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Sponsored Banner (Aero Aqua Badge)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                            .border(BorderStroke(1.dp, GlassTokens.GlassBorderBrush), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFACC15))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "إعلان ممول",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }

                            Text(
                                text = "شريك معتمد • ARRIVA GPS 2026",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = FrutigerDeepNavy
                            )

                            Text(
                                text = "AD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FrutigerAquaDeep
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatDistance(meters: Int): String {
    return if (meters >= 1000) {
        if (meters % 1000 == 0) "${meters / 1000} km" else String.format("%.1f km", meters / 1000f)
    } else {
        "$meters m"
    }
}

fun formatDistance(meters: Float): String {
    if (meters == Float.MAX_VALUE || meters < 0f) return "--"
    return formatDistance(meters.toInt())
}
