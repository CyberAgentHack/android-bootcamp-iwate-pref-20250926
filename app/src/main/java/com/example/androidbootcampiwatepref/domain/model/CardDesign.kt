package com.example.androidbootcampiwatepref.domain.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 名刺デザインの種類
 */
enum class CardDesign(
    val displayName: String,
    val frontBrush: Brush,
    val backBrush: Brush,
    val frontTextColor: Color,
    val backTextColor: Color
) {
    CLASSIC(
        displayName = "クラシック",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF5F5F5),
                Color(0xFFFFFFFF)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE8E8E8),
                Color(0xFFF5F5F5)
            )
        ),
        frontTextColor = Color(0xFF212121),
        backTextColor = Color(0xFF212121)
    ),
    OCEAN(
        displayName = "オーシャン",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4FC3F7),
                Color(0xFF29B6F6),
                Color(0xFF03A9F4)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0288D1),
                Color(0xFF0277BD)
            )
        ),
        frontTextColor = Color(0xFF01579B),
        backTextColor = Color(0xFFFFFFFF)
    ),
    SUNSET(
        displayName = "サンセット",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFB74D),
                Color(0xFFFF8A65)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFF7043),
                Color(0xFFE64A19)
            )
        ),
        frontTextColor = Color(0xFFBF360C),
        backTextColor = Color(0xFFFFFFFF)
    ),
    FOREST(
        displayName = "フォレスト",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF81C784),
                Color(0xFF66BB6A),
                Color(0xFF4CAF50)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF388E3C),
                Color(0xFF2E7D32)
            )
        ),
        frontTextColor = Color(0xFF1B5E20),
        backTextColor = Color(0xFFFFFFFF)
    ),
    PURPLE(
        displayName = "パープル",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFBA68C8),
                Color(0xFFAB47BC),
                Color(0xFF9C27B0)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF7B1FA2),
                Color(0xFF6A1B9A)
            )
        ),
        frontTextColor = Color(0xFF4A148C),
        backTextColor = Color(0xFFFFFFFF)
    ),
    NIGHT(
        displayName = "ナイト",
        frontBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF616161),
                Color(0xFF424242)
            )
        ),
        backBrush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF424242),
                Color(0xFF303030)
            )
        ),
        frontTextColor = Color(0xFFFFFFFF),
        backTextColor = Color(0xFFFFFFFF)
    );

    companion object {
        fun fromName(name: String): CardDesign {
            return entries.find { it.name == name } ?: CLASSIC
        }
    }
}
