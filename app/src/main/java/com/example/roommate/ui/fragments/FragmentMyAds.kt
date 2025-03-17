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
import com.example.roommate.databinding.FragmentMyAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter
import com.example.roommate.viewModel.MyAdsViewModel


class FragmentMyAds : Fragment(R.layout.fragment_my_ads) {
    private lateinit var binding: FragmentMyAdsBinding
    private lateinit var adapter: ListAdAdapter
    private lateinit var myAdsVM: MyAdsViewModel

    /**
     * Cria a view do fragmento e inicializa as dependências necessárias.
     *
     * Este método é chamado para inflar a view do fragmento e configurar o
     * ViewModel e o adaptador para a lista de anúncios.
     *
     * @param inflater O LayoutInflater que irá inflar a view.
     * @param container O contêiner pai para o qual a view será anexada.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     * @return A view inflada que será exibida no fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o ViewModel responsável por obter os dados dos anúncios.
        myAdsVM = ViewModelProvider(this)[MyAdsViewModel::class.java]

        // Infla a view associada ao fragmento.
        binding = FragmentMyAdsBinding.inflate(inflater, container, false)

        // Configura o adaptador para a lista de anúncios.
        adapter = ListAdAdapter(
            onClickItem = { adModel ->
                // Ação de navegação para a tela de detalhes do anúncio.
                val action = FragmentMyAdsDirections.actionFragmentMyAdsToFragmentAdvertisement(adModel)
                findNavController().navigate(action)
            },
            onClickButton = { ad ->
                Toast.makeText(requireContext(), "Clicou no botão do anúncio: ${ad.title}", Toast.LENGTH_SHORT).show()
            },
            myAdsVM
        )

        return binding.root
    }

    /**
     * Chamado após a view ser criada. Configura os observadores de dados e o layout da lista.
     *
     * Neste método, a lista de anúncios é observada e atualizada quando os dados
     * são alterados. Além disso, o clique no botão de criação de anúncios é configurado.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o observador para atualizar a lista de anúncios.
        setObserver()

        // Solicita ao ViewModel os anúncios.
        myAdsVM.getAds()

        // Navega para a tela de criação de anúncio quando o botão é clicado.
        binding.myAdsBtn.setOnClickListener {
            val action = FragmentMyAdsDirections.actionFragmentMyAdsToFragmentCreateAd1(1)
            findNavController().navigate(action)
        }

        // Configura o RecyclerView para exibir os anúncios.
        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }

    /**
     * Configura o observador para a lista de anúncios.
     *
     * Este método configura o observador para a lista de anúncios do ViewModel.
     * Quando a lista é atualizada, o adaptador é notificado para atualizar a UI.
     */
    private fun setObserver(){
        // Observa a lista de anúncios do ViewModel e atualiza o adaptador.
        myAdsVM.getAdList().observe(viewLifecycleOwner){ adList ->
            adapter.updateAdList(adList)
        }
    }
}