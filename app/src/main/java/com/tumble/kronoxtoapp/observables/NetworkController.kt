package com.tumble.kronoxtoapp.observables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import javax.inject.Inject

class NetworkController @Inject constructor() : ViewModel() {

    private val _connected = MutableStateFlow(false)
    val connected = _connected.asStateFlow()

    init {
        checkConnection()
    }

    private fun checkConnection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Attempt to open a connection to a remote server (use a common address like Google's public DNS server)
                val address = InetSocketAddress("8.8.8.8", 53)
                val socketChannel = SocketChannel.open()
                socketChannel.connect(address)
                socketChannel.close()
                _connected.value = true
            } catch (e: Exception) {
                _connected.value = false
            }
        }
    }
}