package com.example.roommate.data.model

import java.io.Serializable

data class GroupModel(
    var id: String = "", // The ID will be assigned after the group is added to Firebase
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int,
    val users: List<String>,
    val advertisementId: String,
    val isPrivate: Boolean,
    val photoUri: String? = null
) : Serializable {

    constructor(
        id: String,
        name: String,
        description: String,
        advertisementId: String,
        qttMembers: Int,
        isPrivate: Boolean,
        photoUri: String
    ) : this(
        id = id,
        name = name,
        description = description,
        qttMembers = qttMembers,
        qttNotifications = 0,
        users = emptyList<String>(),
        advertisementId = advertisementId,
        isPrivate = isPrivate,
        photoUri= photoUri
    )
}