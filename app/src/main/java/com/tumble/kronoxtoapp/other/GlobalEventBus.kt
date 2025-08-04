package com.tumble.kronoxtoapp.other

import kotlinx.coroutines.flow.MutableSharedFlow

object GlobalEventBus {
    val scheduleUpdateFlow = MutableSharedFlow<Unit>(replay = 0)
}
