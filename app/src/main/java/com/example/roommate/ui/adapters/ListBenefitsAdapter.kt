package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.databinding.BenefitsLineBinding
import com.example.roommate.ui.viewHolders.ListBenefitsViewHolder


/**
 * Adaptador responsável por exibir a lista de benefícios no RecyclerView.
 *
 * O [ListBenefitsAdapter] é utilizado para gerenciar a lista de benefícios exibida na tela. Ele é responsável
 * por inflar os itens da lista, associar os dados de cada benefício aos elementos da interface e fornecer
 * os métodos para atualizar a lista ou adicionar novos itens.
 */
class ListBenefitsAdapter : RecyclerView.Adapter<ListBenefitsViewHolder>() {
    // Lista mutável que contém os benefícios a serem exibidos no RecyclerView
    private var benefitsList: MutableList<String> = mutableListOf()

    /**
     * Método chamado para criar a instância do ViewHolder, que é responsável por exibir um item da
     * lista.
     *
     * Esse método infla o layout do item da lista e retorna um ViewHolder contendo as referências
     * dos elementos da interface para o item específico.
     *
     * @param parent O [ViewGroup] onde o item será adicionado.
     * @param viewType O tipo de visualização do item.
     * @return O [ListBenefitsViewHolder] que contém o layout do item e suas referências.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBenefitsViewHolder {
        val item = BenefitsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListBenefitsViewHolder(item)
    }

    /**
     * Método chamado para vincular os dados do item com a visualização correspondente no RecyclerView.
     *
     * Esse método recebe a posição do item e o ViewHolder associado a ele e faz a ligação dos dados do
     * benefício com os elementos de interface correspondentes.
     *
     * @param holder O [ListBenefitsViewHolder] que contém os elementos da interface para o item.
     * @param position A posição do item na lista de benefícios.
     */
    override fun onBindViewHolder(
        holder: ListBenefitsViewHolder, position: Int
    ) {
        holder.bindVH(benefitsList[position])
    }

    /**
     * Retorna o número de itens presentes na lista de benefícios.
     *
     * Esse método é utilizado pelo RecyclerView para determinar quantos itens ele precisa exibir.
     *
     * @return O número total de benefícios na lista.
     */
    override fun getItemCount(): Int {
        return benefitsList.count()
    }

    /**
     * Método responsável por atualizar a lista de benefícios exibida no RecyclerView.
     *
     * Esse método substitui a lista atual de benefícios pela nova lista passada como parâmetro e notifica
     * o RecyclerView de que os dados foram alterados, para que ele seja atualizado na tela.
     *
     * @param list A nova lista de benefícios a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateBenefitList(list: MutableList<String>){
        benefitsList = list
        notifyDataSetChanged()
    }


    /**
     * Método responsável por adicionar um novo benefício à lista e atualizar a interface.
     *
     * Esse método insere um novo benefício no final da lista de benefícios e notifica o RecyclerView de que
     * um novo item foi inserido, fazendo com que ele seja exibido na tela.
     *
     * @param benefit O novo benefício a ser inserido na lista de benefícios.
     */
    fun insertBenefitList(benefit: String){
        benefitsList.add(benefit)
        notifyItemInserted(benefitsList.count())
    }
}