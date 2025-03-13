package com.example.roommate.data.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val _status = MutableLiveData<statusEnum>()
    val status: MutableLiveData<statusEnum> get() = _status

    fun create(user: UserModel){
        val db = FirebaseFirestore.getInstance()

        val userMap = hashMapOf(
            "name" to user.name,
            "bio" to user.bio,
            "age" to user.age,
            "sex" to user.sex,
            "phone" to user.phone,
            "birthDate" to user.birthDate,
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