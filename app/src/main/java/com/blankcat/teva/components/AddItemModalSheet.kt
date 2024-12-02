package com.blankcat.teva.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.blankcat.teva.R
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.TevaCollection
import com.blankcat.teva.models.scanBarcode
import kotlinx.coroutines.launch
import java.io.IOException

private const val CAMERA_PERMISSION_REQUEST_CODE = 1001


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemModalSheet(
    isSheetVisible: (Boolean) -> Unit,
    addBarcode: (TevaBarcode) -> Unit,
    navigateToCamera: () -> Unit,
    barcodeType: TevaCollection,
) {
    val context = LocalContext.current

    val lifecycleCoroutineScope = rememberCoroutineScope()

    var barcode: Barcode? by remember { mutableStateOf(null) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        try {
            val image = uri?.let { InputImage.fromFilePath(context, it) }

            lifecycleCoroutineScope.launch {
                barcode = scanBarcode(image) ?: return@launch
            }
        } catch (e: IOException) {
            Toast.makeText(context, "IO EXCEPTION ON FILE SCANNER: $e", Toast.LENGTH_SHORT).show()
        }
    }

    ModalBottomSheet(
        onDismissRequest = { isSheetVisible(false) },
        sheetState = bottomSheetState,
    ) {
        if (barcode == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { fileLauncher.launch("image/*") }
                        .padding(8.dp)
                ) {
                    Icon(painterResource(R.drawable.folder), "File system")
                    Text("Open file")
                }

                Spacer(modifier = Modifier.width(64.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable {
                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                                != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    context as Activity,
                                    arrayOf(Manifest.permission.CAMERA),
                                    CAMERA_PERMISSION_REQUEST_CODE
                                )
                            }

                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                navigateToCamera()
                            }

                        }
                ) {
                    Icon(painterResource(R.drawable.photo_camera), "File system")
                    Text("Take a photo")
                }
            }
        } else {
            BarcodeNameSheetContent(
                barcode = barcode,
                addBarcode = addBarcode,
                onClose = { isSheetVisible(false) },
                barcodeType = barcodeType,
            )
        }
    }
}