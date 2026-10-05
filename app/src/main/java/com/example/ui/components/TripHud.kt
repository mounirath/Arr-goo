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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.TripState
import com.example.model.UserLocation
import com.example.ui.theme.Y2KChromeSurface
import com.example.ui.theme.Y2KGelButton
import com.example.ui.theme.Y2KTechnoText
import com.example.ui.theme.Y2KTelemetryTag
import com.example.ui.theme.Y2KTokens

@Composable
fun TripHud(
    tripState: TripState,
    userLocation: UserLocation,
    currentLanguage: AppLanguage,
    onStopTrip: () -> Unit,
    isDarkTerrain: Boolean = false,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = tripState.isActive,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        // Y2K Liquid-Chrome Cyber HUD Capsule
        Y2KChromeSurface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            shape = RoundedCornerShape(22.dp),
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
                        // Pulsing Live Cyber Beacon
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .shadow(6.dp, CircleShape, spotColor = Y2KTokens.TextCyanGlow)
                                .clip(CircleShape)
                                .background(if (tripState.isWithinAlertZone) Color(0xFFF43F5E) else Y2KTokens.TextCyanGlow)
                                .border(BorderStroke(1.dp, Color.White), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (tripState.isWithinAlertZone) {
                                    when (currentLanguage) {
                                        AppLanguage.AR -> "🚨 في منطقة التنبيه!"
                                        AppLanguage.EN -> "// ALERT ZONE BREACHED //"
                                        AppLanguage.FR -> "// ZONE D'ALERTE ATTEINTE //"
                                    }
                                } else {
                                    when (currentLanguage) {
                                        AppLanguage.AR -> "رحلة جارية نحو:"
                                        AppLanguage.EN -> "// TRACKING VECTOR //"
                                        AppLanguage.FR -> "// CAP EN COURS //"
                                    }
                                },
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tripState.isWithinAlertZone) Color(0xFFF43F5E) else Y2KTokens.TextCyanGlow
                            )
                            Y2KTechnoText(
                                text = tripState.destination?.name ?: "",
                                fontSize = 15.sp,
                                letterSpacing = 1.sp,
                                color = Y2KTokens.TextPureWhite
                            )
                        }
                    }

                    // Stop trip Holographic Gel Button
                    Y2KGelButton(
                        onClick = onStopTrip,
                        brush = Y2KTokens.HolographicGelBrush,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("hud_stop_trip_button"),
                        contentDescription = "Arrêter le trajet"
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Arrêter le trajet",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
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
                                AppLanguage.EN -> "DIST_REMAINING"
                                AppLanguage.FR -> "DISTANCE_RESTANTE"
                            },
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = Y2KTokens.TextMutedSteel,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (tripState.currentDistanceMeters < Float.MAX_VALUE) {
                                formatDistance(tripState.currentDistanceMeters)
                            } else "--",
                            fontFamily = FontFamily.SansSerif,
                            fontStyle = FontStyle.Italic,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Y2KTokens.TextCyanGlow
                        )
                    }

                    // Speed Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Y2KTokens.ObsidianVoid)
                            .border(BorderStroke(1.dp, Y2KTokens.ChromeBorderBrush), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = Y2KTokens.TextCyanGlow,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${userLocation.speedKmh.toInt()} KM/H",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Y2KTokens.TextSilver
                            )
                        }
                    }

                    // Alert Radius Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Y2KTokens.ObsidianVoid)
                            .border(BorderStroke(1.dp, Y2KTokens.ChromeBorderBrush), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = Color(0xFFD946EF),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tripState.alertRadiusMeters} M",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Y2KTokens.TextSilver
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
                        .height(5.dp)
                        .clip(CircleShape),
                    color = if (tripState.isWithinAlertZone) Color(0xFFF43F5E) else Y2KTokens.TextCyanGlow,
                    trackColor = Y2KTokens.ObsidianVoid
                )
            }
        }
    }
}
