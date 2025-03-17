package com.example.roommate.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roommate.data.model.AdModel
import com.example.roommate.data.repository.AdRepository
import com.example.roommate.utils.userManager

/**
 * ViewModel responsável por gerenciar os anúncios de um usuário.
 *
 * O [MyAdsViewModel] lida com operações relacionadas aos anúncios de um usuário específico.
 * Ele interage com o [AdRepository] para obter os anúncios criados pelo usuário e carregar
 * imagens dos anúncios.
 * A classe expõe os dados por meio de [LiveData], permitindo que a interface do usuário reaja a mudanças
 * nos dados de forma reativa.
 *
 * @property adRepository Instância do [AdRepository] para realizar operações relacionadas aos anúncios.
 * @property adList [MutableLiveData] que contém a lista de anúncios do usuário.
 * @property adImageUrl [LiveData] que contém a URL da imagem de um anúncio específico.
 */
class MyAdsViewModel: ViewModel() {
    private val adRepository = AdRepository() // Instância do repositório de anúncios

    private var adList = MutableLiveData<MutableList<AdModel>>() // Lista de anúncios do usuário

    private val _adImageUrl = MutableLiveData<String>() // URL da imagem de um anúncio
    val adImageUrl: LiveData<String> = _adImageUrl // LiveData para acessar a URL da imagem do anúncio

    /**
     * Retorna a lista de anúncios do usuário.
     *
     * @return [MutableLiveData] contendo a lista de anúncios.
     */
    fun getAdList() : MutableLiveData<MutableList<AdModel>>{
        return adList
    }

    /**
     * Obtém os anúncios criados pelo usuário atualmente autenticado.
     *
     * Esse método chama o repositório de anúncios para recuperar os anúncios do usuário atual,
     * identificando o usuário através do [userManager]. A lista de anúncios é atualizada no
     * [adList].
     */
    fun getAds() {
        adRepository.getAdsByUser(userManager.user.email!!, adList)
    }

    /**
     * Carrega a imagem de um anúncio específico.
     *
     * Esse método chama o repositório de anúncios para recuperar a URL da imagem associada a um anúncio.
     * Em caso de sucesso, a URL da imagem é postada no [adImageUrl]. Em caso de falha, um erro é registrado.
     *
     * @param adId O ID do anúncio cuja imagem será carregada.
     */
    fun loadAdImage(adId: String) {
        adRepository.getAdImage(
            adId = adId,
            onSuccess = { url -> _adImageUrl.postValue(url) },
            onFailure = { exception -> Log.e("adViewModel", "Failed to get image URL", exception) }
        )
    }
}