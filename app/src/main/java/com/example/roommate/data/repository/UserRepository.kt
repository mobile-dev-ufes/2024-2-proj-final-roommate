package com.example.roommate.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.io.File
import java.time.ZoneId
import java.util.Date

/**
 * Repository responsible for managing user data in Firebase Firestore and Firebase Storage.
 *
 * This repository contains methods for creating and retrieving user data, as well as saving the user's image files
 * in Firebase Storage.
 */
class UserRepository {
    // Firestore database instance
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    // Firebase Storage instance
    private var st = Firebase.storage

    /**
     * Creates a new user in Firestore and saves their data.
     *
     * This method sends the user's information to Firestore and then uploads the user's photo
     * to Firebase Storage, if a photo is provided.
     *
     * @param user The user model containing the information to be saved.
     * @param status LiveData for indicating the success or failure of the operation.
     */
    fun create(user: UserModel, status : MutableLiveData<statusEnum>){
        saveAssets(user, status)

        val birthDate = Date.from(user.birthDate!!.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val userMap = hashMapOf(
            "email" to user.email,
            "name" to user.name,
            "bio" to user.bio,
            "sex" to user.sex,
            "phone" to user.phone,
            "birthDate" to Timestamp(birthDate),
            "photo_uri" to user.photo_uri
        )

        db.collection("user").document(user.email!!)
            .set(userMap)
            .addOnSuccessListener {
                status.value = statusEnum.SUCCESS
            }
            .addOnFailureListener {
                status.value = statusEnum.FAIL
            }
    }

    /**
     * Saves the user's photo in Firebase Storage.
     *
     * If the user's photo is provided, this method uploads it to Firebase Storage. Otherwise,
     * the status is marked as success without the need for upload.
     *
     * @param user The user model containing the photo to be saved.
     * @param liveStatus LiveData that receives the status of the operation (success or failure).
     */
    private fun saveAssets(user: UserModel, liveStatus: MutableLiveData<statusEnum>){
        if (user.photo_uri == null){
            liveStatus.value = statusEnum.SUCCESS
            return
        }

        // Create a storage reference from our app
        val storageRef = st.reference

        val userId = user.email!!.replace(Regex("[^A-Za-z]"), "")

        val file = Uri.parse(user.photo_uri!!)
        val path = "users/$userId/${file.lastPathSegment}"
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

        user.photo_uri = "gs://${ref.bucket}/$path"
    }

    /**
     * Retrieves user data from Firestore.
     *
     * This method fetches a user's information based on the provided email. If the user exists,
     * their data is assigned to a `UserModel` object and returned via LiveData.
     *
     * @param userEmail The email of the user to be fetched from Firestore.
     * @param liveStatus LiveData that receives the status of the operation (success or failure).
     * @param liveData LiveData that receives the `UserModel` object with the user's data.
     */
    fun get(userEmail: String, liveStatus: MutableLiveData<statusEnum>, liveData: MutableLiveData<UserModel>) {
        var user = UserModel()

        db.collection("user").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    user = UserModel(
                        document.getString("email"),
                        document.getString("name"),
                        document.getString("bio"),
                        document.getString("sex"),
                        document.getString("phone"),
                        document.getTimestamp("birthDate")!!.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        document.getString("photo_uri")
                    )
                    Log.d("FIREBASE", "DocumentSnapshot data: ${document.data}")

                    liveData.value = user
                    liveStatus.value = statusEnum.SUCCESS
                } else {
                    Log.d("FIREBASE", "No such document")
                    liveStatus.value = statusEnum.FAIL
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "get failed with ", exception)
                liveStatus.value = statusEnum.FAIL
            }
    }

    fun getGroupsForUser(userId: String, callback: (List<GroupModel>) -> Unit) {
        val userRef = db.collection("user").document(userId)

        userRef.get().addOnSuccessListener { document ->
            val groupRefs = document.get("groups") as? List<DocumentReference> ?: return@addOnSuccessListener

            val groupList = mutableListOf<GroupModel>()
            var count = 0

            groupRefs.forEach { groupRef ->
                groupRef.get().addOnSuccessListener { groupDoc ->
                    groupDoc?.let {
                        val id = it.getString("id") ?: ""
                        val name = it.getString("name") ?: ""
                        val description = it.getString("description") ?: ""
                        val advertisementId = it.getString("advertisementId") ?: ""
                        val qttMembers = it.getLong("qttMembers")?.toInt() ?: 0
                        val isPrivate = it.getBoolean("isPrivate") ?: false
                        val photoUrl = it.getString("photoUrl") ?: ""

                        groupList.add(GroupModel(id, name, description, advertisementId, qttMembers, isPrivate, photoUrl))
                    }
                }.addOnCompleteListener {
                    count++
                    if (count == groupRefs.size) {
                        callback(groupList) // Call the callback with the list once all groups are fetched
                    }
                }
            }
        }.addOnFailureListener { e ->
            Log.w("GetGroups", "Error getting user document", e)
            callback(emptyList()) // In case of error, return an empty list
        }
    }


    fun addGroupToUser(userId: String, groupId: String) {
        val userRef = db.collection("user").document(userId)
        val groupRef = db.collection("group").document(groupId)

        // Get the current list of groups for the user
        userRef.get().addOnSuccessListener { document ->
            val currentGroups = document.get("groups") as? MutableList<DocumentReference> ?: mutableListOf()

            // Add the new group reference to the list if it's not already present
            if (!currentGroups.contains(groupRef)) {
                currentGroups.add(groupRef)
            } else {
                println("User already a member of the this group")
            }

            // Update the user's list of groups
            userRef.update("groups", currentGroups)
                .addOnSuccessListener {
                    Log.d("AddGroupToUser", "Group $groupId added to user $userId")
                }
                .addOnFailureListener { e ->
                    Log.w("AddGroupToUser", "Error adding group to user", e)
                }
        }.addOnFailureListener { e ->
            Log.w("AddGroupToUser", "Error fetching user document", e)
        }
    }

    fun getProfileImage(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val photoUri = document.getString("photo_uri").toString()
                getStorageUri(photoUri, onSuccess, onFailure)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private fun getStorageUri(
        photoUri: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (photoUri.isNotEmpty()) {
            val storageRef = storage.getReferenceFromUrl(photoUri)
            storageRef.downloadUrl
                .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            onFailure(Exception("Photo URI not found"))
        }
    }

}