package com.example.androidbootcampiwatepref.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * QRコード生成ユーティリティクラス
 */
object QRCodeGenerator {

    /**
     * 文字列からQRコード画像を生成
     *
     * @param content QRコードに埋め込む文字列
     * @param size QRコード画像のサイズ（width = height）
     * @return 生成されたQRコードのBitmap
     */
    fun generateQRCode(content: String, size: Int = 512): Bitmap {
        // QRコード生成のオプション設定
        val hints = hashMapOf<EncodeHintType, Any>().apply {
            // エラー訂正レベルをMに設定（約15%のデータ復元が可能）
            put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
            // 文字エンコーディングをUTF-8に設定（日本語対応）
            put(EncodeHintType.CHARACTER_SET, "UTF-8")
            // QRコードの余白を1に設定（最小限の余白）
            put(EncodeHintType.MARGIN, 1)
        }
        
        // ZXingのQRCodeWriterを使用してQRコードを生成
        val writer = QRCodeWriter()
        // 文字列をQRコードのビットマトリクスに変換
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
        
        // ビットマトリクスのサイズを取得
        val width = bitMatrix.width
        val height = bitMatrix.height
        // ピクセル配列を作成（白黒の情報を格納）
        val pixels = IntArray(width * height)
        
        // ビットマトリクスをピクセル配列に変換
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                // true（1）なら黒、false（0）なら白
                pixels[offset + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        
        // ピクセル配列からBitmapを生成して返す
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}
