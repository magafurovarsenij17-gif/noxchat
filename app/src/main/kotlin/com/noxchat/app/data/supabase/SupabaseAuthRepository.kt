package com.noxchat.app.data.supabase

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AuthResult(
    val isSuccess: Boolean,
    val message: String,
    val needsEmailConfirmation: Boolean = false
)

data class Session(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String
)

class SupabaseAuthRepository(
    private val context: Context
) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("nox_auth", Context.MODE_PRIVATE)

    fun getSession(): Session? {
        val accessToken = preferences.getString("access_token", null) ?: return null
        val refreshToken = preferences.getString("refresh_token", null) ?: return null
        val userId = preferences.getString("user_id", null) ?: return null
        val email = preferences.getString("email", null) ?: return null

        return Session(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            email = email
        )
    }

    fun signOut() {
        preferences.edit().clear().apply()
    }

    suspend fun signUp(
        email: String,
        password: String
    ): AuthResult {
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        return safeRequest(
            url = "${SupabaseConfig.AUTH_BASE_URL}/signup",
            method = "POST",
            body = body,
            onSuccess = { response ->
                val user = response.optJSONObject("user")

                val confirmationSent = user
                    ?.optString("confirmation_sent_at", "")
                    .orEmpty()
                    .isNotEmpty()

                val emailConfirmed = user
                    ?.optString("email_confirmed_at", "")
                    .orEmpty()
                    .isNotEmpty()

                AuthResult(
                    isSuccess = true,
                    message = if (confirmationSent || emailConfirmed) {
                        "Код подтверждения отправлен на почту."
                    } else {
                        "Аккаунт создан."
                    },
                    needsEmailConfirmation = true
                )
            }
        )
    }

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult {
        val body = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        return safeRequest(
            url = "${SupabaseConfig.AUTH_BASE_URL}/token?grant_type=password",
            method = "POST",
            body = body,
            onSuccess = { response ->
                saveSession(response)

                AuthResult(
                    isSuccess = true,
                    message = "Вход выполнен."
                )
            }
        )
    }

    suspend fun sendEmailCode(email: String): AuthResult {
        val body = JSONObject().apply {
            put("email", email)
            put("create_user", true)
        }

        return safeRequest(
            url = "${SupabaseConfig.AUTH_BASE_URL}/otp",
            method = "POST",
            body = body,
            onSuccess = {
                AuthResult(
                    isSuccess = true,
                    message = "Код отправлен на $email."
                )
            }
        )
    }

    suspend fun verifyEmailCode(
        email: String,
        code: String
    ): AuthResult {
        val body = JSONObject().apply {
            put("type", "email")
            put("email", email)
            put("token", code)
        }

        return safeRequest(
            url = "${SupabaseConfig.AUTH_BASE_URL}/verify",
            method = "POST",
            body = body,
            onSuccess = { response ->
                saveSession(response)

                AuthResult(
                    isSuccess = true,
                    message = "Email подтверждён."
                )
            }
        )
    }

    private suspend fun safeRequest(
        url: String,
        method: String,
        body: JSONObject,
        onSuccess: (JSONObject) -> AuthResult
    ): AuthResult {
        return try {
            val response = withContext(Dispatchers.IO) {
                request(
                    url = url,
                    method = method,
                    body = body
                )
            }

            if (response.isSuccessful) {
                onSuccess(response.json)
            } else {
                AuthResult(
                    isSuccess = false,
                    message = response.errorMessage
                )
            }
        } catch (exception: Exception) {
            AuthResult(
                isSuccess = false,
                message = "Ошибка сети:${exception.javaClass.simpleName}${exception.message}"
            )
        }
    }

    private fun saveSession(json: JSONObject) {
        val accessToken = json.optString("access_token")
        val refreshToken = json.optString("refresh_token")

        val user = json.optJSONObject("user")
        val userId = user?.optString("id").orEmpty()
        val email = user?.optString("email").orEmpty()

        preferences.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .putString("user_id", userId)
            .putString("email", email)
            .apply()
    }

    private fun request(
        url: String,
        method: String,
        body: JSONObject
    ): SupabaseResponse {
        val connection = URL(url).openConnection() as HttpURLConnection

        return try {
            connection.requestMethod = method
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            connection.setRequestProperty("apikey", SupabaseConfig.SUPABASE_ANON_KEY)
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")

            connection.doOutput = true
            connection.outputStream.use { stream ->
                stream.write(body.toString().toByteArray())
            }

            val responseCode = connection.responseCode
            val responseText = if (responseCode in 200..299) {
                connection.inputStream.bufferedReader().readText()
            } else {
                connection.errorStream?.bufferedReader()?.readText().orEmpty()
            }

            val json = if (responseText.isNotBlank()) {
                try {
                    JSONObject(responseText)
                } catch (exception: Exception) {
                    JSONObject()
                }
            } else {
                JSONObject()
            }

            val errorMessage = json.optString("msg")
                .ifBlank {
                    json.optString("error_description")
                }
                .ifBlank {
                    json.optString("message")
                }
                .ifBlank {
                    json.optString("error")
                }
                .ifBlank {
                    "Не удалось выполнить запрос. Код: $responseCode"
                }

            SupabaseResponse(
                isSuccessful = responseCode in 200..299,
                json = json,
                errorMessage = errorMessage
            )
        } finally {
            connection.disconnect()
        }
    }
}

private data class SupabaseResponse(
    val isSuccessful: Boolean,
    val json: JSONObject,
    val errorMessage: String
)