package com.rndapp.roostremote.models.tasks

import android.os.Handler

class WaitTask(val delay: Long): Task("Wait ${delay} ms") {
    companion object {
        const val typeDescription = "Wait some ms"
    }

    override fun execute(callback: () -> Unit) {
        val handler = Handler()
        handler.postDelayed({
            callback()
        }, delay)
    }
}