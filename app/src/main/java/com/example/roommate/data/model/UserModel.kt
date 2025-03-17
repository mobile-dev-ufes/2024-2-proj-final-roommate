package com.example.roommate.data.model

import java.io.Serializable
import java.time.LocalDate

/**
 * Modelo de um usuário dentro do aplicativo.
 *
 * A classe [UserModel] representa um usuário no sistema, contendo informações básicas como nome,
 * biografia, sexo, telefone, data de nascimento e foto do perfil.
 *
 * @property email O e-mail do usuário (opcional).
 * @property name O nome do usuário.
 * @property bio A biografia do usuário (opcional).
 * @property sex O sexo do usuário (opcional).
 * @property phone O telefone do usuário (opcional).
 * @property birthDate A data de nascimento do usuário (opcional).
 * @property photo_uri O URI da foto de perfil do usuário (opcional).
 */
class UserModel(
    var email: String? = null,
    var name: String? = null,
    var bio: String? = null,
    var sex: String? = null,
    var phone: String? = null,
    var birthDate: LocalDate? = null,
    var photo_uri: String? = null
) : Serializable {
    /**
     * Construtor secundário para criar um [UserModel] com nome e biografia.
     *
     * Este construtor pode ser usado quando o usuário tem nome e biografia, mas outros dados (como email, sexo, etc.) não são necessários.
     *
     * @param name O nome do usuário.
     * @param bio A biografia do usuário (opcional).
     */
    constructor(name: String, bio: String?) : this(
        null,
        name,
        bio,
        null,
        null,
        null,
        null
    )

    /**
     * Construtor secundário para criar um [UserModel] com nome, biografia e telefone.
     *
     * Este construtor pode ser usado quando o usuário tem nome, biografia e telefone, mas outros
     * dados (como email, sexo, etc.) não são necessários.
     *
     * @param name O nome do usuário.
     * @param bio A biografia do usuário.
     * @param phone O telefone do usuário.
     */
    constructor(name: String, bio: String, phone: String) : this(
        null,
        name,
        bio,
        null,
        phone,
        null,
        null
    )
}