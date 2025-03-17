package com.example.roommate.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.AdLineBinding
import com.example.roommate.ui.viewHolders.ListAdViewHolder

/**
 * Adaptador responsável por exibir a lista de anúncios no RecyclerView.
 *
 * O [ListAdAdapter] é utilizado para gerenciar a lista de anúncios exibida na tela. Ele é responsável
 * por inflar os itens da lista, associar os dados de cada anúncio aos elementos da interface e fornecer
 * os métodos para atualizar a lista ou adicionar novos itens. O adaptador recebe dois lambdas como parâmetros:
 * um para lidar com cliques nos itens da lista e outro para gerenciar cliques em um botão específico dentro
 * de cada item de anúncio.
 *
 * @param onClickItem Função de callback chamada quando um item da lista é clicado.
 * @param onClickButton Função de callback chamada quando um botão dentro de um item é clicado.
 */
class ListAdAdapter(
    private val onClickItem: (AdModel) -> Unit,
    private val onClickButton: (AdModel) -> Unit
) : RecyclerView.Adapter<ListAdViewHolder>() {

    // Lista mutável que contém os anúncios a serem exibidos no RecyclerView
    private var adList: MutableList<AdModel> = mutableListOf()

    /**
     * Método chamado para criar a instância do ViewHolder, que é responsável por exibir um item da
     * lista.
     *
     * Esse método infla o layout do item da lista e retorna um ViewHolder contendo as referências
     * dos elementos da interface para o item específico.
     *
     * @param parent O [ViewGroup] onde o item será adicionado.
     * @param viewType O tipo de visualização do item.
     * @return O [ListAdViewHolder] que contém o layout do item e suas referências.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdViewHolder {
        val item = AdLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListAdViewHolder(item)
    }

    /**
     * Método chamado para vincular os dados do item com a visualização correspondente no RecyclerView.
     *
     * Esse método recebe a posição do item e o ViewHolder associado a ele e faz a ligação dos dados do
     * anúncio com os elementos de interface correspondentes.
     *
     * @param holder O [ListAdViewHolder] que contém os elementos da interface para o item.
     * @param position A posição do item na lista de anúncios.
     */
    override fun onBindViewHolder(holder: ListAdViewHolder, position: Int) {
        val ad = adList[position]
        holder.bindVH(ad, onClickButton)

        // Define o comportamento do clique no item da lista
        holder.itemView.setOnClickListener {
            onClickItem(ad)
        }
    }

    /**
     * Retorna o número de itens presentes na lista de anúncios.
     *
     * Esse método é utilizado pelo RecyclerView para determinar quantos itens ele precisa exibir.
     *
     * @return O número total de anúncios na lista.
     */
    override fun getItemCount(): Int = adList.size

    /**
     * Método responsável por atualizar a lista de anúncios exibida no RecyclerView.
     *
     * Esse método substitui a lista atual de anúncios pela nova lista passada como parâmetro e
     * notifica o RecyclerView de que os dados foram alterados, para que ele seja atualizado na tela.
     *
     * @param list A nova lista de anúncios a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateAdList(list: MutableList<AdModel>){
        adList = list
        notifyDataSetChanged()
    }

    /**
     * Método responsável por adicionar um novo anúncio à lista e atualizar a interface.
     *
     * Esse método insere um novo anúncio no final da lista de anúncios e notifica o RecyclerView de
     * que um novo item foi inserido, fazendo com que ele seja exibido na tela.
     *
     * @param ad O novo [AdModel] a ser inserido na lista de anúncios.
     */
    fun insertAdList(ad: AdModel){
        adList.add(ad)
        notifyItemInserted(adList.count())
    }
}