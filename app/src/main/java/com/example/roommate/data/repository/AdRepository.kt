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

    fun getAllAds(liveData: MutableLiveData<MutableList<AdModel>>) {
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
                liveData.value = adList
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting Ad documents: ", exception)
            }
    }

    fun getAdsByUser(userId: String, liveData: MutableLiveData<MutableList<AdModel>>) {
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .whereEqualTo("owner", userId)
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
                    Log.d("FIREBASE-MYADS", "${document.id} => ${document.data}")
                }
                Log.d("TESTE", "hello")
                liveData.value = adList
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE-MYADS", "Error getting Ad documents: ", exception)
            }
    }
}