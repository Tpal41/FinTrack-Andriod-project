package com.fintrack.app.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintrack.app.core.designsystem.theme.FinTrackEmerald
import com.fintrack.app.core.designsystem.theme.FinTrackGold

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black.copy(alpha = 0.5f)),
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun PremiumHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                brush = Brush.linearGradient(
                    colors = listOf(FinTrackEmerald, FinTrackGold)
                )
            )
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
