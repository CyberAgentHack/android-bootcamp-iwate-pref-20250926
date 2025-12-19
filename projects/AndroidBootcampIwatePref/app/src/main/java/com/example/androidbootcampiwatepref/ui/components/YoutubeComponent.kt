package com.example.androidbootcampiwatepref.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidbootcampiwatepref.domain.domainobject.Channel
import com.example.androidbootcampiwatepref.domain.domainobject.Video
import kotlin.Boolean
import com.example.androidbootcampiwatepref.viewmodel.YoutubeViewModel


//登録者を表示するコンポーザブル
@Composable
fun ChannelItem(
    channel: Channel,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, //水平方向に中央寄せ
        modifier = modifier
    ) {
        Image(
            painter = painterResource(channel.imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(65.dp)
                .border(//枠線
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = CircleShape
                )
                .background(//背景色
                    color = Color.White,
                    shape = CircleShape
                )
        )
        Text(
            text = channel.name,
            fontSize = 11.sp
        )
    }
}

//動画アイテムを表示するコンポーザブル
@Composable
fun VideoItem(
    video: Video,
    viewModel: YoutubeViewModel,
    modifier: Modifier = Modifier,
    showLikeButton: Boolean = false
) {
    val likeCount by viewModel.getLikeCount(video.id).collectAsState(initial = video.likeCount)
    Column(
        modifier = modifier.padding(bottom = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // 高さを固定
        ) {
            //動画のサムネ
            Image(
                painter = painterResource(id = video.thumbnailRes),
                contentDescription = "動画サムネ",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // サムネの右下に動画時間を表示
            Text(
                text = "10:25",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Boxの右下に配置
                    .padding(8.dp)//外側の余白
                    .background(
                        Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 2.dp)//内側の余白
            )
        }


        Row(
            modifier = Modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            // チャンネルアイコン
            Image(
                painter = painterResource(id = video.channelIconRes), // ダミーのチャンネルアイコン
                contentDescription = "チャンネルアイコン",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .border(//枠線
                        width = 1.dp,
                        color = Color.DarkGray,
                        shape = CircleShape
                    )
                    .background(//背景色
                        color = Color.White,
                        shape = CircleShape
                    )
            )

            // タイトルと情報
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f), // 残りのスペースを全て使う
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = video.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2
                )
                Text(
                    text = "${video.channelName}・${video.viewCount}・${video.uploadedAt}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }

            if (showLikeButton) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp), // アイコンとテキストの間のスペース
                    modifier = Modifier.padding(top = 8.dp)
                ) {

                    //いいねボタン
                    IconButton(
                        onClick = { viewModel.onLikeClicked(video.id) },
                        modifier = Modifier.size(24.dp) // ボタンのサイズを小さく調整
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ThumbUp,
                            contentDescription = "いいね",
                        )
                    }

                    // いいね数
                    Text(
                        text = likeCount.toString(),
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(top = 8.dp, start = 8.dp) // 位置を少し調整
                    )
                }
            }
        }
    }
}