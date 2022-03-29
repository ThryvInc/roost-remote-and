package com.rndapp.roostremote.models.tasks

import com.rndapp.roostremote.models.Flow

class FlowTask(val flowName: String): Task(flowName) {
    companion object {
        const val typeDescription = "Execute another batch"
    }

    override fun execute(callback: () -> Unit) {
        Flow.triggerFlowNamed(flowName, callback)
    }
}
