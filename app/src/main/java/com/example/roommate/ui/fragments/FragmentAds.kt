package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.viewModel.FeedViewModel

/**
 * Fragment que exibe uma lista de anúncios.
 *
 * O [FragmentAds] é responsável por exibir uma lista de anúncios e permitir que o usuário interaja
 *  com eles.
 * O fragmento utiliza um [RecyclerView] para exibir os anúncios, e o usuário pode clicar em um item
 *  da lista para visualizar detalhes sobre o anúncio ou clicar em um botão específico dentro do anúncio.
 *
 * O fragmento também permite que o usuário navegue para a tela de criação de um novo anúncio.
 */
class FragmentAds : Fragment(R.layout.fragment_ads) {
    // Binding da view da fragmento, que acessa os elementos da interface
    private lateinit var binding: FragmentAdsBinding

    // Adaptador para exibir a lista de anúncios no RecyclerView
    private lateinit var adapter: ListAdAdapter

    // ViewModel responsável pelo gerenciamento dos anúncios
    private lateinit var adsVM: FeedViewModel

    /**
     * Método chamado para inflar a view do fragmento e inicializar o ViewModel.
     *
     * @param inflater O objeto LayoutInflater usado para inflar a view.
     * @param container O container no qual a view será colocada.
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     * @return A raiz da view inflada.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o binding da fragmento
        binding = FragmentAdsBinding.inflate(inflater, container, false)

        // Inicializa o adaptador para a lista de anúncios
        adapter = ListAdAdapter(
            onClickItem = { adModel ->
                // Navega para a tela de detalhes do anúncio quando um item da lista é clicado
                val action = FragmentAdsDirections.actionFragmentAdsToFragmentAdvertisement(adModel)
                findNavController().navigate(action)
            },
            onClickButton = { ad ->
                // Exibe uma mensagem de Toast quando o botão de um anúncio é clicado
                Toast.makeText(requireContext(), "Clicou no botão do anúncio: ${ad.title}", Toast.LENGTH_SHORT).show()
            }
        )

        // Inicializa o ViewModel responsável por fornecer a lista de anúncios
        adsVM = ViewModelProvider(this)[FeedViewModel::class.java]

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para ser interagida.
     *
     * - Configura o Observer para observar as mudanças na lista de anúncios.
     * - Configura o RecyclerView e define o adaptador.
     * - Configura a navegação para a criação de novos anúncios.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o observer para a lista de anúncios
        setObserver()

        // Obtém a lista de anúncios ao carregar a fragmento
        adsVM.getAds()

        // Configura a navegação para a criação de um novo anúncio
        binding.createAdBtn.setOnClickListener {
            val action = FragmentAdsDirections.actionFragmentAdsToFragmentCreateAd1(0)
            findNavController().navigate(action)
        }

        // Configura o RecyclerView com o LayoutManager e o adaptador
        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }

    /**
     * Método que configura o Observer para a lista de anúncios.
     * O observer irá atualizar o adaptador quando a lista de anúncios mudar.
     */
    private fun setObserver(){
        adsVM.getAdList().observe(viewLifecycleOwner) { adList ->
            adapter.updateAdList(adList)
        }
    }
}