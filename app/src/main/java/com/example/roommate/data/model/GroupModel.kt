package com.example.roommate.data.model

import java.io.Serializable

/**
 * Modelo de um grupo dentro do aplicativo.
 *
 * A classe [GroupModel] representa um grupo de usuários dentro do sistema. Um grupo é composto por
 * um conjunto de membros, possui um nome, descrição, quantidade de notificações e a privacidade do
 * grupo.
 * Além disso, ele está associado a um anúncio de imóvel e pode ter uma foto associada.
 *
 * @property id O ID único do grupo, atribuído após a criação no Firebase.
 * @property name O nome do grupo.
 * @property description A descrição do grupo.
 * @property qttMembers A quantidade de membros atualmente no grupo.
 * @property qttNotifications A quantidade de notificações pendentes no grupo.
 * @property users A lista de IDs de usuários membros do grupo.
 * @property advertisementId O ID do anúncio de imóvel associado ao grupo.
 * @property isPrivate Define se o grupo é privado ou público.
 * @property photoUri O URI da foto associada ao grupo (opcional).
 */
data class GroupModel(
    var id: String = "", // The ID will be assigned after the group is added to Firebase
    val name: String,
    val description: String,
    val qttMembers: Int,
    val qttNotifications: Int,
    val users: List<String>,
    val advertisementId: String,
    val isPrivate: Boolean,
    var photoUri: String? = null
) : Serializable {

    /**
     * Construtor adicional para criar um [GroupModel] com um ID específico.
     *
     * Este construtor permite inicializar um grupo com todos os dados, exceto as notificações
     * e os membros, que podem ser definidos depois.
     *
     * @param id O ID único do grupo.
     * @param name O nome do grupo.
     * @param description A descrição do grupo.
     * @param advertisementId O ID do anúncio associado ao grupo.
     * @param qttMembers A quantidade de membros do grupo.
     * @param isPrivate Define se o grupo é privado ou público.
     * @param photoUri URI da foto do grupo.
     */
    constructor(
        id: String,
        name: String,
        description: String,
        advertisementId: String,
        qttMembers: Int,
        isPrivate: Boolean,
        photoUri: String
    ) : this(
        id = id,
        name = name,
        description = description,
        qttMembers = qttMembers,
        qttNotifications = 0,
        users = emptyList<String>(),
        advertisementId = advertisementId,
        isPrivate = isPrivate,
        photoUri= photoUri
    )
}