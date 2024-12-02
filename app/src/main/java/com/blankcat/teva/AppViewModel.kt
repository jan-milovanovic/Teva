package com.blankcat.teva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankcat.teva.models.AppData
import com.blankcat.teva.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    private val _appData: MutableStateFlow<AppData?> = MutableStateFlow<AppData?>(null)
    val appData: StateFlow<AppData?> = _appData.asStateFlow()

    init {
        viewModelScope.launch {
            appRepository.appDataFlow.collect { data ->
                _appData.value = data
            }
        }
    }

    fun initializeAppData() {
        _appData.value = appRepository.getInitialAppData()
    }

    fun toggleDarkTheme(value: Boolean) {
        viewModelScope.launch {
            appRepository.toggleDarkTheme(value)
        }
    }
}