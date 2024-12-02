package com.blankcat.teva.ui.coupons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.repository.CouponRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponsScreenViewModel @Inject constructor(
    private val couponRepository: CouponRepository,
) : ViewModel() {

    var searchQuery = MutableStateFlow("")
    private var _searchResults = MutableStateFlow(listOf<TevaBarcode>())

    val searchResults: StateFlow<List<TevaBarcode>> =
        combine(searchQuery, _searchResults) { query, coupons ->
            if (query.isBlank()) {
                coupons
        } else {
                coupons.filter { it.name.contains(query.trim(), ignoreCase = true) }
        }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _searchResults.value)


    init {
        viewModelScope.launch {
            couponRepository.getCoupons().collect { coupons ->
                _searchResults.value = coupons
            }
        }
    }

    fun updateQuery(text: String) {
        searchQuery.value = text
    }

    fun addCoupon(coupon: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.insertCoupon(coupon)
        }
    }

    fun deleteCoupon(coupon: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.deleteCoupons(coupon)
        }
    }

    fun updateCoupon(tevaBarcode: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            couponRepository.updateCoupon(tevaBarcode)
        }
    }
}