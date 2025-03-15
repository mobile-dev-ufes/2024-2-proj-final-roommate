package com.example.roommate.data.repository

import android.util.Log
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class GroupRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerGroup(group: GroupModel) {
        db.collection("group")
            .add(group)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterGroup", "Group added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("RegisterGroup", "Error adding group", e)
            }
    }

    fun getMembersFromGroup(groupId: String, callback: (List<UserModel>) -> Unit) {
        val userList = mutableListOf<UserModel>()
        db.collection("group").document(groupId)
            .get()
            .addOnSuccessListener { argsGroup ->
                val usersRefs = argsGroup.get("users")

                val list = if (usersRefs is List<*>) {
                    usersRefs.filterIsInstance<DocumentReference>()
                } else {
                    emptyList()
                }

                if (list.isEmpty()) {
                    callback(userList) // Return empty list if no users
                    return@addOnSuccessListener
                }

                var count = 0
                list.forEach { userRef ->
                    userRef.get().addOnSuccessListener { document ->
                        document?.let {
                            val name = it.getString("name") ?: ""
                            val bio = it.getString("bio") ?: ""
                            val phone = it.getString("phone") ?: ""
                            userList.add(UserModel(name, bio, phone))
                        }
                    }.addOnCompleteListener {
                        count++
                        if (count == list.size) {
                            callback(userList) // Return the list when all users are fetched
                        }
                    }
                }
            }.addOnFailureListener {
                println("Erro ao buscar grupo: ${it.message}")
                callback(emptyList()) // Return an empty list in case of failure
            }
    }

    fun fetchUserGroups(userId: String, callback: (List<GroupModel>) -> Unit) {
        val userRef: DocumentReference = db.collection("user").document(userId)

        db.collection("group")
            .whereArrayContains("users", userRef)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groupList = mutableListOf<GroupModel>()

                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    //val qttMembers = document.getLong("qttMembers")?.toInt() ?: 0

                    val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0

                    // Get the 'users' field which is a List of DocumentReferences
                    val usersRef = document.get("users") as? List<*> ?: emptyList<DocumentReference>()

                    // Convert the List<*> to List<DocumentReference> safely
                    val userRefsList = usersRef.mapNotNull { it as? DocumentReference }
                    val qttMembers = userRefsList.size

                    val advertisementId = document.getDocumentReference("advertisementId")

                    if (advertisementId == null) {
                        println("ERROR: on fetch group")
                    } else {
                        groupList.add(
                            GroupModel(
                                id,
                                name,
                                description,
                                qttMembers,
                                qttNotifications,
                                userRefsList,
                                advertisementId
                            )
                        )
                    }
                }

                callback(groupList)
            }
            .addOnFailureListener { e ->
                println("Error fetching groups: $e")
            }
    }
}