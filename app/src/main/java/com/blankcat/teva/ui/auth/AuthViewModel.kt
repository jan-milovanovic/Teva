package com.blankcat.teva.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankcat.teva.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _errorState: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private fun setError(value: String) {
        _errorState.value = value
        viewModelScope.launch {
            delay(1000L)
            _errorState.value = null
        }
    }

    init {
        viewModelScope.launch {
            authRepository.init()
        }
    }

    fun isAuthenticated(): StateFlow<Boolean> {
        return authRepository.isAuthenticated
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result.isFailure) {
                setError("Invalid login")
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = authRepository.register(email, password)
            if (result.isSuccess) {
                onSuccess()
            } else {
                setError("Registration failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
