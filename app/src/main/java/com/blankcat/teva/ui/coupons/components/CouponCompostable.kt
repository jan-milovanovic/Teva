package com.blankcat.teva.ui.coupons.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.blankcat.teva.R
import com.blankcat.teva.components.ItemZoomDialog
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.components.UpdateItemDialog
import com.blankcat.teva.models.CodeType
import com.blankcat.teva.models.getBarcodeType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponCompostable(
    tevaBarcode: TevaBarcode,
    deleteCoupon: () -> Unit,
    updateCoupon: () -> Unit,
) {
    var showUpdateCouponDialog by remember { mutableStateOf(false) }
    var showCouponZoomedDialog by remember { mutableStateOf(false) }

    val barcodePainter: Painter = try {
        tevaBarcode.barcodePainter
    } catch (e: Exception) {
        painterResource(id = R.drawable.block)
    }

    val color = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier = Modifier
            .height(160.dp)
            .combinedClickable(
                onClick = { showCouponZoomedDialog = true },
                onLongClick = { showUpdateCouponDialog = true }
            )
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(BiasAlignment(-1f, -0.5f))
                .background(color, shape = RoundedCornerShape(16.dp))
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(BiasAlignment(-1f, 0.5f))
                .background(color, shape = RoundedCornerShape(16.dp))
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(BiasAlignment(1f, 0.5f))
                .background(color, shape = RoundedCornerShape(16.dp))
        )
        Box(
            modifier = Modifier
                .size(32.dp)
                .align(BiasAlignment(1f, -0.5f))
                .background(color, shape = RoundedCornerShape(16.dp))
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(color, shape = RoundedCornerShape(8.dp))
                .align(Alignment.Center)
                .padding(8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Image(
                    barcodePainter,
                    "barcode",
                    modifier = Modifier.fillMaxWidth()
                )
                if (getBarcodeType(tevaBarcode.type) == CodeType.Barcode) {
                    Text(tevaBarcode.code, style = MaterialTheme.typography.labelSmall)
                }
            }
            Text(
                tevaBarcode.name,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis
            )
        }

    }


    if (showCouponZoomedDialog) {
        ItemZoomDialog(tevaBarcode, isDialogVisible = { showCouponZoomedDialog = it })
    }


    if (showUpdateCouponDialog) {
        UpdateItemDialog(
            name = tevaBarcode.name,
            isDialogVisible = { showUpdateCouponDialog = it },
            deleteItem = deleteCoupon,
            updateItem = updateCoupon,
        )
    }

}