package com.example.trackmyrun.core.domain.mapper

import com.example.trackmyrun.core.domain.model.PathPointModel
import com.google.android.gms.maps.model.LatLng

fun PathPointModel.toLatLng(): LatLng = LatLng(latitude, longitude)