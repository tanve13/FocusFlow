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

    val badges = listOf(
        Badge(
            "1",
            "Consistency Starter",
            "Maintain a 7-day streak",
            R.drawable.img,
            unlocked = streak >= 7,
            progress = (streak / 7f).coerceAtMost(1f)
        ),
        Badge(
            "2",
            "Focus Warrior",
            "Complete a 30-day streak",
            R.drawable.img,
            unlocked = streak >= 30
        ),
        Badge(
            "3",
            "Master of Discipline",
            "Achieve a 50-day streak",
            R.drawable.img,
            unlocked = streak >= 50
        ),
        Badge(
            "4",
            "Daily Grinder",
            "Finish 10 sessions in a single day",
            R.drawable.img,
            unlocked = todaySessions >= 10
        ),
        Badge(
            "5",
            "Century Achiever",
            "Complete 100 focus sessions",
            R.drawable.img,
            unlocked = totalSessions >= 100
        ),
        Badge(
            "6",
            "Time Investor",
            "Reach 500 minutes of focus time",
            R.drawable.img,
            unlocked = totalMinutes >= 500
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Achievements") },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.Filled.Lock, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // HEADER GRADIENT
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(Color.Black, Color(0xFF202020)))
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        "Your Achievements",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Unlock badges by staying consistent!",
                        color = Color.White.copy(0.8f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 50.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(badges) { badge ->
                    BadgeCard(badge = badge, onShare = { onShare(badge) })
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeCard(badge: Badge, onShare: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val confettiVisible = remember { mutableStateOf(false) }

    LaunchedEffect(badge.unlocked) {
        if (badge.unlocked) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
            confettiVisible.value = true
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                // Glow or locked badge background
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        }
                        .clip(CircleShape)
                        .background(
                            brush = if (badge.unlocked) Brush.radialGradient(
                                colors = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
                            ) else Brush.radialGradient(
                                colors = listOf(Color.Gray, Color.Gray)
                            )
                        )
                )

                Text(
                    badge.title,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    style = MaterialTheme.typography.titleMedium
                )

                // Confetti effect placeholder
                if (confettiVisible.value) {
                    Text("🎉", fontSize = 32.sp, modifier = Modifier.align(Alignment.TopEnd))
                    // TODO: Replace with actual confetti animation library
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(badge.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = badge.progress,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Share button
            OutlinedButton(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
                Text("Share")
            }
        }
    }
}
