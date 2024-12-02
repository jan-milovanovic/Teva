package com.blankcat.teva.ui.cart

import RecurringProductList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.blankcat.teva.ui.cart.components.ShoppingList

@Composable
fun CartScreen(
    viewModel: CartScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.verticalScroll(scrollState).imePadding()) {
        RecurringProductList(
            recurringProducts = uiState.recurringProducts,
            checkItemPurchased = viewModel::checkItemPurchased,
            addRepurchasableProduct = viewModel::addRepurchasableProduct,
            updateRepurchasableProduct = viewModel::updateRepurchasableProduct,
            removeRepurchasableProduct = viewModel::removeRepurchasableProduct,
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            ShoppingList(
                listValue = uiState.cartNote?.text ?: "",
                onListUpdate = viewModel::updateShoppingList,
            )
        }

    }
}