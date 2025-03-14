package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.firestore.FirebaseFirestore

class AdRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getAllAds(): MutableLiveData<MutableList<AdModel>> {
        val adList = mutableListOf<AdModel>()
        val liveData = MutableLiveData<MutableList<AdModel>>()

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
                liveData.value = adList
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting Ad documents: ", exception)
            }

        return liveData
    }
}