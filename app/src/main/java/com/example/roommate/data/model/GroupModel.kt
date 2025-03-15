package com.example.roommate.data.model

import java.io.Serializable

data class GroupModel(
    val id: String,
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int,
    val users: List<String>,
    val advertisementId: String
) : Serializable {
    constructor(name: String, description: String, advertisementId: String) :
            this("salve", name, description, 0, 0, emptyList(), advertisementId)

    constructor(name: String, description: String, advertisementId: String, qttMembers: Int) :
            this("salve", name, description, qttMembers, 0, emptyList(), advertisementId)

    constructor(name: String, description: String, advertisementId: String, users: List<String>) :
            this("salve", name, description, 1, 0, users, advertisementId)
}
