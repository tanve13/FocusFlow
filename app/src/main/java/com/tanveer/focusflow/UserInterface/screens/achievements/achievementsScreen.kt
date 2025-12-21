package com.tanveer.focusflow.UserInterface.screens.achievements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanveer.focusflow.R
import com.tanveer.focusflow.data.model.Badge


// ----------------------------- ACHIEVEMENT SCREEN ---------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementScreen(
    badges: List<Badge>,
    streak: Int,
    todaySessions: Int,
    totalSessions: Int,
    totalMinutes: Int,
    onShare: (Badge) -> Unit = {},
    navBack: () -> Unit
) {

    val finalBadges = listOf(
        Badge("1", "Consistency Starter", "Maintain a 7-day streak", R.drawable.img, streak >= 7, (streak / 7f).coerceAtMost(1f)),
        Badge("2", "Focus Warrior", "Complete a 30-day streak", R.drawable.img, streak >= 30),
        Badge("3", "Master of Discipline", "Achieve a 50-day streak", R.drawable.img, streak >= 50),
        Badge("4", "Daily Grinder", "Finish 10 sessions in a single day", R.drawable.img, todaySessions >= 10),
        Badge("5", "Century Achiever", "Complete 100 focus sessions", R.drawable.img, totalSessions >= 100),
        Badge("6", "Time Investor", "Reach 500 minutes of focus time", R.drawable.img, totalMinutes >= 500)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Achievements", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.Default.Lock, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔥 HEADER
            item {
                Column {
                    Text(
                        "Your Achievements",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Unlock badges by staying consistent!",
                        color = Color.White.copy(0.7f)
                    )
                }
            }

            // 🏆 BADGES
            items(finalBadges) { badge ->
                BadgeCard(badge = badge) {
                    onShare(badge)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}


@Composable
fun BadgeCard(badge: Badge, onShare: () -> Unit) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(badge.unlocked) {
        if (badge.unlocked) {
            scale.animateTo(1f, tween(500))
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                badge.title,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                badge.description,
                color = Color.White.copy(0.7f),
                fontSize = 13.sp
            )

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = badge.progress,
                color = Color.White,
                trackColor = Color.White.copy(0.2f)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onShare,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text("Share")
            }
        }
    }
}
