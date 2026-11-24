package com.noxchat.app.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noxchat.app.data.supabase.SupabaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val isError: Boolean = false,
    val email: String = "",
    val isCodeSent: Boolean = false
)

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = SupabaseAuthRepository(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email.trim(),
            message = "",
            isError = false
        )
    }

    fun signUp(
        email: String,
        password: String
    ) {
        if (!isEmailValid(email)) {
            showError("Введите корректный email.")
            return
        }

        if (password.length < 8) {
            showError("Пароль должен содержать минимум 8 символов.")
            return
        }

        viewModelScope.launch {
            setLoading()

            val result = repository.signUp(
                email = email,
                password = password
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = result.message,
                isError = !result.isSuccess,
                isCodeSent = result.isSuccess
            )
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        if (!isEmailValid(email)) {
            showError("Введите корректный email.")
            return
        }

        if (password.isBlank()) {
            showError("Введите пароль.")
            return
        }

        viewModelScope.launch {
            setLoading()

            val result = repository.signIn(
                email = email,
                password = password
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = result.message,
                isError = !result.isSuccess
            )
        }
    }

    fun sendCode(email: String) {
        if (!isEmailValid(email)) {
            showError("Введите корректный email.")
            return
        }

        viewModelScope.launch {
            setLoading()

            val result = repository.sendEmailCode(email)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = result.message,
                isError = !result.isSuccess,
                isCodeSent = result.isSuccess
            )
        }
    }

    fun verifyCode(
        email: String,
        code: String
    ) {
        if (code.length < 6) {
            showError("Введите 6-значный код.")
            return
        }

        viewModelScope.launch {
            setLoading()

            val result = repository.verifyEmailCode(
                email = email,
                code = code
            )

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = result.message,
                isError = !result.isSuccess
            )
        }
    }

    private fun setLoading() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            message = "",
            isError = false
        )
    }

    private fun showError(message: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            message = message,
            isError = true
        )
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@") &&
            email.contains(".") &&
            email.length > 5
    }
}