package com.blankcat.teva.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankcat.teva.data.dao.TransactionRunner
import com.blankcat.teva.models.CartNote
import com.blankcat.teva.models.Product
import com.blankcat.teva.repository.CartNoteRepository
import com.blankcat.teva.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

data class CartState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val cartNote: CartNote? = null,
    val recurringProducts: List<Product?> = listOf(),
)

@HiltViewModel
class CartScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartNoteRepository: CartNoteRepository,
    private val transactionRunner: TransactionRunner,
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(CartState())
    val uiState: StateFlow<CartState> = _uiState.asStateFlow()

    private var debounceJob: Job? = null
    private var checkboxJob: Job? = null

    init {
        viewModelScope.launch {
            productRepository.getProducts()
                .combine(cartNoteRepository.getNote()) { products, cartNote ->
                    CartState(recurringProducts = products, cartNote = cartNote)
                }.collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun addRepurchasableProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
                productRepository.insertProduct(product)
        }
    }

    fun updateRepurchasableProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
                productRepository.updateProduct(product)
        }
    }

    fun removeRepurchasableProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
                productRepository.deleteProduct(product)
        }
    }

    fun checkItemPurchased(product: Product) {
        _uiState.update { currentState ->
            val recurringProducts = currentState.recurringProducts.toMutableList()
            val index = currentState.recurringProducts.indexOfFirst { it?.id == product.id }

            recurringProducts[index] = product.copy(isChecked = !product.isChecked)
            currentState.copy(recurringProducts = recurringProducts)
        }

        if (product.isChecked) {
            checkboxJob?.cancel()
            return
        }

        val refreshedProduct = product.copy(lastPurchase = ZonedDateTime.now(), isChecked = false)
        checkboxJob = viewModelScope.launch {
            delay(2000L)
            transactionRunner {
                productRepository.updateProduct(refreshedProduct)
            }
        }
    }

    fun updateShoppingList(text: String) {
        _uiState.update { currentState ->
            currentState.copy(cartNote = CartNote(text = text))
        }

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(500L)
            transactionRunner {
                cartNoteRepository.insertNote(CartNote(text = text))
            }
        }
    }
}