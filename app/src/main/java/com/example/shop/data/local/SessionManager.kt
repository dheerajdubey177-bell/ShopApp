package com.example.shop.data.local

import android.content.Context

class SessionManager(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "shop_session",
            Context.MODE_PRIVATE
        )

    fun saveUser(email: String, role: String) {

        prefs.edit()
            .putString("email", email)
            .putString("role", role)
            .apply()
    }

    fun getRole(): String? {
        return prefs.getString("role", null)
    }

    fun getUser(): String? {

        return prefs.getString(
            "email",
            null
        )
    }

    fun logout() {

        prefs.edit().clear().apply()
    }
}
