package com.example.roommate.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class GroupViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _groups = MutableLiveData<List<GroupModel>>()
    val groups: LiveData<List<GroupModel>> = _groups

    private val _members = MutableLiveData<List<UserModel>>()
    val members: LiveData<List<UserModel>> = _members

    fun registerGroup(group: GroupModel) {
        db.collection("group")
            .add(group)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterGroup", "Group added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("RegisterGroup", "Error adding group", e)
            }
    }

    fun getMembersFromGroup(groupId: String) {
        db.collection("group").document(groupId)
            .get()
            .addOnSuccessListener { argsGroup ->
                val usersRefs = argsGroup.get("users") as? List<DocumentReference> ?: emptyList()
                val userList = mutableListOf<UserModel>()
                var count = 0
                val qttUsers = usersRefs.size

                if (qttUsers == 0) {
                    _members.postValue(emptyList())
                } else {
                    usersRefs.forEach { userRef ->
                        userRef.get().addOnSuccessListener { document ->
                            document?.let {
                                val name = it.getString("name") ?: ""
                                val bio = it.getString("bio") ?: ""
                                val phone = it.getString("phone") ?: ""
                                userList.add(UserModel(name, bio, phone))
                                println("Usuário carregado: $name")
                            }
                        }.addOnCompleteListener {
                            count++
                            if (count == qttUsers) {
                                _members.postValue(userList)
                                println("Todos os usuários carregados.")
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                println("Erro ao buscar grupo: ${it.message}")
            }
    }


    fun fetchUserGroups(userId: String) {
        val userRef: DocumentReference = db.collection("user").document(userId)

        db.collection("group")
            .whereArrayContains("users", userRef)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val groupList = mutableListOf<GroupModel>()

                for (document in querySnapshot) {
                    val id = document.id
                    val name = document.getString("name") ?: ""
                    val description = document.getString("description") ?: ""
                    //val qttMembers = document.getLong("qttMembers")?.toInt() ?: 0

                    val qttNotifications = document.getLong("qttNotifications")?.toInt() ?: 0

                    // Get the 'users' field which is a List of DocumentReferences
                    val usersRef = document.get("users") as? List<*> ?: emptyList<DocumentReference>()

                    // Convert the List<*> to List<DocumentReference> safely
                    val userRefsList = usersRef.mapNotNull { it as? DocumentReference }
                    val qttMembers = userRefsList.size

                    val advertisementId = document.getDocumentReference("advertisementId")

                    if (advertisementId == null) {
                        println("ERROR: on fetch group")
                    } else {
                        groupList.add(
                            GroupModel(
                                id,
                                name,
                                description,
                                qttMembers,
                                qttNotifications,
                                userRefsList,
                                advertisementId
                            )
                        )
                    }
                }

                _groups.postValue(groupList)
            }
            .addOnFailureListener { e ->
                println("Error fetching groups: $e")
            }
    }
}
