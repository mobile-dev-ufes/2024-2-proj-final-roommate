package com.example.roommate.utils

/**
 * Enumeração que representa os diferentes estados de autenticação.
 *
 * Cada valor desta enumeração descreve um possível estado de autenticação e estado gerado para cadastro no Firebase Auth durante o processo de login ou verificação de usuário.
 *
 * - [SUCCESS]: Indica que a autenticação foi bem-sucedida.
 * - [WEAK_PASS]: Indica que a senha fornecida é considerada fraca.
 * - [COLLISION]: Indica que houve um conflito (por exemplo, nome de usuário ou e-mail já existente).
 * - [NETWORK]: Indica que ocorreu um erro relacionado à rede.
 * - [FAIL]: Indica que a autenticação falhou, mas sem um motivo específico.
 * - [NIL]: Indica que não há estado de autenticação definido ou o valor é nulo.
 * - [INVALID_USER]: Indica que o usuário fornecido é inválido ou não existe na autenticação.
 * - [INVALID_CREDENTIAL]: Indica que as credenciais fornecidas são inválidas.
 */

enum class authStatusEnum {
    SUCCESS, WEAK_PASS, COLLISION, NETWORK, FAIL, NIL, INVALID_USER, INVALID_CREDENTIAL
}