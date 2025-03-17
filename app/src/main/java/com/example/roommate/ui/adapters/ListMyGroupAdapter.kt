package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.MyGroupsLineBinding
import com.example.roommate.ui.viewHolders.ListMyGroupViewHolder
import com.example.roommate.viewModel.GroupViewModel
import com.example.roommate.viewModel.UserViewModel

class ListMyGroupAdapter(
    private val onClickItem: (GroupModel) -> Unit,
    private val groupViewModel: GroupViewModel
) : RecyclerView.Adapter<ListMyGroupViewHolder>() {
/**
 * Adaptador responsável por exibir a lista de grupos do usuário em um RecyclerView.
 *
 * O [ListMyGroupAdapter] gerencia a exibição dos grupos nos quais o usuário está envolvido dentro de um RecyclerView.
 * Cada item da lista é associado a um objeto [GroupModel], e ao clicar em um item, ele executa a ação definida em [onItemClick].
 *
 * @property onItemClick Função chamada quando um item (grupo) é clicado, recebendo o [GroupModel] do grupo selecionado.
 */

    // Lista mutável que contém os grupos a serem exibidos no RecyclerView
    private var groupList: MutableList<GroupModel> = mutableListOf()

    /**
     * Método chamado para criar a instância do ViewHolder, responsável por exibir um item da lista.
     *
     * Esse método infla o layout do item da lista e retorna um [ListMyGroupViewHolder] contendo as referências
     * dos elementos da interface para o item específico.
     *
     * @param parent O [ViewGroup] onde o item será adicionado.
     * @param viewType O tipo de visualização do item.
     * @return O [ListMyGroupViewHolder] que contém o layout do item e suas referências.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMyGroupViewHolder {
        val item = MyGroupsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMyGroupViewHolder(item, groupViewModel)
    }

    /**
     * Método chamado para vincular os dados do item com a visualização correspondente no RecyclerView.
     *
     * Esse método recebe a posição do item e o [ListMyGroupViewHolder] associado a ele, fazendo a ligação dos dados do
     * grupo com os elementos de interface correspondentes.
     *
     * @param holder O [ListMyGroupViewHolder] que contém os elementos da interface para o item.
     * @param position A posição do item na lista de grupos.
     */
    override fun onBindViewHolder(holder: ListMyGroupViewHolder, position: Int) {
        holder.bindVH(groupList[position])

        // Configura o clique do item para passar o grupo selecionado
        holder.itemView.setOnClickListener {
            onClickItem(groupList[position])  // Passa o grupo quando o item é clicado
        }
    }

    /**
     * Retorna o número de itens presentes na lista de grupos.
     *
     * Esse método é utilizado pelo RecyclerView para determinar quantos itens ele precisa exibir.
     *
     * @return O número total de grupos na lista.
     */
    override fun getItemCount(): Int = groupList.size

    /**
     * Método responsável por atualizar a lista de grupos exibida no RecyclerView.
     *
     * Esse método substitui a lista atual de grupos pela nova lista passada como parâmetro, limpando a lista existente
     * para evitar referências duplicadas. Após isso, ele notifica o RecyclerView de que os dados foram alterados,
     * para que ele seja atualizado na tela.
     *
     * @param list A nova lista de grupos a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateGroupList(list: MutableList<GroupModel>) {
        groupList.clear() // Avoid replacing the reference
        groupList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Método responsável por adicionar um novo grupo à lista e atualizar a interface.
     *
     * Esse método insere um novo grupo no final da lista de grupos e notifica o RecyclerView de que um novo item foi
     * inserido, fazendo com que ele seja exibido na tela.
     *
     * @param group O novo grupo a ser inserido na lista de grupos.
     */
    fun insertGroupList(group: GroupModel) {
        groupList.add(group)
        notifyItemInserted(groupList.size - 1) // Corrected index
    }
}