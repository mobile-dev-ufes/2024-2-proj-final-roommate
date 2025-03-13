package com.example.roommate.data.model

import java.io.Serializable

data class GroupModel(
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int
) : Serializable
