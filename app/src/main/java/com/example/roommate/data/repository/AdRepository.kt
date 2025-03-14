package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.firestore.FirebaseFirestore

class AdRepository {
    private val _status = MutableLiveData<statusEnum>()
    val status: MutableLiveData<statusEnum> get() = _status

    init {
        _status.value = statusEnum.NIL
    }

    fun getAllAds(): MutableList<AdModel> {
        val db = FirebaseFirestore.getInstance()
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adList.add(
                        AdModel(
                            description = document.getString("title")!!,
                            price = document.getDouble("price")!!,
                            local = document.getString("city")!!,
                        )
                    )
                }
                _status.postValue(statusEnum.SUCCESS)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting Ad documents: ", exception)
                _status.postValue(statusEnum.SUCCESS)
            }

        return adList
    }
}