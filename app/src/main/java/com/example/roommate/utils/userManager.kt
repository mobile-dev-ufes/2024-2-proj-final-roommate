package com.example.roommate.utils

import com.example.roommate.data.model.UserModel

/**
 * Objeto singleton responsável por gerenciar o usuário atual na aplicação.
 *
 * Este objeto contém uma instância de [UserModel] que representa o usuário atual. Ele pode ser usado para armazenar
 * ou acessar os dados do usuário durante o ciclo de vida da aplicação.
 *
 * A instância de [UserModel] é inicializada com valores padrão, mas pode ser alterada para refletir o usuário autenticado
 * ou registrado no sistema.
 *
 * - [user]: Instância da classe [UserModel] que contém as informações do usuário, como nome, e-mail, etc.
 */
object userManager {
    var user = UserModel()
}