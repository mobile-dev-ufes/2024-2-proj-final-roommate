package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.statusEnum

/**
 * ViewModel responsável pela criação de anúncios no aplicativo.
 *
 * O [CreateAdViewModel] gerencia o fluxo de criação de anúncios, interagindo com o repositório
 * de anúncios ([AdRepository]). Ele mantém o estado atual da criação do anúncio, como sucesso
 * ou falha, e expõe esse estado por meio de um [MutableLiveData].
 *
 * @property adRepository Instância do [AdRepository] responsável por gerenciar a criação de
 * anúncios no sistema.
 * @property status [MutableLiveData] que mantém o status atual da operação de criação do anúncio.
 */
class CreateAdViewModel: ViewModel() {
    // Instância do repositório responsável pela criação de anúncios
    private val adRepository = AdRepository()

    // Status da criação do anúncio
    private var status = MutableLiveData<statusEnum>()

    /**
     * Retorna o [MutableLiveData] que contém o status da criação do anúncio.
     *
     * O status pode ser um valor da enumeração [statusEnum], como [SUCCESS], [FAIL], etc.
     *
     * @return [MutableLiveData] que contém o status da criação do anúncio.
     */
    fun isRegistered(): MutableLiveData<statusEnum> {
        return status
    }

    /**
     * Método responsável por criar um novo anúncio.
     *
     * Esse método chama o repositório de anúncios para criar um novo anúncio no sistema
     * com os dados fornecidos. O resultado da operação é emitido através do [status].
     *
     * @param user O [AdModel] que contém as informações do anúncio a ser criado.
     */
    fun registerAd(user: AdModel) {
        adRepository.create(user, status)
    }

}