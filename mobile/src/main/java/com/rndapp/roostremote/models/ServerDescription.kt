package com.rndapp.roostremote.models

import java.io.Serializable

/**
 * Created by ell on 8/27/16.
 */
class ServerDescription : Serializable {
    val host: String? = null
    val hostNamespace: String? = null
    val endpoints: List<Endpoint>? = null
}
