package com.example.trackmyrun.core.extensions

import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import java.time.Instant
import java.time.ZoneId

fun Long.toStopwatchUserFormat(): String = toDuration(DurationUnit.MILLISECONDS).toComponents { hours, minutes, seconds, _ ->
    "%02dh:%02dm:%02ds".format(hours, minutes, seconds)
}

fun Long.toStopwatchFormat(): String = toDuration(DurationUnit.MILLISECONDS).toComponents { hours, minutes, seconds, _ ->
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    }

fun Long.toLabelDateFormat(): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofPattern("dd MMM\nHH:mm"))

fun Long.toDateTimeFormat(): String = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))