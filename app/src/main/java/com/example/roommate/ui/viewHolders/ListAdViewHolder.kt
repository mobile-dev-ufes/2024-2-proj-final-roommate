package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.data.model.AdModel
import com.example.roommate.viewModel.MyAdsViewModel

/**
 * ViewHolder responsável por exibir os dados de um anúncio em uma linha dentro do RecyclerView.
 *
 * O [ListAdViewHolder] é responsável por associar os dados do modelo [AdModel] aos elementos da interface
 * (como o título, local e preço do anúncio) dentro de uma célula do RecyclerView. Ele também gerencia as interações,
 * como o clique no botão de favorito para um anúncio.
 *
 * @property binding O objeto [AdLineBinding] que contém as referências dos elementos da interface.
 */
class ListAdViewHolder(
    private val binding: AdLineBinding,
    private val adViewModel: MyAdsViewModel
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um anúncio aos elementos da interface.
     *
     * Esse método configura os textos e interações da interface com base no modelo de dados [AdModel],
     * e também define o comportamento do clique no ícone de favorito para o anúncio.
     *
     * @param ad O objeto [AdModel] contendo os dados do anúncio que serão exibidos.
     * @param onClickButton A função a ser chamada quando o ícone de favorito for clicado, passando o anúncio.
     */
    fun bindVH(ad: AdModel, onClickButton: (AdModel) -> Unit) {
        binding.adDescriptionTv.text = ad.title
        binding.adLocalTv.text = ad.localToString()
        binding.adPriceTv.text = ad.getValueString()

        // Call ViewModel function to load image
        adViewModel.getAdImage(ad.id.toString()) { imageUrl ->
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.adImg)
        }

        // Configura o clique no ícone de favorito
        binding.favoriteAdsHeart.setOnClickListener {
            onClickButton(ad)
        }

    }
}