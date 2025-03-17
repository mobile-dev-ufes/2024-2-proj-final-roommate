package com.example.roommate.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.GroupModel
import com.example.roommate.data.model.UserModel
import com.example.roommate.utils.statusEnum
import com.example.roommate.utils.userManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage

/**
 * Repositório responsável por gerenciar operações relacionadas a grupos de usuários.
 *
 * A classe [GroupRepository] fornece métodos para registrar grupos, adicionar membros a um grupo,
 * salvar ativos (como fotos de grupo) no Firebase Storage, e buscar informações sobre membros e
 * grupos.
 *
 * @property db Instância do [FirebaseFirestore] usada para realizar operações de leitura e
 * gravação no Firestore.
 * @property st Instância do [FirebaseStorage] usada para manipular arquivos no Firebase Storage.
 */
class GroupRepository {
    private val db = FirebaseFirestore.getInstance()
    private var st = Firebase.storage

    /**
     * Registra um novo grupo no Firestore e realiza atualizações relacionadas aos membros do grupo
     * e aos anúncios associados.
     *
     * Após a criação do grupo, o método tenta adicionar o usuário autenticado como membro do grupo,
     * bem como atualizar os documentos de grupo, usuário e anúncio de forma atômica usando um batch.
     *
     * @param group O objeto [GroupModel] contendo as informações do grupo a ser registrado.
     * @param onSuccess Função de callback chamada quando o grupo é registrado com sucesso.
     * @param onFailure Função de callback chamada quando ocorre um erro durante o registro.
     */
    fun registerGroup(group: GroupModel, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("group")
            .add(group)  // Adiciona o grupo ao Firestore
            .addOnSuccessListener { documentReference ->
                // Atribui o ID gerado ao grupo
                group.id = documentReference.id
                // Salva os ativos (foto do grupo) no Firebase Storage
                saveAssets(group)

                val userEmail = userManager.user.email.toString()
                val userRef = db.collection("user").document(userEmail)
                val userRefList = listOf(userRef)

                val updates = mapOf(
                    "users" to userRefList,
                    "id" to documentReference.id,
                    "photoUri" to group.photoUri,
                    "qttMembers" to 1
                )

                // Atualiza o documento do grupo com a lista de usuários e o número de membros
                db.collection("group").document(documentReference.id)
                    .update(updates)
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating group", e)
                        onFailure(e)
                        return@addOnFailureListener
                    }


                // Cria uma transação em batch para atualizar o grupo nos documentos de anúncios e usuário
                val groupRef = db.collection("group").document(documentReference.id)
                val batch = db.batch()

                // Adiciona referência do grupo ao anúncio
                val advertisementRef = db.collection("advertisement").document(group.advertisementId)
                batch.update(advertisementRef, "groups", FieldValue.arrayUnion(groupRef))

                // Adiciona referência do grupo ao usuário
                batch.update(userRef, "groups", FieldValue.arrayUnion(groupRef))

                // Commita o batch
                batch.commit()
                    // Chama o callback de sucesso
                    .addOnSuccessListener {
                        Log.d("RegisterGroup", "Group reference added to advertisement and user in a single batch.")
                        onSuccess()
                    }
                    // Chama o callback de erro
                    .addOnFailureListener { e ->
                        Log.w("RegisterGroup", "Error updating advertisement and user", e)
                        onFailure(e)
                    }
            }
            // Chama o callback de erro
            .addOnFailureListener { e ->
                Log.w("RegisterGroup", "Error adding group", e)
                onFailure(e)
            }
    }

    /**
     * Salva os ativos relacionados ao grupo, como a foto do grupo, no Firebase Storage.
     *
     * Se o grupo tiver uma foto associada, o método realiza o upload para o Firebase Storage e atualiza
     * a URI da foto do grupo com o caminho gerado.
     *
     * @param group O objeto [GroupModel] contendo as informações do grupo.
     */
    private fun saveAssets(group: GroupModel){
        // Pré-pronto para implementações futuras
        val liveStatus = MutableLiveData<statusEnum>()

        // Verifica se o grupo tem uma foto associada
        if (group.photoUri == null){
            return
        }

        val storageRef = st.reference // Obtém a referência à raiz do Firebase Stora
        val file = Uri.parse(group.photoUri) // Converte a URI da foto do grupo em um objeto Uri
        val path = "groups/${group.id}/${file.lastPathSegment}" // Cria o caminho do arquivo no Storage
        val ref = storageRef.child(path) // Cria uma referência ao local específico no Storage

        // Realiza o upload da foto para o Firebase Storage
        val uploadTask = ref.putFile(file)

        uploadTask
            // Marca o upload como bem-sucedido
            .addOnSuccessListener {
                liveStatus.value = statusEnum.SUCCESS
            }
            // Marca o upload como falho
            .addOnFailureListener(){
                Log.d("FIRESTORE", "get failed with $file")
                liveStatus.value = statusEnum.FAIL_IMG
            }

        // Atualiza a URI da foto no grupo
        group.photoUri = "gs://${ref.bucket}/$path"
    }

    /**
     * Obtém os membros de um grupo com base no seu ID.
     *
     * O método consulta o grupo no Firestore e busca as referências dos usuários associados,
     * em seguida, faz a leitura dos documentos dos usuários e os adiciona a uma lista.
     * A lista final de membros é retornada via callback.
     *
     * @param groupId O ID do grupo a ser consultado.
     * @param callback Função de callback chamada com a lista de usuários quando a operação for
     * concluída.
     */
    fun getMembersFromGroup(groupId: String, callback: (List<UserModel>) -> Unit) {
        if (groupId == "") {
            println("empty GroupId in getMembersFromGroupId")
            return
        }

        val userList = mutableListOf<UserModel>()
        db.collection("group").document(groupId)
            .get()
            .addOnSuccessListener { argsGroup ->
                val usersRefs = argsGroup.get("users")

                // Converte a lista de referências para usuários
                val list = if (usersRefs is List<*>) {
                    usersRefs.filterIsInstance<DocumentReference>()
                } else {
                    emptyList()
                }

                if (list.isEmpty()) {
                    callback(userList) // Retorna a lista vazia caso não haja usuários
                    return@addOnSuccessListener
                }

                var count = 0
                list.forEach { userRef ->
                    userRef.get().addOnSuccessListener { document ->
                        document?.let {
                            val email = it.getString("email") ?: ""
                            val name = it.getString("name") ?: ""
                            val bio = it.getString("bio") ?: ""
                            val phone = it.getString("phone") ?: ""
                            userList.add(UserModel(email, name, bio, phone))
                        }
                    }.addOnCompleteListener {
                        count++
                        if (count == list.size) {
                            callback(userList) // Retorna a lista quando todos os usuários forem carregados
                        }
                    }
                }
            }.addOnFailureListener {
                println("Erro ao buscar grupo: ${it.message}")
                callback(emptyList()) // // Retorna uma lista vazia em caso de falha
            }
    }

    /**
     * Adiciona um novo membro a um grupo.
     *
     * O método atualiza o grupo, incluindo o novo membro na lista de usuários e incrementa o
     * contador de membros do grupo.
     *
     * @param groupId O ID do grupo onde o membro será adicionado.
     * @param userId O ID do usuário a ser adicionado ao grupo.
     */
    fun addGroupMember(groupId: String, userId: String) {
        val groupRef = db.collection("group").document(groupId)
        val userRef = db.collection("user").document(userId)

        groupRef.get().addOnSuccessListener { document ->
            // Verifica se a lista de usuários existe e adiciona o novo membro
            val currentUsers = document.get("users") as? MutableList<DocumentReference> ?: mutableListOf()

            // Adiciona o usuário à lista se ele não estiver presente
            if (!currentUsers.contains(userRef)) {
                currentUsers.add(userRef)
            } else {
                println("Group already has this member")
            }

            // Atualiza o contador de membros
            val currentQttMembers = document.getLong("qttMembers")?.toInt() ?: 0
            val updatedQttMembers = currentQttMembers + 1

            val updates = mapOf(
                "users" to currentUsers,
                "qttMembers" to updatedQttMembers
            )

            // Update the "users" and "qttMembers" fields in Firestore
            groupRef.update(updates)
                .addOnSuccessListener {
                    Log.d("AddGroupMember", "User $userId added to group $groupId")
                }
                .addOnFailureListener { e ->
                    Log.w("AddGroupMember", "Error adding user to group", e)
                }
        }.addOnFailureListener { e ->
            Log.w("AddGroupMember", "Error fetching group document", e)
        }
    }

    /**
     * Converte um documento Firestore para um objeto [GroupModel].
     *
     * @param document O documento Firestore a ser convertido.
     * @return O objeto [GroupModel] correspondente.
     */
    private fun DocumentSnapshot.toGroupModel(): GroupModel {
        return GroupModel(
            id = getString("id") ?: "",
            name = getString("name") ?: "",
            description = getString("description") ?: "",
            advertisementId = getString("advertisementId") ?: "",
            qttMembers = getLong("qttMembers")?.toInt() ?: 0,
            isPrivate = getBoolean("isPrivate") ?: false,
            photoUri = getString("photoUri") ?: ""
        )
    }

    /**
     * Recupera a imagem de um grupo a partir do Firebase Firestore.
     *
     * Esta função recupera o URI da imagem associada a um grupo, buscando o campo `photoUri`
     * dentro do documento do grupo no Firestore. Após obter o URI da imagem, ela chama a função
     * `getStorageUri` para obter a URL do arquivo armazenado no Firebase Storage.
     *
     * @param groupId O ID do grupo, utilizado para buscar o documento correspondente na coleção
     * "group" do Firestore.
     * @param onSuccess Função de retorno que será chamada quando a URL da imagem for obtida com
     * sucesso. Recebe a URL da imagem como parâmetro (String).
     * @param onFailure Função de retorno que será chamada caso ocorra algum erro. Recebe uma
     * exceção como parâmetro.
     */
    fun getGroupImage(
        groupId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Busca o documento do grupo pelo ID fornecido
        db.collection("group").document(groupId)
            .get()
            .addOnSuccessListener { document ->
                // Recupera o campo "photoUri" do documento
                val photoUri = document.getString("photoUri").toString()

                // Chama a função para obter a URL da imagem no Storage
                getStorageUri(photoUri, onSuccess, onFailure)
            }
            .addOnFailureListener { exception ->
                // Se houver falha ao buscar o documento, chama a função de falha com a exceção
                onFailure(exception)
            }
    }

    /**
     * Recupera a URL de download de um arquivo armazenado no Firebase Storage, dado o seu URI.
     *
     * Esta função utiliza o URI fornecido (referente à foto) para buscar o arquivo no Firebase
     * Storage e obter a URL de download pública. A URL pode ser usada para exibir a imagem ou
     * realizar outras operações com o arquivo.
     *
     * @param photoUri O URI do arquivo no Firebase Storage.
     * @param onSuccess Função de retorno chamada quando a URL de download é obtida com sucesso.
     * Recebe a URL como parâmetro (String).
     * @param onFailure Função de retorno chamada caso ocorra algum erro durante o processo.
     * Recebe uma exceção como parâmetro.
     */
    private fun getStorageUri(
        photoUri: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (photoUri.isNotEmpty()) {
            // Cria uma referência ao arquivo no Storage utilizando o URI fornecido
            val storageRef = st.getReferenceFromUrl(photoUri)

            // Obtém a URL de download do arquivo
            storageRef.downloadUrl
                // Chama o callback de sucesso passando a URL do arquivo
                .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
                // Chama o callback de falha caso ocorra algum erro
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            // Se o URI estiver vazio, chama o callback de falha com uma exceção
            onFailure(Exception("Photo URI not found"))
        }
    }
}