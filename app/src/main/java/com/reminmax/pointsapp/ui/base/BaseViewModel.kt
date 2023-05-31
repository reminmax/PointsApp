package com.reminmax.pointsapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _eventChannel = Channel<IBaseEvent>(Channel.BUFFERED)
    val eventsFlow = _eventChannel.receiveAsFlow()

    fun sendEvent(event: IBaseEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }

}