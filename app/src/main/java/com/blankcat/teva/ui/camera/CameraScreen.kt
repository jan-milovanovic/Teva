package com.blankcat.teva.ui.camera

import android.widget.Toast
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.blankcat.teva.ui.cards.CardsScreenViewModel
import com.blankcat.teva.components.BarcodeNameSheetContent
import com.blankcat.teva.models.TevaCollection
import com.blankcat.teva.ui.cards.utils.QrCodeDrawable
import com.blankcat.teva.ui.cards.utils.QrCodeViewModel
import com.blankcat.teva.ui.coupons.CouponsScreenViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    cardsViewModel: CardsScreenViewModel = hiltViewModel(),
    couponsViewModel: CouponsScreenViewModel = hiltViewModel(),
    barcodeType: TevaCollection,
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }


    var showModalBottomSheet by remember { mutableStateOf(false) }

    val barcode = remember { mutableStateOf<Barcode?>(null) }

    val cameraController = remember { LifecycleCameraController(context) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val barcodeScanner by remember {
        mutableStateOf(
            BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder().build()
            )
        )
    }

    DisposableEffect(cameraController) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose {
            cameraController.unbind()
            barcodeScanner.close()
        }
    }

    LaunchedEffect(cameraController) {
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(context)
            ) { result: MlKitAnalyzer.Result? ->
                val barcodeResults = result?.getValue(barcodeScanner)
                if ((barcodeResults == null) ||
                    (barcodeResults.size == 0) ||
                    (barcodeResults.first() == null)
                ) {
                    previewView.overlay.clear()
                    return@MlKitAnalyzer
                }

                val qrCodeViewModel = QrCodeViewModel(barcodeResults[0])
                val qrCodeDrawable = QrCodeDrawable(qrCodeViewModel)

                previewView.setOnTouchListener(qrCodeViewModel.qrCodeTouchCallback)
                previewView.overlay.clear()
                previewView.overlay.add(qrCodeDrawable)
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.controller = cameraController
            }
        )
        IconButton(
            onClick = { onClose() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .safeDrawingPadding()
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close camera",
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Button(
            onClick = {
                if (barcode.value == null) {
                    Toast.makeText(context, "No barcode detected", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                cameraController.unbind()
                showModalBottomSheet = true
            },
            modifier = Modifier
                .safeContentPadding()
                .safeGesturesPadding()
                .padding(32.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text("Add code")
        }

        if (showModalBottomSheet)
            ModalBottomSheet(
                onDismissRequest = {
                    showModalBottomSheet = false
                    cameraController.bindToLifecycle(lifecycleOwner)
                },
                sheetState = bottomSheetState
            ) {
                BarcodeNameSheetContent(
                    barcode = barcode.value,
                    addBarcode = {
                        if (barcodeType == TevaCollection.Card) {
                            cardsViewModel.addCard(it)
                        } else {
                            couponsViewModel.addCoupon(it)
                        }
                    },
                    onClose = onClose,
                    barcodeType = barcodeType,
                )
            }
    }
}