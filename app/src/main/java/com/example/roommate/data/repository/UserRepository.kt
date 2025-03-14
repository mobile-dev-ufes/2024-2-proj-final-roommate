package com.example.roommate.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId
import java.util.Date

class UserRepository {
    private val _status = MutableLiveData<statusEnum>()
    val status: MutableLiveData<statusEnum> get() = _status

    fun create(user: UserModel){
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
            .addOnFailureListener{
                _status.postValue(statusEnum.FAIL)
            }
    }
}