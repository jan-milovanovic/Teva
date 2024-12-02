import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.blankcat.teva.models.Product
import com.blankcat.teva.ui.cart.components.ProductModalBottomSheet
import com.blankcat.teva.ui.cart.components.RecurringProduct


@Composable
fun RecurringProductList(
    recurringProducts: List<Product?>?,
    checkItemPurchased: (Product) -> Unit,
    addRepurchasableProduct: (Product) -> Unit,
    updateRepurchasableProduct: (Product) -> Unit,
    removeRepurchasableProduct: (Product) -> Unit
) {
    var showModalBottomSheet by remember { mutableStateOf(false) }

    var product by remember { mutableStateOf<Product?>(null) }

    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return Offset.Zero
            }
        }
    }



    Column(modifier = Modifier.imePadding()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Recurring products", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { showModalBottomSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        }
        LazyColumn(
            state = listState,
            // flingBehavior = rememberSnapFlingBehavior(lazyListState = listState, snapPosition = SnapPosition.Start),
            modifier = Modifier
                .requiredHeightIn(min = 0.dp, max = 300.dp)
                .nestedScroll(nestedScrollConnection)
        ) {
            items(recurringProducts.orEmpty()) {
                RecurringProduct(
                    product = it!!,
                    checkItemPurchased = { checkItemPurchased(it) },
                    updateProduct = {
                        product = it
                        showModalBottomSheet = true
                    },
                    removeProduct = { removeRepurchasableProduct(it) }
                )
            }
        }
        if (showModalBottomSheet) {
            ProductModalBottomSheet(
                product = product,
                isSheetVisible = {
                    showModalBottomSheet = it
                    product = null
                },
                updateRepurchasableProduct = updateRepurchasableProduct,
                addRepurchasableProduct = addRepurchasableProduct,
            )
        }
    }
}