package com.blankcat.teva.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.blankcat.teva.R
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.TevaCollection
import com.blankcat.teva.models.generateBarcode
import com.google.mlkit.vision.barcode.common.Barcode

@Composable
fun BarcodeNameSheetContent(
    barcode: Barcode? = null,
    tevaBarcode: TevaBarcode? = null,
    addBarcode: ((TevaBarcode) -> Unit)? = null,
    updateBarcode: ((TevaBarcode) -> Unit)? = null,
    onClose: () -> Unit,
    barcodeType: TevaCollection,
) {

    // TODO(...): improve single purpose
    require((barcode != null && addBarcode != null) || tevaBarcode != null && updateBarcode != null) {
        "When adding a barcode, 'barcode' and 'addBarcode' must not be null. For updating barcodes, 'tevaBarcode' and 'updateBarcode' must not be null."
    }


    val context = LocalContext.current

    var cardText by remember { mutableStateOf(tevaBarcode?.name ?: "") }

    val barcodePainter: Painter = try {
        if (tevaBarcode != null) {
            generateBarcode(tevaBarcode.code, tevaBarcode.type)
        } else {
            generateBarcode(barcode?.displayValue ?: "null", barcode?.format ?: -1)
        }
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Failed barcode generation: ${barcode?.format ?: tevaBarcode?.type}",
            Toast.LENGTH_LONG
        ).show()
        painterResource(id = R.drawable.block)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        Image(
            barcodePainter,
            "code",
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .padding(4.dp)
        )
        Text(barcode?.displayValue ?: tevaBarcode?.code ?: "")
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            label = { Text("Name", color = MaterialTheme.colorScheme.onBackground) },
            value = cardText,
            onValueChange = { cardText = it },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (updateBarcode != null) {
                updateBarcode.invoke(tevaBarcode!!.copy(name = cardText))
                return@Button
            }

            if (barcode == null) {
                onClose.invoke()
                return@Button
            }

            addBarcode!!(
                TevaBarcode(
                    name = cardText,
                    code = barcode.displayValue ?: "null",
                    type = barcode.format,
                    collection = barcodeType,
                )
            )
            onClose.invoke()
        }) {
            Text("${if (updateBarcode != null) "Update" else "Add"} barcode")
        }
    }
}