package com.example.roommate.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.example.roommate.utils.userManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

class GroupRepository {
    private val db = FirebaseFirestore.getInstance()
    private var st = Firebase.storage

    fun registerGroup(group: GroupModel) {
        db.collection("group")
            .add(group)
            .addOnSuccessListener { documentReference ->
                group.id = documentReference.id
                saveAssets(group)

                val userEmail = userManager.user.email.toString()
                val userRef = db.collection("user").document(userEmail)
                val userRefList = listOf(userRef)

                val updates = mapOf(
                    "users" to userRefList,
                    "id" to documentReference.id,
                    "photoUri" to group.photoUri,
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

    private fun saveAssets(group: GroupModel){
        val liveStatus = MutableLiveData<statusEnum>()

        if (group.photoUri == null){
            return
        }

        // Create a storage reference from our app
        val storageRef = st.reference
        val file = Uri.parse(group.photoUri)
        val path = "groups/${group.id}/${file.lastPathSegment}"
        val ref = storageRef.child(path)

        val uploadTask = ref.putFile(file)

        uploadTask
            .addOnSuccessListener {
                liveStatus.value = statusEnum.SUCCESS
            }
            .addOnFailureListener(){
                Log.d("FIRESTORE", "get failed with $file")
                liveStatus.value = statusEnum.FAIL_IMG
            }

        group.photoUri = "gs://${ref.bucket}/$path"
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
                            val email = it.getString("email") ?: ""
                            val name = it.getString("name") ?: ""
                            val bio = it.getString("bio") ?: ""
                            val phone = it.getString("phone") ?: ""
                            userList.add(UserModel(email, name, bio, phone))
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

    fun addGroupMember(groupId: String, userId: String) {
        val groupRef = db.collection("group").document(groupId)
        val userRef = db.collection("user").document(userId)

        groupRef.get().addOnSuccessListener { document ->
            // Check if the "users" field exists and is a List
            val currentUsers = document.get("users") as? MutableList<DocumentReference> ?: mutableListOf()

            // Add the new user to the list if it's not already present
            if (!currentUsers.contains(userRef)) {
                currentUsers.add(userRef)
            } else {
                println("Group already has this member")
            }

            // Retrieve the current "qttMembers" value and increment it by 1
            val currentQttMembers = document.getLong("qttMembers")?.toInt() ?: 0
            val updatedQttMembers = currentQttMembers + 1

            val updates = mapOf(
                "users" to currentUsers,
                "qttMembers" to updatedQttMembers
            )

            // Update the "users" and "qttMembers" fields in Firestore
            groupRef.update(updates)
                .addOnSuccessListener {
                    Log.d("AddGroupMember", "User $userId added to group $groupId")
                }
                .addOnFailureListener { e ->
                    Log.w("AddGroupMember", "Error adding user to group", e)
                }
        }.addOnFailureListener { e ->
            Log.w("AddGroupMember", "Error fetching group document", e)
        }
    }

    private fun DocumentSnapshot.toGroupModel(): GroupModel {
        return GroupModel(
            id = getString("id") ?: "",
            name = getString("name") ?: "",
            description = getString("description") ?: "",
            advertisementId = getString("advertisementId") ?: "",
            qttMembers = getLong("qttMembers")?.toInt() ?: 0,
            isPrivate = getBoolean("isPrivate") ?: false,
            photoUri = getString("photoUri") ?: ""
        )
    }
}