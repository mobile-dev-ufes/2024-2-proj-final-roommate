package com.example.roommate.data.repository

import android.util.Log
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GroupRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerGroup(group: GroupModel) {
        db.collection("group")
            .add(group)
            .addOnSuccessListener { documentReference ->
                group.id = documentReference.id
                Log.d("RegisterGroup", "Group added with ID: ${documentReference.id}")

                val groupRef = db.collection("group").document(group.id)

                // Reference path to the advertisement document
                db.collection("advertisement")
                    .document(group.advertisementId)
                    .update("groups", FieldValue.arrayUnion(groupRef))
                    .addOnSuccessListener {
                        Log.d("RegisterGroup", "Group reference added to advertisement")
                    }
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating advertisement with group reference", e)
                    }

                // Reference path to the advertisement document
                db.collection("user")
                    .document(group.users.first())
                    .update("groups", FieldValue.arrayUnion(groupRef))
                    .addOnSuccessListener {
                        Log.d("RegisterGroup", "Group reference added to user")
                    }
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating user with group reference", e)
                    }
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
        val groupList = mutableListOf<GroupModel>()
        db.collection("user").document(userId)
            .get()
            .addOnSuccessListener { argsUser ->
                val groupRefs = argsUser.get("groups")

                val list = if (groupRefs is List<*>) {
                    groupRefs.filterIsInstance<DocumentReference>()
                } else {
                    emptyList()
                }

                if (list.isEmpty()) {
                    callback(groupList) // Return empty list if no users
                    return@addOnSuccessListener
                }

                var count = 0
                list.forEach { groupRef ->
                    groupRef.get().addOnSuccessListener { document ->
                        document?.let {
                            val id = it.getString("id") ?: ""
                            val name = it.getString("name") ?: ""
                            val description = it.getString("description") ?: ""
                            val advertisementId = it.getString("advertisementId") ?: ""
                            val qttMembers = (it.getLong("qttMembers") as? Long)?.toInt() ?: 0
                            val isPrivate = it.getBoolean("isPrivate") ?: false

                            groupList.add(GroupModel(id, name, description, advertisementId, qttMembers, isPrivate))
                        }
                    }.addOnCompleteListener {
                        count++
                        if (count == list.size) {
                            callback(groupList) // Return the list when all users are fetched
                        }
                    }
                }
            }.addOnFailureListener {
                println("Erro ao buscar grupo: ${it.message}")
                callback(emptyList()) // Return an empty list in case of failure
            }
    }

//    fun fetchUserGroups(userId: String, callback: (List<GroupModel>) -> Unit) {
//        val userRef: DocumentReference = db.collection("user").document(userId)
//
//        db.collection("user")
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val groupList = mutableListOf<GroupModel>()
//
//                for (document in querySnapshot) {
//                    val id = document.id
//                    val name = document.getString("name") ?: ""
//                    val description = document.getString("description") ?: ""
//                    val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0
//                    val isPrivate = document.getBoolean("isPrivate") ?: false
//
//                    // Retrieve 'users' safely
//                    val usersList = document.get("users") as? List<*> ?: emptyList<Any>()
//                    val userIdsList = usersList.mapNotNull {
//                        when (it) {
//                            is String -> it
//                            is DocumentReference -> it.id
//                            else -> null
//                        }
//                    }
//
//                    val qttMembers = userIdsList.size
//
//                    val advertisementId = document.getDocumentReference("advertisementId")!!.id
//
//                    groupList.add(
//                        GroupModel(
//                            id,
//                            name,
//                            description,
//                            qttMembers,
//                            qttNotifications,
//                            userIdsList,
//                            advertisementId,
//                            isPrivate
//                        )
//                    )
//                }
//
//                callback(groupList)
//            }
//            .addOnFailureListener { e ->
//                println("❌ Error fetching groups: $e")
//            }
//    }
}