package com.example.roommate.utils

/**
 * Enumeração que representa os diferentes status durante a recuperação e registro de dados no
 *  banco de dados.
 *
 * Cada valor desta enumeração reflete um possível estado de operação quando interagindo com o
 *  banco de dados, seja para registrar ou recuperar informações.
 *
 * - [SUCCESS]: Indica que a operação foi bem-sucedida e os dados foram registrados ou recuperados
 *  corretamente.
 * - [FAIL]: Indica que houve uma falha na operação, podendo ser devido a um erro de banco de dados,
 *  de validação ou outro tipo de falha.
 * - [NIL]: Indica que não há estado de autenticação definido ou o valor é nulo.
 * - [FAIL_IMG]: Indica que ocorreu uma falha ao tentar registrar ou recuperar uma imagem ou arquivo
 *  relacionado ao banco de dados.
 */
enum class statusEnum {
    SUCCESS,
    FAIL,
    NIL,
    FAIL_IMG,
}