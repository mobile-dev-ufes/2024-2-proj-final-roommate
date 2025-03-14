package com.example.roommate.data.model

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

data class GroupModel(
    val id: String,
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int,
    val users: List<DocumentReference>
) : Serializable
