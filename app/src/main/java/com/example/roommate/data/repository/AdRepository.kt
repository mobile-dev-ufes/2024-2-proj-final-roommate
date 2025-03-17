package com.example.roommate.data.repository

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.model.Address
import com.example.roommate.utils.statusEnum
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.storage

/**
 * Classe responsável por interagir com o banco de dados Firebase para realizar operações
 * relacionadas aos anúncios.
 *
 * A classe [AdRepository] fornece métodos para realizar as seguintes operações:
 * - Obter todos os anúncios ou anúncios específicos de um usuário.
 * - Criar um novo anúncio.
 * - Salvar fotos associadas ao anúncio.
 * - Obter imagens de anúncios armazenadas no Firebase Storage.
 *
 * @property db Instância do Firestore para interagir com o banco de dados.
 * @property st Instância do FirebaseStorage para interagir com o armazenamento de fotos.
 */
class AdRepository {
    private val db = FirebaseFirestore.getInstance()
    private var st = Firebase.storage

    /**
     * Recupera todos os anúncios armazenados no Firestore e os retorna através do [liveData].
     *
     * @param liveData O LiveData que será atualizado com a lista de anúncios recuperados.
     */
    fun getAllAds(liveData: MutableLiveData<MutableList<AdModel>>) {
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adList.add(AdModel(document))  // Adiciona cada anúncio à lista
                }
                liveData.value = adList // Atualiza o LiveData com a lista de anúncios
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE-ADS", "Error getting Ad documents: ", exception)
            }
    }

    /**
     * Recupera os anúncios de um usuário específico baseado no ID do usuário.
     *
     * @param userId O ID do usuário cujos anúncios devem ser recuperados.
     * @param liveData O LiveData que será atualizado com a lista de anúncios do usuário.
     */
    fun getAdsByUser(userId: String, liveData: MutableLiveData<MutableList<AdModel>>) {
        val adList = mutableListOf<AdModel>()

        db.collection("advertisement")
            .whereEqualTo("owner", userId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adList.add(AdModel(document)) // Adiciona os anúncios do usuário à lista
                    Log.d("FIREBASE-MYADS", "${document.id} => ${document.data}")
                }

                // Atualiza o LiveData com os anúncios do usuário
                liveData.value = adList
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE-MYADS", "Error getting Ad documents: ", exception)
            }
    }

    /**
     * Cria um novo anúncio no Firestore e armazena as fotos associadas ao anúncio no
     * Firebase Storage.
     * Atualiza o status da operação por meio de [status].
     *
     * @param ad O modelo do anúncio a ser criado.
     * @param status O LiveData que será atualizado com o status da operação (sucesso ou falha).
     */
    fun create(ad: AdModel,  status : MutableLiveData<statusEnum>){
        // Cria uma referência para um novo documento
        val adRef = db.collection("advertisement").document()

        ad.id = adRef.id // Atribui o ID gerado pelo Firestore ao anúncio
        saveAssets(ad, status) // Salva as fotos do anúncio no Firebase Storage

        val adMap = ad.toMap() // Converte o anúncio para um mapa de dados

        // Cria o documento no Firestore
        adRef.set(adMap)
            .addOnSuccessListener { documentReference ->
                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${adRef.id}")

                // Atualiza o documento com seu ID gerado
                adRef.update("id", adRef.id)
                    .addOnSuccessListener {
                        Log.d("FIREBASE-ADS", "ID atualizado com sucesso")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIREBASE-ADS", "Erro ao atualizar ID", e)
                    }

                Log.d("FIREBASE-ADS", "Documento adicionado com ID: ${documentReference}")
            }
            .addOnFailureListener {
                Log.d("FIREBASE-ADS", "Erro ao adicionar o documento")
                status.value = statusEnum.FAIL
            }
    }

    /**
     * Salva as fotos associadas ao anúncio no Firebase Storage.
     *
     * @param ad O modelo de anúncio contendo as fotos.
     * @param liveStatus O LiveData que será atualizado com o status da operação de upload das fotos.
     */
    private fun saveAssets(ad: AdModel, liveStatus: MutableLiveData<statusEnum>){
        // Retorna caso não haja fotos
        if (ad.photos.isEmpty()){
            liveStatus.value = statusEnum.SUCCESS
            return
        }


        val storageRef = st.reference  // Referência ao Firebase Storage
        val photos: MutableList<String> = mutableListOf()

        for (photo in ad.photos){
            val file = Uri.parse(photo) // Converte o caminho da foto para um URI
            val path = "ads/${ad.id}/${file.lastPathSegment}" // Define o caminho no Firebase Storage
            val ref = storageRef.child(path) // Cria uma referência para o arquivo

            val uploadTask = ref.putFile(file) // Realiza o upload da foto

            photos.add("gs://${ref.bucket}/$path") // Adiciona o caminho no storage à lista de fotos

            uploadTask
                // Caso o upload seja bem-sucedido, o status é sucesso
                .addOnSuccessListener {
                    liveStatus.value = statusEnum.SUCCESS
                    Log.d("FIRE - STORAGE", "Upload bem-sucedido: $file")
                }
                // Caso falhe, o status é de falha no upload da imagem
                .addOnFailureListener { exception ->
                    liveStatus.value = statusEnum.FAIL_IMG
                    Log.d("FIRE - STORAGE", "Falha no upload: $file", exception)
                }
        }

        // Atualiza o modelo de anúncio com os caminhos das fotos no Firebase Storage
        ad.photos = photos
    }

    /**
     * Recupera a imagem de um anúncio armazenado no Firebase Storage.
     *
     * @param adId O ID do anúncio.
     * @param onSuccess Função de callback chamada quando a URL da imagem é obtida com sucesso.
     * @param onFailure Função de callback chamada quando ocorre um erro ao obter a URL da imagem.
     */
    fun getAdImage(
        adId: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("advertisement").document(adId)
            .get()
            .addOnSuccessListener { document ->
                val photosList = document.get("photos") as? List<String> ?: emptyList()

                if (photosList.isNotEmpty()) {
                    val firstPhotoUri = photosList.first() // Pega a primeira foto do anúncio
                    getStorageUri(firstPhotoUri, onSuccess, onFailure)  // Recupera a URL da foto no Firebase Storage
                } else {
                    onFailure(Exception("No photos found")) // Caso não haja fotos, chama o callback de erro
                }
            }
            // Caso falhe ao recuperar os dados do documento, chama o callback de erro
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Recupera a URL de um arquivo de imagem no Firebase Storage a partir de seu URI.
     *
     * @param photoUri O URI da foto no Firebase Storage.
     * @param onSuccess Função de callback chamada quando a URL do arquivo é obtida com sucesso.
     * @param onFailure Função de callback chamada quando ocorre um erro ao obter a URL.
     */
    private fun getStorageUri(
        photoUri: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (photoUri.isNotEmpty()) {
            val storageRef = st.getReferenceFromUrl(photoUri)
            storageRef.downloadUrl
                // Retorna a URL da foto
                .addOnSuccessListener { uri -> onSuccess(uri.toString()) }
                // Chama o callback de erro caso falhe
                .addOnFailureListener { exception -> onFailure(exception) }
        } else {
            // Caso o URI da foto não seja válido
            onFailure(Exception("Photo URI not found"))
        }
    }
}