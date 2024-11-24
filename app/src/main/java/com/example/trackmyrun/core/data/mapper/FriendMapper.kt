package com.example.trackmyrun.core.data.mapper

import com.example.trackmyrun.core.data.local.entity.FriendEntity
import com.example.trackmyrun.core.domain.model.FriendModel

fun FriendEntity.toModel(): FriendModel = FriendModel(
    startTimestamp = startTimestamp,
    name = name,
    id = id
)

fun FriendModel.toEntity(): FriendEntity = FriendEntity(
    startTimestamp = startTimestamp,
    name = name,
    id = id
)