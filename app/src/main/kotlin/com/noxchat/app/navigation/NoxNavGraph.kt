package com.noxchat.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noxchat.app.ui.auth.SignInScreen
import com.noxchat.app.ui.auth.SignUpScreen
import com.noxchat.app.ui.auth.VerifyEmailScreen
import com.noxchat.app.ui.chat.ChatScreen
import com.noxchat.app.ui.main.MainScreen
import com.noxchat.app.ui.onboarding.OnboardingScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val SIGN_UP = "sign_up"
    const val SIGN_IN = "sign_in"
    const val MAIN = "main"
    const val CHAT = "chat/{chatId}"

    fun chat(chatId: String): String = "chat/$chatId"
}

@Composable
fun NoxNavGraph(
    startDestination: String,
    onOnboardingComplete: () -> Unit,
    onAuthComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onStart = {
                    navController.navigate(Routes.SIGN_UP)
                },
                onSignIn = {
                    navController.navigate(Routes.SIGN_IN)
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCodeSent = { email ->
                    navController.navigate("verify_email/$email")
                }
            )
        }

        composable(Routes.SIGN_IN) {
            SignInScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }

                    onAuthComplete()
                }
            )
        }

        composable("verify_email/{email}") { entry ->
            val email = entry.arguments
                ?.getString("email")
                .orEmpty()

            VerifyEmailScreen(
                email = email,
                onBack = {
                    navController.popBackStack()
                },
                onSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }

                    onAuthComplete()
                }
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onOpenChat = { chatId: String ->
                    navController.navigate(Routes.chat(chatId))
                }
            )
        }

        composable(Routes.CHAT) { entry ->
            val chatId = entry.arguments
                ?.getString("chatId")
                .orEmpty()

            ChatScreen(
                chatId = chatId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}