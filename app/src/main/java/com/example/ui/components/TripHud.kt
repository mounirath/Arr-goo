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
import com.example.ui.theme.LiquidGlassCapsule
import com.example.ui.theme.LiquidGlassTokens

@Composable
fun TripHud(
    tripState: TripState,
    userLocation: UserLocation,
    currentLanguage: AppLanguage,
    onStopTrip: () -> Unit,
    isDarkTerrain: Boolean = false,
    modifier: Modifier = Modifier
) {
    val textColor = if (isDarkTerrain) LiquidGlassTokens.DarkTextPrimary else LiquidGlassTokens.LightTextPrimary
    val textSecondaryColor = if (isDarkTerrain) LiquidGlassTokens.DarkTextSecondary else LiquidGlassTokens.LightTextSecondary

    AnimatedVisibility(
        visible = tripState.isActive,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        // Floating Liquid Glass HUD Capsule
        LiquidGlassCapsule(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            isDarkTerrain = isDarkTerrain,
            shape = RoundedCornerShape(26.dp),
            elevation = 16.dp
        ) {
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
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Stop trip Capsule Button
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
                            color = textSecondaryColor,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (tripState.currentDistanceMeters < Float.MAX_VALUE) {
                                formatDistance(tripState.currentDistanceMeters)
                            } else "--",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isDarkTerrain) Color(0xFF38BDF8) else FrutigerMeadowDark
                        )
                    }

                    // Speed Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isDarkTerrain) Color(0x301E293B) else Color.White.copy(alpha = 0.8f))
                            .border(
                                BorderStroke(1.dp, if (isDarkTerrain) Color(0x5038BDF8) else Color(0xFFBAE6FD)),
                                CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
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
                                color = textColor
                            )
                        }
                    }

                    // Alert Radius Pill
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isDarkTerrain) Color(0x301E293B) else Color.White.copy(alpha = 0.8f))
                            .border(
                                BorderStroke(1.dp, if (isDarkTerrain) Color(0x5038BDF8) else Color(0xFFBAE6FD)),
                                CircleShape
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = FrutigerSkyBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tripState.alertRadiusMeters} m",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Progress Bar toward Alert Zone
                val progress = if (tripState.initialDistanceMeters > 0f) {
                    val covered = tripState.initialDistanceMeters - tripState.currentDistanceMeters
                    (covered / tripState.initialDistanceMeters).coerceIn(0f, 1f)
                } else 0f

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = if (tripState.isWithinAlertZone) Color(0xFFF43F5E) else FrutigerAquaDeep,
                    trackColor = if (isDarkTerrain) Color(0x40334155) else Color(0x300284C7)
                )
            }
        }
    }
}
