package com.example.roommate.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class UserRepository {
    private val _status = MutableLiveData<statusEnum>()
    val status: MutableLiveData<statusEnum> get() = _status

    init {
        _status.value = statusEnum.NIL
    }

    fun create(user: UserModel) {
        _status.value = statusEnum.NIL

        val birthDate = Date.from(user.birthDate!!.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val db = FirebaseFirestore.getInstance()

        val userMap = hashMapOf(
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
                _status.postValue(statusEnum.SUCCESS)
            }
            .addOnFailureListener {
                _status.postValue(statusEnum.FAIL)
            }
    }

    fun get(userEmail: String) : UserModel {
        _status.value = statusEnum.NIL

        val db = FirebaseFirestore.getInstance()
        var user = UserModel()

        db.collection("user").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
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
                } else {
                    Log.d("FIREBASE", "No such document")

                }
                _status.postValue(statusEnum.SUCCESS)
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "get failed with ", exception)
                _status.postValue(statusEnum.FAIL)
            }

        return user
    }
}