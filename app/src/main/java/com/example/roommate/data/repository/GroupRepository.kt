package com.example.roommate.data.repository

import android.util.Log
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.userManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GroupRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerGroup(group: GroupModel) {
        db.collection("group")
            .add(group)
            .addOnSuccessListener { documentReference ->

                val userEmail = userManager.user.email.toString()
                val userRef = db.collection("user").document(userEmail)
                val userRefList = listOf(userRef)

                val updates = mapOf(
                    "users" to userRefList,
                    "id" to documentReference.id,
                    "qttMembers" to 1
                )

                db.collection("group").document(documentReference.id)
                    .update(updates)
                    .addOnSuccessListener {
                        Log.d("RegisterGroup", "Group updated with ID and user reference.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating group", e)
                    }



                val groupRef = db.collection("group").document(documentReference.id)

                val batch = db.batch()

                // Add group reference to advertisement
                val advertisementRef = db.collection("advertisement").document(group.advertisementId)
                batch.update(advertisementRef, "groups", FieldValue.arrayUnion(groupRef))

                // Add group reference to user
                batch.update(userRef, "groups", FieldValue.arrayUnion(groupRef))

                // Commit the batch
                batch.commit()
                    .addOnSuccessListener {
                        Log.d("RegisterGroup", "Group reference added to advertisement and user in a single batch.")
                    }
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating advertisement and user", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("RegisterGroup", "Error adding group", e)
            }

    }

    fun getMembersFromGroup(groupId: String, callback: (List<UserModel>) -> Unit) {
        if (groupId == "") {
            println("empty GroupId in getMembersFromGroupId")
            return
        }

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
                            val qttMembers = it.getLong("qttMembers")?.toInt() ?: 0
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
}