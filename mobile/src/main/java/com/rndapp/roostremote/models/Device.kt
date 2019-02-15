package com.rndapp.roostremote.models

import java.io.Serializable
import java.util.*

/**
 * Created by ell on 1/11/16.
 */
class Device : ServerObject(), Serializable {
    var name: String? = null
    val host: String? = null
    val hostNamespace: String? = null
    var describer: String? = null
    val describerNamespace: String? = null
    val deviceTypeId: String? = null
    var properties: HashMap<*, *>? = null
    val imageUrl: String? = null
}
