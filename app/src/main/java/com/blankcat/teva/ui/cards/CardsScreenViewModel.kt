package com.blankcat.teva.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankcat.teva.models.TevaBarcode
import com.blankcat.teva.repository.CardRepository
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
class CardsScreenViewModel @Inject constructor(
    private val cardRepository: CardRepository,
) : ViewModel() {

    var searchQuery = MutableStateFlow("")
    private var _searchResults = MutableStateFlow(listOf<TevaBarcode>())

    val searchResults: StateFlow<List<TevaBarcode>> =
        combine(searchQuery, _searchResults) { query, cards ->
            if (query.isBlank()) {
                cards
            } else {
                cards.filter { it.name.contains(query.trim(), ignoreCase = true) }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _searchResults.value)


    init {
        viewModelScope.launch {
            cardRepository.getCards().collect { cards ->
                _searchResults.value = cards
            }
        }
    }

    fun updateQuery(text: String) {
        searchQuery.value = text
    }

    fun addCard(tevaBarcode: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.insertCard(tevaBarcode)
        }
    }

    fun deleteCard(tevaBarcode: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.deleteCards(tevaBarcode)
        }
    }

    fun updateCard(tevaBarcode: TevaBarcode) {
        viewModelScope.launch(Dispatchers.IO) {
            cardRepository.updateCard(tevaBarcode)
        }
    }
}