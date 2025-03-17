package com.example.roommate.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository

/**
 * ViewModel responsável por gerenciar o feed de anúncios no aplicativo.
 *
 * O [FeedViewModel] gerencia a lista de anúncios que serão exibidos no feed.
 * Ele interage com o repositório de anúncios ([AdRepository]) para obter os dados necessários e
 * mantém a lista de anúncios em um [MutableLiveData], permitindo que a interface do usuário
 * reaja a mudanças nos dados.
 *
 * @property adRepository Instância do [AdRepository] responsável por recuperar os anúncios do sistema.
 * @property adList [MutableLiveData] que contém a lista de anúncios a ser exibida no feed.
 */
class FeedViewModel: ViewModel() {
    // Instância do repositório responsável por obter anúncios
    private val adRepository = AdRepository()

    // Lista de anúncios a ser exibida no feed
    private var adList = MutableLiveData<MutableList<AdModel>>()

    /**
     * Retorna o [MutableLiveData] que contém a lista de anúncios.
     *
     * Esse [LiveData] será observado pela interface do usuário para exibir os anúncios.
     *
     * @return [MutableLiveData] que contém a lista de anúncios a ser exibida.
     */
    fun getAdList() : MutableLiveData<MutableList<AdModel>>{
        return adList
    }

    /**
     * Método responsável por recuperar todos os anúncios através do repositório.
     *
     * Esse método chama o repositório de anúncios para buscar os dados necessários
     * e atualiza a lista de anúncios mantida em [adList].
     */
    fun getAds() {
        adRepository.getAllAds(adList)
    }

}