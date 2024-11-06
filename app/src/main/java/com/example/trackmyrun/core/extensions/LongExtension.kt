package com.example.trackmyrun.core.extensions

import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.toStopwatchFormat(): String {
    return toDuration(DurationUnit.MILLISECONDS).toComponents { hours, minutes, seconds, _ ->
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
}