package edu.utap.mytrivia.ui.home.fragment.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class QuizTimer(
    lifecycle: Lifecycle,
    private val timerStart: () -> Unit,
    private val timerStop: () -> Unit
) : LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun startTimer() {
        timerStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopTimer() {
        timerStop()
    }

}