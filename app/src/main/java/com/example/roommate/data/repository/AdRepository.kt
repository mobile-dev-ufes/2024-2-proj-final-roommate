package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.example.roommate.utils.userManager
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage

class AdRepository {
    private val db = FirebaseFirestore.getInstance()
    private var st = Firebase.storage

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

                ad.id = adRef.id

                // Update the document with its generated ID
                adRef.update("id", adRef.id)
                    .addOnSuccessListener {
                        Log.d("FIREBASE-ADS", "ID atualizado com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIREBASE-ADS", "Erro ao atualizar ID", e)
                    }

                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${documentReference}")
                saveAssets(ad, status)
            }
            .addOnFailureListener {
                Log.d("FIREBASE-ADS", "Erro ao adicionar o documento")
                status.value = statusEnum.FAIL
            }
    }

    private fun saveAssets(ad: AdModel, liveStatus: MutableLiveData<statusEnum>){
        if (ad.photos.isEmpty()){
            liveStatus.value = statusEnum.SUCCESS
            return
        }

        // Create a storage reference from our app
        val storageRef = st.reference
        val userId = userManager.user.email!!.replace(Regex("[^A-Za-z]"), "")

        for (photo in ad.photos){
            val file = Uri.parse(photo)
            val ref = storageRef.child("ads/${ad.id}/${file.lastPathSegment}")

            val uploadTask = ref.putFile(file)

            uploadTask
                .addOnSuccessListener {
                    liveStatus.value = statusEnum.SUCCESS
                    Log.d("FIRE - STORAGE", "Upload bem-sucedido: $file")
                }
                .addOnFailureListener { exception ->
                    liveStatus.value = statusEnum.FAIL_IMG
                    Log.d("FIRE - STORAGE", "Falha no upload: $file", exception)
                }
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