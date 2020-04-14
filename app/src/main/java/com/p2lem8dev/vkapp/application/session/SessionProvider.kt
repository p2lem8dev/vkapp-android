package com.p2lem8dev.vkapp.application.session

import android.content.Context
import android.content.ContextWrapper

object SessionProvider {

    fun provide(contextWrapper: ContextWrapper): SessionManager {
        val preferences = contextWrapper
            .getSharedPreferences(SESSION_PREFERENCES, Context.MODE_PRIVATE)

        return AppSessionManager(preferences)
    }

    private var sessionManager: SessionManager? = null
    private const val SESSION_PREFERENCES = "app:session:preferences"
}