package com.noxchat.app.data.supabase

object SupabaseConfig {

    // Замените на Project URL из Supabase.
    const val SUPABASE_URL = "https://jrzkgqjttqzezetssxwn.supabase.co"

    // Замените на Anon public key из Supabase.
    // НЕ используйте service_role key в приложении.
    const val SUPABASE_ANON_KEY = "sb_publishable_k2d7QvZhXO8FENJCEbaoOA_WvBntKFd"

    const val AUTH_BASE_URL = "$SUPABASE_URL/auth/v1"
}