package com.blankcat.teva.ui.cart.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.blankcat.teva.components.UpdateItemDialog
import com.blankcat.teva.models.Product
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecurringProduct(
    product: Product,
    checkItemPurchased: (() -> Unit)? = null,
    updateProduct: (() -> Unit)? = null,
    removeProduct: (() -> Unit)? = null,
) {
    var isAlertDialogOpen by remember { mutableStateOf(false) }

    val animatedProgressBar by animateFloatAsState(
        targetValue = product.progressValue,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress_value_bar"
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onLongClick = { isAlertDialogOpen = true }) { }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(product.title, style = MaterialTheme.typography.titleSmall)

            if (product.store.isNotEmpty()) {
                val storeText = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                        append("Found in: ")
                    }
                    withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                        append(product.store)
                    }
                }
                Text(storeText)
            }

            val repurchaseText = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                    append("Repurchase in: ")
                }
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {

                    val remainingTime = product.remainingTime.toHours()
                    append(
                        when {
                            remainingTime >= 48L -> "${
                                (product.remainingTime.toHours().toFloat() / 24).roundToInt()
                            } days"

                            remainingTime >= 24L -> "Tomorrow"
                            else -> "Today"
                        }
                    )
                }
            }

            Text(repurchaseText)

            LinearProgressIndicator(
                drawStopIndicator = { },
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .height(8.dp),
                progress = { animatedProgressBar },
                )
        }

        Checkbox(
            checked = product.isChecked,
            onCheckedChange = { checkItemPurchased?.invoke() },
            modifier = Modifier.scale(1.5f)
        )

        if (isAlertDialogOpen) {
            UpdateItemDialog(
                name = product.title,
                isDialogVisible = { isAlertDialogOpen = it },
                updateItem = { updateProduct?.invoke() },
                deleteItem = { removeProduct?.invoke() },
            )
        }
    }
}