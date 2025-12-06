package com.example.androidbootcampiwatepref.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 名刺風デザインのスプラッシュ画面
 * 
 * 2枚のカードが画面を駆け抜けた後、アプリ名がフェードインする演出を表示
 */
@Composable
fun SplashScreen() {
    // アニメーション用の状態
    var animationPhase by remember { mutableStateOf(0) }
    
    // アニメーションの進行度(0f〜1f)
    val animationProgress by animateFloatAsState(
        targetValue = if (animationPhase == 0) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1200,
            easing = LinearEasing
        ),
        label = "card_exchange"
    )
    
    // テキスト表示用のアルファ値
    val textAlpha by animateFloatAsState(
        targetValue = if (animationProgress > 0.7f) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
        label = "text_fade_in"
    )
    
    // 0.3秒後にアニメーション開始
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        animationPhase = 1
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                )
            )
    ) {
        // 左側のカード(上端から下端へ駆け抜ける)
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(320.dp)
                .align(Alignment.TopStart)
                .offset(
                    x = 30.dp,
                    y = ((-320 + 1500 * animationProgress).dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF616161)
            )
        ) {}
        
        // 右側のカード(下端から上端へ駆け抜ける)
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(320.dp)
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-30).dp,
                    y = ((320 - 1500 * animationProgress).dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF616161)
            )
        ) {}
        
        // カード通過後にアプリ名表示
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(textAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "名刺管理",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Business Card App",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
