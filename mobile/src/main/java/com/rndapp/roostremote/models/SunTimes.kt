package com.rndapp.roostremote.models

data class SunResults(val results: SunTimes?)

data class SunTimes(val sunrise: String, val sunset: String)