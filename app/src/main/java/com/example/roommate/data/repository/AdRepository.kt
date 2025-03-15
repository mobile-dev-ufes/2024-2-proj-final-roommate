package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.utils.statusEnum
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.io.Serializable

class AdRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getAllAds(liveData: MutableLiveData<MutableList<AdModel>>) {
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adList.add(adModelFromDocument(document))
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
                    adList.add(adModelFromDocument(document))
                    Log.d("FIREBASE-MYADS", "${document.id} => ${document.data}")
                }
                Log.d("TESTE", "hello")
                liveData.value = adList
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE-MYADS", "Error getting Ad documents: ", exception)
            }
    }

    fun create(ad: AdModel,  status : MutableLiveData<statusEnum>){
        val adMap = ad.toMap()

        db.collection("advertisement").document()
            .set(adMap)
            .addOnSuccessListener { documentReference ->
                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${documentReference}")
                status.value = statusEnum.SUCCESS
            }
            .addOnFailureListener {
                Log.d("FIREBASE-ADS", "Erro ao adicionar o documento")
                status.value = statusEnum.FAIL
            }
    }

    private fun adModelFromDocument(document: QueryDocumentSnapshot): AdModel{
        var benefits = document.get("benefits")
        var local = document.get("local")

        benefits = if (benefits is Map<*, *>){
            (benefits as? Map<String, Boolean>)
        } else {
            null
        }

        local = if(local is Map<*, *>) {
            Address(local as Map<Any, Any>)
        } else {
            Address("unknown", null, "unknown", "unknown", "unknown", "unknown")
        }

        return AdModel(
            title = document.getString("title"),
            rent_value = document.getDouble("rent_value") ?: 0.0,
            cond_value = document.getDouble("cond_value"),
            max_client = document.getLong("max_client")?.toInt(),
            description = document.getString("description"),
            suite_qtt = document.getLong("suiteQtt")?.toInt(),
            bedroom_qtt = document.getLong("bedroomQtt")?.toInt(),
            area = document.getDouble("area"),
            benefits = benefits,
            local = local,
            groups = arrayOf()
        )
    }
}