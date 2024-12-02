package com.blankcat.teva.ui.cart.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blankcat.teva.models.Product
import com.blankcat.teva.models.Repeat
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductModalBottomSheet(
    product: Product?,
    isSheetVisible: (Boolean) -> Unit,
    updateRepurchasableProduct: (Product) -> Unit,
    addRepurchasableProduct: (Product) -> Unit,
) {
    var productName by remember { mutableStateOf(product?.title ?: "") }
    var isProductValid by remember { mutableStateOf(productName.isNotEmpty()) }
    var storeName by remember { mutableStateOf(product?.store ?: "") }
    var isDropdownMenuOpen by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedRepeat: Repeat = product?.repeat ?: Repeat.WEEKLY


    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = { isSheetVisible(false) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                label = { Text("Item", color = MaterialTheme.colorScheme.onBackground) },
                value = productName,
                isError = !isProductValid,
                onValueChange = {
                    productName = it
                    isProductValid = productName.isNotEmpty()
                },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                label = { Text("Store", color = MaterialTheme.colorScheme.onBackground) },
                value = storeName,
                onValueChange = { storeName = it },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(OutlinedTextFieldDefaults.MinWidth)
            ) {
                Text("Repeat:")
                Spacer(modifier = Modifier.width(16.dp))
                Text(selectedRepeat.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { isDropdownMenuOpen = true }
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                        .fillMaxWidth()
                )
                DropdownMenu(
                    expanded = isDropdownMenuOpen,
                    onDismissRequest = { isDropdownMenuOpen = false }
                ) {
                    Repeat.entries.forEach {
                        DropdownMenuItem(text = { Text(it.name) },
                            onClick = {
                                selectedRepeat = it
                                isDropdownMenuOpen = false
                            }
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (!isProductValid) {
                        return@Button
                    }
                    if (product != null) {
                        updateRepurchasableProduct(
                            product.copy(
                                title = productName,
                                store = storeName,
                                repeat = selectedRepeat,
                            )
                        )
                    } else {
                        addRepurchasableProduct(
                            Product(
                                title = productName,
                                store = storeName,
                                repeat = selectedRepeat,
                                lastPurchase = ZonedDateTime.now(),
                            ),
                        )
                    }
                    isSheetVisible(false)

                }
            ) {
                Text(if (product != null) "Update item" else "Add item")
            }
        }

    }

}
