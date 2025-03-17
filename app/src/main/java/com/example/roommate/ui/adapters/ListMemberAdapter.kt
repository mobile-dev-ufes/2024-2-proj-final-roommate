package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.UserModel
import com.example.roommate.databinding.EntryRequestsLineBinding
import com.example.roommate.ui.viewHolders.ListMemberViewHolder
import com.example.roommate.viewModel.UserViewModel

/**
 * Adaptador responsável por exibir a lista de membros em uma interface de usuário.
 *
 * O [ListMemberAdapter] gerencia a exibição de membros dentro de um RecyclerView. Cada item da
 * lista é associado a um objeto [UserModel], e ao clicar em um item, ele executa a ação definida
 * na função [onClickItem].
 *
 * @property onClickItem Função chamada quando um item (membro) é clicado, recebendo o [UserModel]
 * do membro selecionado.
 */
class ListMemberAdapter(
    private val onClickItem: (UserModel) -> Unit,
    private val userViewModel: UserViewModel
): RecyclerView.Adapter<ListMemberViewHolder>() {
    private val memberList: MutableList<UserModel> = mutableListOf()

    /**
     * Método chamado para criar a instância do ViewHolder, responsável por exibir um item da lista.
     *
     * Esse método infla o layout do item da lista e retorna um [ListMemberViewHolder] contendo as referências
     * dos elementos da interface para o item específico.
     *
     * @param parent O [ViewGroup] onde o item será adicionado.
     * @param viewType O tipo de visualização do item.
     * @return O [ListMemberViewHolder] que contém o layout do item e suas referências.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMemberViewHolder {
        val item = EntryRequestsLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMemberViewHolder(item, userViewModel)
    }

    /**
     * Método chamado para vincular os dados do item com a visualização correspondente no RecyclerView.
     *
     * Esse método recebe a posição do item e o [ListMemberViewHolder] associado a ele, fazendo a ligação dos dados do
     * membro com os elementos de interface correspondentes.
     *
     * @param holder O [ListMemberViewHolder] que contém os elementos da interface para o item.
     * @param position A posição do item na lista de membros.
     */
    override fun onBindViewHolder(holder: ListMemberViewHolder, position: Int) {
        holder.bindVH(memberList[position])

        // Configura o clique do item para passar o membro selecionado
        holder.itemView.setOnClickListener{
            onClickItem(memberList[position])
        }
    }

    /**
     * Retorna o número de itens presentes na lista de membros.
     *
     * Esse método é utilizado pelo RecyclerView para determinar quantos itens ele precisa exibir.
     *
     * @return O número total de membros na lista.
     */
    override fun getItemCount(): Int = memberList.size

    /**
     * Método responsável por atualizar a lista de membros exibida no RecyclerView.
     *
     * Esse método substitui a lista atual de membros pela nova lista passada como parâmetro, limpando a lista existente
     * para evitar referências duplicadas. Após isso, ele notifica o RecyclerView de que os dados foram alterados,
     * para que ele seja atualizado na tela.
     *
     * @param list A nova lista de membros a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateMemberList(list: MutableList<UserModel>) {
        memberList.clear() // Avoid replacing the reference
        memberList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Método responsável por adicionar um novo membro à lista e atualizar a interface.
     *
     * Esse método insere um novo membro no final da lista de membros e notifica o RecyclerView de que um novo item foi
     * inserido, fazendo com que ele seja exibido na tela.
     *
     * @param user O novo membro a ser inserido na lista de membros.
     */
    fun insertMemberList(user: UserModel) {
        memberList.add(user)
        notifyItemInserted(memberList.count())
    }
}
