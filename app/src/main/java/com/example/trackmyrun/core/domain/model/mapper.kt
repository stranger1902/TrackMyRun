package com.example.trackmyrun.core.domain.model

import com.google.android.gms.maps.model.LatLng

fun PathPointModel.toLatLng(): LatLng = LatLng(latitude, longitude)