package com.example.roommate.data.model

import com.google.firebase.firestore.DocumentReference
import org.w3c.dom.Document
import java.io.Serializable

data class GroupModel(
    val id: String,
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int,
    val users: List<DocumentReference>,
    val advertisementId: DocumentReference
) : Serializable {
    constructor(name: String, description: String, advertisementId: DocumentReference) : this("salve", name, description, 0, 0, emptyList<DocumentReference>(), advertisementId)
}
