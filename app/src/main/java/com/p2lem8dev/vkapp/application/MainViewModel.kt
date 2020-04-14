package com.p2lem8dev.vkapp.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.p2lem8dev.vkapp.application.session.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            sessionManager.asFlow().collect { state ->
                when (state) {
                    SessionManager.State.Expired -> onSessionExpired()
                    SessionManager.State.Active,
                    SessionManager.State.Background,
                    SessionManager.State.Stopped -> Unit
                }
            }
        }
    }

    fun onResume() {
        sessionManager.onResume()
    }

    fun onPause() {
        sessionManager.onStop()
    }

    fun onSessionExpired() = Unit

    override fun onCleared() {
        sessionManager.onTerminate()
        super.onCleared()
    }
}