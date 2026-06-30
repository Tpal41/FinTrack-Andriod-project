package com.fintrack.app.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.fintrack.app.core.designsystem.theme.FinTrackBlack
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackNavy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashRoute(
    onSplashFinished: () -> Unit
) {
    val logoScale = remember { Animatable(0.78f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 720, easing = FastOutSlowInEasing)
            )
        }
        launch {
            contentAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 520, delayMillis = 180)
            )
        }
        delay(1250)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.alpha(contentAlpha.value)
        ) {
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .background(FinTrackEmerald.copy(alpha = 0.16f), CircleShape)
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountBalanceWallet,
                    contentDescription = null,
                    tint = FinTrackEmerald,
                    modifier = Modifier.scale(1.45f)
                )
            }
            Text(
                text = "FinTrack",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Smart Personal Finance Manager",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
