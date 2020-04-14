package com.p2lem8dev.vkapp.application.session

import android.content.SharedPreferences
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.*

internal class AppSessionManager(private val preferences: SharedPreferences) : SessionManager {

    private val stateChannel = ConflatedBroadcastChannel<SessionManager.State>()

    override fun asFlow(): Flow<SessionManager.State> = stateChannel.asFlow()

    override fun onResume() {
        val expired = try {
            val lastSessionTime = preferences.getString(PREF_LAST_SESSION_TIME, null)
                ?.toLong()
                ?: throw IllegalArgumentException()
            val currentTime = Date().time
            val diff = currentTime - lastSessionTime
            diff > SESSION_EXPIRE_TIME
        } catch (t: Throwable) {
            true
        }

        if (expired)
            stateChannel.offer(SessionManager.State.Expired)
        else
            stateChannel.offer(SessionManager.State.Active)
    }

    override fun onStop() {
        saveLastSessionTime()
        stateChannel.offer(SessionManager.State.Background)
    }

    override fun refresh() {
        saveLastSessionTime()
        onResume()
    }

    private fun saveLastSessionTime() {
        preferences.edit()
            .putString(PREF_LAST_SESSION_TIME, Date().time.toString())
            .apply()
    }

    override fun onTerminate() {
        preferences.edit()
            .putString(PREF_LAST_SESSION_TIME, null)
            .apply()
        stateChannel.offer(SessionManager.State.Stopped)
    }

    companion object {
        private const val SESSION_EXPIRE_TIME = 1_000L
        private const val PREF_LAST_SESSION_TIME = "app:session:preferences:session_time"
    }
}