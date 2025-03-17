package com.example.roommate.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.io.File
import java.time.ZoneId
import java.util.Date

/**
 * Repositório responsável por gerenciar os dados do usuário no Firebase Firestore e Firebase
 * Storage.
 *
 * Esta classe contém métodos para criar e recuperar dados de usuários, bem como salvar arquivos
 * de imagem do usuário no Firebase Storage.
 *
 * Além disso, ela implementa operações como associar grupos ao usuário, obter informações sobre
 * os grupos do usuário, e fazer upload de fotos de perfil no Firebase Storage.
 *
 * @constructor Cria uma instância do repositório de usuário, utilizando o Firestore e Firebase
 * Storage.
 */
class UserRepository {
    // Instância do Firestore
    private val db = FirebaseFirestore.getInstance()
    // Instância do Firebase Storage
    private var st = Firebase.storage

    /**
     * Cria um novo usuário no Firestore e salva seus dados.
     *
     * Este método envia as informações do usuário para o Firestore e, em seguida, faz o upload
     * da foto do usuário para o Firebase Storage, caso uma foto seja fornecida.
     *
     * @param user O modelo de usuário contendo as informações a serem salvas.
     * @param status LiveData que indica o sucesso ou falha da operação.
     */
    fun create(user: UserModel, status : MutableLiveData<statusEnum>){
        saveAssets(user, status)

        val birthDate = Date.from(user.birthDate!!.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val userMap = hashMapOf(
            "email" to user.email,
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
                status.value = statusEnum.SUCCESS
            }
            .addOnFailureListener {
                status.value = statusEnum.FAIL
            }
    }

    /**
     * Salva a foto do usuário no Firebase Storage.
     *
     * Caso a foto do usuário seja fornecida, este método faz o upload da foto para o Firebase Storage.
     * Caso contrário, o status é marcado como sucesso sem necessidade de upload.
     *
     * @param user O modelo de usuário contendo a foto a ser salva.
     * @param liveStatus LiveData que recebe o status da operação (sucesso ou falha).
     */
    private fun saveAssets(user: UserModel, liveStatus: MutableLiveData<statusEnum>){
        // Retorna caso não haja foto
        if (user.photo_uri == null){
            liveStatus.value = statusEnum.SUCCESS
            return
        }

        val storageRef = st.reference

        // Prepara o caminho do arquivo no Storage
        val userId = user.email!!.replace(Regex("[^A-Za-z]"), "")
        val file = Uri.parse(user.photo_uri!!)
        val path = "users/$userId/${file.lastPathSegment}"
        val ref = storageRef.child(path)

        // Inicia o upload do arquivo para o Firebase Storage.
        val uploadTask = ref.putFile(file)

        uploadTask
            // Se o upload for bem-sucedido, marca o status como sucesso.
            .addOnSuccessListener {
                liveStatus.value = statusEnum.SUCCESS
            }
            // Se o upload falhar, marca o status como falha.
            .addOnFailureListener(){
                Log.d("FIRESTORE", "get failed with $file")
                liveStatus.value = statusEnum.FAIL_IMG
            }

        user.photo_uri = "gs://${ref.bucket}/$path"
    }

    /**
     * Recupera os dados de um usuário a partir do Firestore.
     *
     * Este método busca os dados de um usuário a partir do e-mail fornecido. Se o usuário existir,
     * as informações são extraídas e retornadas através de um objeto `UserModel`, que é passado para
     * o `LiveData` fornecido. Se o documento não for encontrado ou ocorrer um erro, um status de falha é retornado.
     *
     * @param userEmail O e-mail do usuário a ser recuperado.
     * @param liveStatus LiveData que recebe o status da operação (sucesso ou falha).
     * @param liveData LiveData que recebe o objeto `UserModel` contendo os dados do usuário.
     */
    fun get(userEmail: String, liveStatus: MutableLiveData<statusEnum>, liveData: MutableLiveData<UserModel>) {
        var user: UserModel

        // Realiza a busca no Firestore.
        db.collection("user").document(userEmail)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Cria um objeto UserModel com os dados obtidos do Firestore.
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

                    // Salva os dados e status no LiveData.
                    liveData.value = user
                    liveStatus.value = statusEnum.SUCCESS

                // Se o documento não for encontrado, marca a operação como falha.
                } else {
                    Log.d("FIREBASE", "No such document")
                    liveStatus.value = statusEnum.FAIL
                }
            }
            // Em caso de erro, marca a operação como falha.
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "get failed with ", exception)
                liveStatus.value = statusEnum.FAIL
            }
    }

    /**
     * Recupera os grupos aos quais um usuário pertence.
     *
     * Este método busca todos os grupos aos quais o usuário está associado. Ele coleta as
     * referências dos grupos a partir do documento do usuário e, para cada grupo, recupera as
     * informações detalhadas de cada grupo (nome, descrição, etc.) após o que retorna uma lista
     * com os grupos do usuário. Um callback é chamado com essa lista de grupos quando a operação
     * é concluída.
     *
     * @param userId O ID do usuário para o qual os grupos serão recuperados.
     * @param callback Função de retorno que será chamada com a lista de grupos após a recuperação.
     */
    fun getGroupsForUser(userId: String, callback: (List<GroupModel>) -> Unit) {
        val userRef = db.collection("user").document(userId)

        userRef.get().addOnSuccessListener { document ->
            val groupRefs = document.get("groups") as? List<DocumentReference> ?: return@addOnSuccessListener

            val groupList = mutableListOf<GroupModel>()
            var count = 0

            // Recupera os detalhes de cada grupo associado ao usuário.
            groupRefs.forEach { groupRef ->
                groupRef.get().addOnSuccessListener { groupDoc ->
                    groupDoc?.let {
                        val id = it.getString("id") ?: ""
                        val name = it.getString("name") ?: ""
                        val description = it.getString("description") ?: ""
                        val advertisementId = it.getString("advertisementId") ?: ""
                        val qttMembers = it.getLong("qttMembers")?.toInt() ?: 0
                        val isPrivate = it.getBoolean("isPrivate") ?: false
                        val photoUrl = it.getString("photoUrl") ?: ""

                        groupList.add(GroupModel(id, name, description, advertisementId, qttMembers, isPrivate, photoUrl))
                    }
                }.addOnCompleteListener {
                    count++
                    if (count == groupRefs.size) {
                        callback(groupList) // Chama o callback quando todos os grupos forem recuperados.
                    }
                }
            }
        }.addOnFailureListener { e ->
            Log.w("GetGroups", "Error getting user document", e)
            callback(emptyList()) // Em caso de erro, retorna uma lista vazia
        }
    }

    /**
     * Adiciona um grupo à lista de grupos de um usuário.
     *
     * Este método recupera a lista de grupos associada ao usuário e adiciona a referência de
     * um novo grupo.
     * Se o grupo já estiver na lista do usuário, a operação é ignorada. Se o grupo não estiver
     * presente, ele é adicionado à lista e a lista é atualizada no Firestore.
     *
     * @param userId O ID do usuário.
     * @param groupId O ID do grupo a ser adicionado.
     */
    fun addGroupToUser(userId: String, groupId: String) {
        val userRef = db.collection("user").document(userId)
        val groupRef = db.collection("group").document(groupId)

        // Recupera a lista atual de grupos do usuário.
        userRef.get().addOnSuccessListener { document ->
            val currentGroups = document.get("groups") as? MutableList<DocumentReference> ?: mutableListOf()

            // Se o grupo não estiver na lista, adiciona o grupo.
            if (!currentGroups.contains(groupRef)) {
                currentGroups.add(groupRef)
            } else {
                println("User already a member of the this group")
            }

            // Atualiza a lista de grupos no Firestore.
            userRef.update("groups", currentGroups)
                .addOnSuccessListener {
                    Log.d("AddGroupToUser", "Group $groupId added to user $userId")
                }
                .addOnFailureListener { e ->
                    Log.w("AddGroupToUser", "Error adding group to user", e)
                }
        }.addOnFailureListener { e ->
            Log.w("AddGroupToUser", "Error fetching user document", e)
        }
    }

    /**
     * Recupera a imagem de perfil de um usuário.
     *
     * Este método busca a URI da foto de perfil de um usuário armazenada no Firestore. Em seguida,
     * utiliza o Firebase Storage para obter a URL pública do arquivo, que pode ser usada para exibir
     * a imagem do perfil em interfaces de usuário.
     *
     * @param userId O ID do usuário.
     * @param onSuccess Função de retorno chamada quando a URL da imagem de perfil é obtida com
     * sucesso.
     * @param onFailure Função de retorno chamada caso ocorra algum erro ao tentar obter a URL.
     */
    fun getProfileImage(
        userId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("user").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val photoUri = document.getString("photo_uri").toString()
                getStorageUri(photoUri, onSuccess, onFailure)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Recupera a URL de download de um arquivo armazenado no Firebase Storage.
     *
     * Este método utiliza o URI da foto de perfil armazenada no Firebase Storage e retorna a URL
     * pública do arquivo, para que possa ser utilizada para exibir a imagem ou outras operações.
     *
     * @param photoUri O URI do arquivo no Firebase Storage.
     * @param onSuccess Função de retorno chamada quando a URL de download é obtida com sucesso.
     * @param onFailure Função de retorno chamada caso ocorra algum erro durante o processo.
     */
    private fun getStorageUri(
        photoUri: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (photoUri.isNotEmpty()) {
            val storageRef = st.getReferenceFromUrl(photoUri)
            // Tenta obter a URL de download do arquivo armazenado
            storageRef.downloadUrl
                // Em caso de sucesso, chama o callback onSuccess
                .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
                // Em caso de falha, chama o callback onFailure com a exceção
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            // Chama o callback onFailure com a exceção
            onFailure(Exception("Photo URI not found"))
        }
    }

}