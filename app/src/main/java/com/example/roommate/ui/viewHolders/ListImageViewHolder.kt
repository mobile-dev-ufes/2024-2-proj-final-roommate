package com.example.roommate.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.ImgsAdLineBinding


/**
 * ViewHolder responsável por exibir imagens de um anúncio em uma linha dentro do RecyclerView.
 *
 * O [ListImageViewHolder] é utilizado para associar as imagens de um anúncio aos elementos da interface
 * dentro de uma célula do RecyclerView. Essa classe pode ser expandida para manipular visualmente
 * as imagens quando necessário.
 *
 * @property binding O objeto [ImgsAdLineBinding] que contém as referências dos elementos da interface,
 *                    incluindo os elementos de imagem do layout da célula.
 */
class ListImageViewHolder(private val binding: ImgsAdLineBinding) :
    RecyclerView.ViewHolder(binding.root) {
}