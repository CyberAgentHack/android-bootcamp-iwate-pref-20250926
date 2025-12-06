package com.example.androidbootcampiwatepref.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.androidbootcampiwatepref.domain.model.BusinessCardData
import java.io.File

/**
 * 名刺データをAndroid共有機能で送信するユーティリティ
 * 
 * JSONファイルと画像ファイルをセットで共有
 */
object BusinessCardSharer {
    
    /**
     * 名刺データを共有
     * 
     * @param context コンテキスト
     * @param businessCardData 名刺データ
     * @param profileImageUri プロフィール画像のURI（オプション）
     */
    fun shareBusinessCard(
        context: Context,
        businessCardData: BusinessCardData,
        profileImageUri: String?
    ) {
        try {
            val uris = mutableListOf<Uri>()
            
            // 1. 名刺データ（JSON）をファイルに保存
            val jsonFile = File(context.cacheDir, "business_card_${System.currentTimeMillis()}.json")
            jsonFile.writeText(businessCardData.toJson())
            
            val jsonUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                jsonFile
            )
            uris.add(jsonUri)
            
            // 2. プロフィール画像がある場合はコピー
            if (!profileImageUri.isNullOrEmpty()) {
                try {
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(profileImageUri))
                    if (inputStream != null) {
                        val imageFile = File(context.cacheDir, "profile_image_${System.currentTimeMillis()}.jpg")
                        imageFile.outputStream().use { output ->
                            inputStream.copyTo(output)
                        }
                        inputStream.close()
                        
                        val imageUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            imageFile
                        )
                        uris.add(imageUri)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 画像のコピーに失敗してもJSONは送信
                }
            }
            
            // 3. Android共有画面を開く
            val intent = Intent().apply {
                if (uris.size == 1) {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, uris[0])
                } else {
                    action = Intent.ACTION_SEND_MULTIPLE
                    putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                }
                type = "*/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_TITLE, "名刺を共有")
            }
            
            context.startActivity(Intent.createChooser(intent, "名刺を共有"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 受信した名刺ファイルからBusinessCardDataを復元
     * 
     * @param context コンテキスト
     * @param uri 受信したJSONファイルのURI
     * @return BusinessCardData、失敗時はnull
     */
    fun importBusinessCard(context: Context, uri: Uri): BusinessCardData? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val jsonString = inputStream?.bufferedReader()?.use { it.readText() }
            inputStream?.close()
            
            if (jsonString != null) {
                BusinessCardData.fromJson(jsonString)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
