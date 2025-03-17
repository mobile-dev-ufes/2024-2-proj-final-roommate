package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentFavoriteAdsBinding
import com.example.roommate.ui.adapters.ListAdAdapter


/**
 * Fragmento responsável por exibir os anúncios favoritos do usuário.
 * Este fragmento utiliza um RecyclerView para listar os anúncios e permite navegação para detalhes ao clicar em um item.
 */
class FragmentFavoriteAds : Fragment(R.layout.fragment_favorite_ads) {
    // Binding para acessar as views no layout de forma segura.
    private lateinit var binding: FragmentFavoriteAdsBinding

    // Adapter que será usado para preencher a lista de anúncios.
    private lateinit var adapter: ListAdAdapter

    /**
     * Método responsável por inflar o layout do fragmento e configurar o adapter da lista de anúncios.
     *
     * @param inflater O inflater utilizado para inflar o layout.
     * @param container O container onde o fragmento será adicionado.
     * @param savedInstanceState O estado salvo do fragmento, se houver.
     * @return A view do fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o binding
        binding = FragmentFavoriteAdsBinding.inflate(inflater, container, false)

        // Configura o adapter com as ações de clique
        adapter = ListAdAdapter(
            onClickItem = {
                // Navega para o fragmento de anúncio ao clicar em um item
                findNavController().navigate(R.id.action_fragmentFavoriteAds_to_fragmentAdvertisement)
            },
            onClickButton = { ad ->
                // Para implementações futuras
                // Toast.makeText(requireContext(), "Clicou no botão do anúncio: ${ad.title}", Toast.LENGTH_SHORT).show()
            }
        )

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada.
     * Aqui é configurado o RecyclerView para exibir a lista de anúncios.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo do fragmento, se houver.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter
    }
}