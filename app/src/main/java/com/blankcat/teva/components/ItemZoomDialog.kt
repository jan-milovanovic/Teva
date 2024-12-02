package com.blankcat.teva.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blankcat.teva.R
import com.blankcat.teva.models.CodeType
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.getBarcodeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemZoomDialog(tevaBarcode: TevaBarcode, isDialogVisible: (Boolean) -> Unit) {

    val barcodePainter: Painter = try {
        tevaBarcode.barcodePainter
    } catch (e: Exception) {
        painterResource(id = R.drawable.block)
    }

    BasicAlertDialog(
        onDismissRequest = { isDialogVisible(false) },
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier

        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Image(
                    painter = barcodePainter,
                    contentDescription = "code",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                if (getBarcodeType(tevaBarcode.type) == CodeType.Barcode) {
                    Text(
                        tevaBarcode.code,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                tevaBarcode.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}