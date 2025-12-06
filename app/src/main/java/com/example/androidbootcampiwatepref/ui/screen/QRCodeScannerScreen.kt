package com.example.androidbootcampiwatepref.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * QRコードスキャン画面
 * カメラでQRコードを読み取って名刺データを取得
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeScannerScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onQRCodeScanned: (String) -> Unit
) {
    // 現在のContextを取得（カメラ権限チェックやCameraX初期化に使用）
    val context = LocalContext.current
    
    // カメラ権限の状態を管理
    // 初期値はContextCompatでカメラ権限が許可されているかをチェック
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    // カメラ権限リクエスト用のランチャーを作成
    // ActivityResultContracts.RequestPermission()でシステムの権限ダイアログを表示
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // 権限が許可されたかどうかの結果を受け取り、状態を更新
        hasCameraPermission = isGranted
    }
    
    // 画面初回表示時に実行される副作用
    // カメラ権限が未許可の場合、自動的に権限リクエストを実行
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
    
    // カメラ権限の状態に応じて表示内容を切り替え
    Box(modifier = modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // 権限が許可されている場合：カメラプレビューを表示
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onQRCodeDetected = onQRCodeScanned
            )
        } else {
            // 権限が未許可の場合：権限リクエスト画面を表示
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // カメラ権限が必要であることを説明するテキスト
                    Text(
                        text = "カメラの権限が必要です",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // 権限リクエストを再実行するボタン
                    Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                        Text("権限を許可")
                    }
                }
            }
        }
    }
}

/**
 * カメラプレビューとQRコード検出
 *
 * CameraXを使用してカメラプレビューを表示し、
 * MLKit Barcode Scanningでリアルタイムにバーコードを検出
 */
@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    onQRCodeDetected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // カメラ処理用のExecutor（バックグラウンドスレッド）
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    // カメラプレビュー用のView
    val previewView = remember { PreviewView(context) }
    
    // 処理中フラグ（重複スキャン防止）
    var isProcessing by remember { mutableStateOf(false) }
    // QRコード検出済みフラグ（複数回のコールバック防止）
    var hasDetectedQR by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    
    // カメラの初期化と設定
    LaunchedEffect(previewView) {
        // CameraProviderを非同期で取得
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            // プレビュー用のUseCaseを作成
            val preview = Preview.Builder()
                .build()
                .also {
                    // PreviewViewにカメラ映像を接続
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            
            // 画像解析用のUseCaseを作成（QRコード検出に使用）
            val imageAnalyzer = ImageAnalysis.Builder()
                // 最新のフレームのみを処理（古いフレームは破棄）
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    // バックグラウンドスレッドで画像を解析
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        // 未処理かつQR未検出の場合のみ処理
                        if (!isProcessing && !hasDetectedQR) {
                            processImageProxy(imageProxy) { qrContent ->
                                if (!hasDetectedQR) {
                                    hasDetectedQR = true
                                    onQRCodeDetected(qrContent)
                                }
                            }
                        } else {
                            // 処理中または検出済みの場合はすぐに閉じる
                            imageProxy.close()
                        }
                    }
                }
            
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("QRCodeScanner", "カメラバインド失敗", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    Box(modifier = modifier) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        
        // スキャンガイド
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            // ガイドライン枠（四隅に角マーク）
            Box(
                modifier = Modifier.size(280.dp)
            ) {
                // 半透明の背景
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val cornerLength = 40f
                    val strokeWidth = 8f
                    val color = androidx.compose.ui.graphics.Color.White
                    
                    // 左上の角
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(cornerLength, 0f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, cornerLength),
                        strokeWidth = strokeWidth
                    )
                    
                    // 右上の角
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, 0f),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, cornerLength),
                        strokeWidth = strokeWidth
                    )
                    
                    // 左下の角
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                        end = androidx.compose.ui.geometry.Offset(cornerLength, size.height),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, size.height),
                        end = androidx.compose.ui.geometry.Offset(0f, size.height - cornerLength),
                        strokeWidth = strokeWidth
                    )
                    
                    // 右下の角
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width - cornerLength, size.height),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(size.width, size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height - cornerLength),
                        strokeWidth = strokeWidth
                    )
                }
                
                // 中央のテキスト
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Card(
                        modifier = Modifier.padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ) {
                        Text(
                            text = "QRコードをここに合わせてください",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * MLKit Barcode Scanningを使用してQRコードを検出
 *
 * @param imageProxy CameraXから取得した画像
 * @param onQRCodeDetected QRコード検出時のコールバック
 */
@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQRCodeDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        // MLKit用のInputImageに変換（回転情報も含む）
        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )
        
        // MLKitのバーコードスキャナーを取得
        val scanner = BarcodeScanning.getClient()
        
        // 画像からバーコードを検出（非同期処理）
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // 検出された全てのバーコードをチェック
                for (barcode in barcodes) {
                    // QRコード形式のみを処理
                    if (barcode.format == Barcode.FORMAT_QR_CODE) {
                        barcode.rawValue?.let { qrContent ->
                            // QRコードの内容をコールバック
                            onQRCodeDetected(qrContent)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QRCodeScanner", "バーコード処理失敗", e)
            }
            .addOnCompleteListener {
                // 処理完了後、必ずImageProxyを閉じる（メモリリーク防止）
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
