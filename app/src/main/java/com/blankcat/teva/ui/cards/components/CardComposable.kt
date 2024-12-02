package com.blankcat.teva.ui.cards.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.blankcat.teva.R
import com.blankcat.teva.components.ItemZoomDialog
import com.blankcat.teva.components.UpdateItemDialog
import com.blankcat.teva.models.CodeType
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.models.getBarcodeType

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CardComposable(tevaBarcode: TevaBarcode, deleteCard: () -> Unit, updateCard: () -> Unit) {
    var showUpdateCardDialog by remember { mutableStateOf(false) }
    var showCardZoomedDialog by remember { mutableStateOf(false) }

    val barcodePainter: Painter = try {
        tevaBarcode.barcodePainter
    } catch (e: Exception) {
        painterResource(id = R.drawable.block)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .combinedClickable(
                onClick = { showCardZoomedDialog = true },
                onLongClick = { showUpdateCardDialog = true }
            )
            .padding(8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
        ) {
            Image(
                barcodePainter,
                "barcode",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            if (getBarcodeType(tevaBarcode.type) == CodeType.Barcode) {
                Text(tevaBarcode.code, style = MaterialTheme.typography.labelSmall)
            }
        }
        Text(tevaBarcode.name)
    }

    if (showCardZoomedDialog) {
        ItemZoomDialog(tevaBarcode, isDialogVisible = { showCardZoomedDialog = it })
    }


    if (showUpdateCardDialog) {
        UpdateItemDialog(
            name = tevaBarcode.name,
            isDialogVisible = { showUpdateCardDialog = it },
            deleteItem = deleteCard,
            updateItem = updateCard
        )
    }
}