package com.example.roommate.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId
import java.util.Date

class UserRepository {
    private val db = FirebaseFirestore.getInstance()

    fun create(user: UserModel, status : MutableLiveData<statusEnum>){
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
}