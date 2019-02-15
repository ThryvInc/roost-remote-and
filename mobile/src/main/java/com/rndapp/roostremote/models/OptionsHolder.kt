package com.rndapp.roostremote.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by ell on 5/15/15.
 */
class OptionsHolder : Serializable {
    var key: String? = null
        protected set
    var staticValues: ArrayList<Option>? = null
        protected set
    var values: ArrayList<Option>? = null
        protected set
}
