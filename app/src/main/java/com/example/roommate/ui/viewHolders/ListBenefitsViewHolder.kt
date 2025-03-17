package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.BenefitsLineBinding

/**
 * ViewHolder responsável por exibir os dados de um benefício em uma linha dentro do RecyclerView.
 *
 * O [ListBenefitsViewHolder] é responsável por associar os dados de um benefício (representado como uma String)
 * aos elementos da interface dentro de uma célula do RecyclerView.
 *
 * @property binding O objeto [BenefitsLineBinding] que contém as referências dos elementos da interface.
 */
class ListBenefitsViewHolder(private val binding: BenefitsLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Método responsável por vincular os dados de um benefício aos elementos da interface.
     *
     * Esse método configura o texto do [TextView] que exibe o benefício dentro da célula do
     * RecyclerView.
     *
     * @param benefit A string representando um benefício a ser exibido.
     */
    fun bindVH(benefit: String) {
        binding.contentTv.text = benefit
    }
}