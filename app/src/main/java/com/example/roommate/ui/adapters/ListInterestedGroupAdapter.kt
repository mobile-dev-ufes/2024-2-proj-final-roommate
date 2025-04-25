package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.InterestedGroupsLineBinding
import com.example.roommate.ui.viewHolders.ListInterestedGroupViewHolder
import com.example.roommate.viewModel.GroupViewModel

/**
 * Adaptador responsável por exibir a lista de grupos interessados no RecyclerView.
 *
 * O [ListInterestedGroupAdapter] é utilizado para gerenciar a lista de grupos interessados exibida
 * na tela.
 * Ele é responsável por inflar os itens da lista, associar os dados de cada grupo aos elementos da
 * interface e fornecer os métodos para atualizar a lista ou adicionar novos itens.
 *
 * @property onClickItem Função que será chamada quando um item da lista for clicado, recebendo o [GroupModel] correspondente.
 */
class ListInterestedGroupAdapter(
    private val onClickItem: (GroupModel) -> Unit,
    private val groupViewModel: GroupViewModel
) : RecyclerView.Adapter<ListInterestedGroupViewHolder>() {

    // Lista mutável que contém os grupos interessados a serem exibidos no RecyclerView
        private val groupList: MutableList<GroupModel> = mutableListOf()

    /**
     * Método chamado para criar a instância do ViewHolder, que é responsável por exibir um item da lista.
     *
     * Esse método infla o layout do item da lista e retorna um [ListInterestedGroupViewHolder] contendo
     * as referências dos elementos da interface para o item específico.
     *
     * @param parent O [ViewGroup] onde o item será adicionado.
     * @param viewType O tipo de visualização do item.
     * @return O [ListInterestedGroupViewHolder] que contém o layout do item e suas referências.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListInterestedGroupViewHolder {
        val item =
            InterestedGroupsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ListInterestedGroupViewHolder(item, groupViewModel)
    }

    /**
     * Método chamado para vincular os dados do item com a visualização correspondente no RecyclerView.
     *
     * Esse método recebe a posição do item e o [ListInterestedGroupViewHolder] associado a ele e faz a ligação dos dados do
     * grupo com os elementos de interface correspondentes.
     *
     * @param holder O [ListInterestedGroupViewHolder] que contém os elementos da interface para o item.
     * @param position A posição do item na lista de grupos.
     */
    override fun onBindViewHolder(holder: ListInterestedGroupViewHolder, position: Int) {
        val group = groupList[position]
        holder.bindVH(group)

        holder.itemView.setOnClickListener {
            onClickItem(group)  // Pass the group when the item is clicked
        }
    }

    /**
     * Retorna o número de itens presentes na lista de grupos interessados.
     *
     * Esse método é utilizado pelo RecyclerView para determinar quantos itens ele precisa exibir.
     *
     * @return O número total de grupos na lista.
     */
    override fun getItemCount(): Int {
        return groupList.count()
    }

    /**
     * Método responsável por adicionar um novo grupo à lista e atualizar a interface.
     *
     * Esse método insere um novo grupo no final da lista de grupos e notifica o RecyclerView de que um novo item foi
     * inserido, fazendo com que ele seja exibido na tela.
     *
     * @param group O novo grupo a ser inserido na lista de grupos.
     */
    fun insertGroupList(group: GroupModel){
        groupList.add(group)
        notifyItemInserted(groupList.count())
    }

    /**
     * Método responsável por atualizar a lista de grupos interessados exibida no RecyclerView.
     *
     * Esse método substitui a lista atual de grupos pela nova lista passada como parâmetro, limpando a lista existente
     * para evitar referências duplicadas. Após isso, ele notifica o RecyclerView de que os dados foram alterados,
     * para que ele seja atualizado na tela.
     *
     * @param list A nova lista de grupos interessados a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateGroupList(list: MutableList<GroupModel>) {
        groupList.clear() // Avoid replacing the reference
        groupList.addAll(list)
        notifyDataSetChanged()
    }
}