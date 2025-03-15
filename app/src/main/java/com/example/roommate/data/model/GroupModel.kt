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
    val isPrivate: Boolean
) : Serializable {

    // Constructor without the ID, to the createGroup
    constructor(
        name: String,
        description: String,
        advertisementId: String,
        users: List<String>,
        isPrivate: Boolean
    ) : this(
        id = "", // Empty initially, will be assigned later
        name = name,
        description = description,
        qttMembers = users.size, // Set the number of members based on the users list size
        qttNotifications = 0, // Default value, can be changed later
        users = users,
        advertisementId = advertisementId,
        isPrivate = isPrivate
    )

    // Constructor to get only the necessary data to show in InterestedGourps
    constructor(
        id: String,
        name: String,
        description: String,
        advertisementId: String,
        qttMembers: Int,
        isPrivate: Boolean
    ) : this(
        id = id,
        name = name,
        description = description,
        qttMembers = qttMembers,
        qttNotifications = 0,
        users = emptyList<String>(),
        advertisementId = advertisementId,
        isPrivate = isPrivate
    )
}