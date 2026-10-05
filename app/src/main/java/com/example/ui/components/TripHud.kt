package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.TripState
import com.example.model.UserLocation
import com.example.ui.theme.FrutigerAquaDeep
import com.example.ui.theme.FrutigerDeepNavy
import com.example.ui.theme.FrutigerGrassGreen
import com.example.ui.theme.FrutigerMeadowDark
import com.example.ui.theme.FrutigerSkyBlue
import com.example.ui.theme.FrutigerSlate
import com.example.ui.theme.GlassTokens

@Composable
fun TripHud(
    tripState: TripState,
    userLocation: UserLocation,
    currentLanguage: AppLanguage,
    onStopTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = tripState.isActive,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        // Frutiger Aero Translucent Aqua HUD Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = Color(0xFF0284C7).copy(alpha = 0.35f),
                    ambientColor = Color.White
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.White,
                            Color(0xF2E0F2FE),
                            Color(0xE6F0FDF4)
                        )
                    )
                )
                .border(
                    BorderStroke(1.5.dp, GlassTokens.GlassBorderBrush),
                    RoundedCornerShape(24.dp)
                )
        ) {
            // Specular Top Gloss Sheen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(GlassTokens.GlossCapBrush)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Header: Destination name & Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Pulsing Live Indicator
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .shadow(4.dp, CircleShape)
                                .clip(CircleShape)
                                .background(if (tripState.isWithinAlertZone) Color(0xFFF43F5E) else FrutigerGrassGreen)
                                .border(BorderStroke(1.5.dp, Color.White), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (tripState.isWithinAlertZone) {
                                    when (currentLanguage) {
                                        AppLanguage.AR -> "🚨 في منطقة التنبيه!"
                                        AppLanguage.EN -> "🚨 Inside alert zone!"
                                        AppLanguage.FR -> "🚨 Dans la zone d'alerte !"
                                    }
                                } else {
                                    when (currentLanguage) {
                                        AppLanguage.AR -> "رحلة جارية نحو:"
                                        AppLanguage.EN -> "En route to:"
                                        AppLanguage.FR -> "En route vers :"
                                    }
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tripState.isWithinAlertZone) Color(0xFFE11D48) else FrutigerMeadowDark
                            )
                            Text(
                                text = tripState.destination?.name ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = FrutigerDeepNavy,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Stop trip Aero Gel Button
                    IconButton(
                        onClick = onStopTrip,
                        modifier = Modifier
                            .size(36.dp)
                            .shadow(4.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2))
                            .border(BorderStroke(1.dp, Color(0xFFFCA5A5)), CircleShape)
                            .testTag("hud_stop_trip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Arrêter le trajet",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Metrics Row: Remaining Distance, Alert Threshold, Speed, ETA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Big Distance Remaining
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                AppLanguage.AR -> "المسافة المتبقية"
                                AppLanguage.EN -> "Distance left"
                                AppLanguage.FR -> "Distance restante"
                            },
                            fontSize = 11.sp,
                            color = FrutigerSlate,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (tripState.currentDistanceMeters < Float.MAX_VALUE) {
                                formatDistance(tripState.currentDistanceMeters)
                            } else "--",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = FrutigerMeadowDark
                        )
                    }

                    // Speed Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                            .border(BorderStroke(1.dp, Color(0xFFBAE6FD)), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = FrutigerAquaDeep,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userLocation.speedKmh.toInt()} km/h",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = FrutigerDeepNavy
                            )
                        }
                    }

                    // Threshold Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(BorderStroke(1.dp, Color(0xFFFCD34D)), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatDistance(tripState.alertRadiusMeters),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF78350F)
                            )
                        }
                    }

                    // ETA Pill
                    if (tripState.estimatedArrivalSeconds > 0) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.8f))
                                .border(BorderStroke(1.dp, Color(0xFFBAE6FD)), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = FrutigerSkyBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val mins = tripState.estimatedArrivalSeconds / 60
                                Text(
                                    text = if (mins > 0) "$mins min" else "< 1 min",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FrutigerDeepNavy
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Frutiger Aero Gel Progress Bar
                val progress = if (tripState.initialDistanceMeters > 0f) {
                    ((tripState.initialDistanceMeters - tripState.currentDistanceMeters) / tripState.initialDistanceMeters).coerceIn(0f, 1f)
                } else 0f

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = FrutigerGrassGreen,
                    trackColor = Color(0xFFBAE6FD)
                )
            }
        }
    }
}
