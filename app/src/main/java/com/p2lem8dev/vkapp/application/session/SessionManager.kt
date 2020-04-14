package com.p2lem8dev.vkapp.application.session

import kotlinx.coroutines.flow.Flow

interface SessionManager {

    enum class State {
        Expired,
        Active,
        Background,
        Stopped
    }

    fun asFlow(): Flow<State>

    fun onResume()

    fun onStop()

    fun onTerminate()

    fun refresh()
}