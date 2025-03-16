package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.utils.statusEnum
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class AdRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getAllAds(liveData: MutableLiveData<MutableList<AdModel>>) {
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adList.add(AdModel(document))
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
                    adList.add(AdModel(document))
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
        val adRef = db.collection("advertisement").document()
        val adMap = ad.toMap()

        adRef.set(adMap)
            .addOnSuccessListener { documentReference ->
                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${adRef.id}")

                // Update the document with its generated ID
                adRef.update("id", adRef.id)
                    .addOnSuccessListener {
                        Log.d("FIREBASE-ADS", "ID atualizado com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIREBASE-ADS", "Erro ao atualizar ID", e)
                    }

                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${documentReference}")
                status.value = statusEnum.SUCCESS
            }
            .addOnFailureListener {
                Log.d("FIREBASE-ADS", "Erro ao adicionar o documento")
                status.value = statusEnum.FAIL
            }
    }

    // Substituída pelo construtor definido na classe
    private fun adModelFromDocument(document: QueryDocumentSnapshot): AdModel{
        var benefits = document.get("benefits")
        var local = document.get("local")

        benefits = if (benefits is Map<*, *>){
            benefits as? Map<String, Boolean>
        } else {
            null
        }

        local = if(local is Map<*, *>) {
            Address(local as Map<Any, Any>)
        } else {
            Address()
        }

        return AdModel(
            id = document.getString("id"),
            owner = document.getString("owner"),
            title = document.getString("title"),
            rent_value = document.getDouble("rent_value") ?: 0.0,
            cond_value = document.getDouble("cond_value"),
            max_client = document.getLong("max_client"),
            description = document.getString("description"),
            suite_qtt = document.getLong("suite_qtt"),
            bedroom_qtt = document.getLong("bedroom_qtt"),
            parking_qtt = document.getLong("parking_qtt"),
            area = document.getDouble("area"),
            benefits = benefits,
            local = local,
            groups = arrayOf()
        )
    }
}