package com.example.roommate.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.FragmentAdvertisementBinding
import com.example.roommate.ui.adapters.ListBenefitsAdapter
import com.example.roommate.viewModel.MyAdsViewModel

/**
 * Fragmento que exibe os detalhes de um anúncio específico.
 *
 * O [FragmentAdvertisement] é responsável por exibir informações detalhadas sobre um anúncio,
 * como o título, descrição, localização e benefícios. O fragmento também permite que o usuário
 * navegue para grupos de interesse relacionados ao anúncio ou visualize a localização no Google Maps.
 *
 * O fragmento utiliza um [RecyclerView] para exibir os benefícios do anúncio e usa [Glide] para
 * carregar a imagem do anúncio.
 */
class FragmentAdvertisement : Fragment(R.layout.fragment_advertisement) {

    // Binding da view do fragmento, que acessa os elementos da interface
    private lateinit var binding: FragmentAdvertisementBinding

    // Adaptador para exibir a lista de benefícios do anúncio
    private lateinit var adapter: ListBenefitsAdapter

    // ViewModel responsável pelo gerenciamento dos dados do anúncio
    private lateinit var adViewModel: MyAdsViewModel

    // Argumentos passados ao fragmento contendo as informações do anúncio
    private val args: FragmentAdvertisementArgs by navArgs()

    /**
     * Método chamado para inflar a view do fragmento e inicializar o ViewModel.
     *
     * @param inflater O objeto LayoutInflater usado para inflar a view.
     * @param container O container no qual a view será colocada.
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     * @return A raiz da view inflada.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

            // Inicializa o binding da fragmento
        binding = FragmentAdvertisementBinding.inflate(inflater, container, false)

        // Inicializa o adaptador para a lista de benefícios
        adapter = ListBenefitsAdapter()

        // Inicializa o ViewModel para obter dados do anúncio
        adViewModel = ViewModelProvider(this).get(MyAdsViewModel::class.java)

        // Preenche as informações do anúncio na interface
        bindInfo()

        return binding.root
    }

    /**
     * Método chamado quando a view foi criada e está pronta para ser interagida.
     * Configura as ações do botão e a exibição dos benefícios.
     */
    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o clique para navegar até os grupos interessados
        binding.adGroupBtn.setOnClickListener {
            val action =
                FragmentAdvertisementDirections.actionFragmentAdvertisementToFragmentInterestedGroups(
                    args.adInfo.id.toString()
                    )
            findNavController().navigate(action)
        }

        // Configura o clique para abrir o Google Maps com a localização do anúncio
        binding.gMapsImg.setOnClickListener {
            goToMap()
        }

        // Configura o RecyclerView para exibir os benefícios do anúncio
        binding.recycleListBenefits.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recycleListBenefits.adapter = adapter

        // Atualiza a lista de benefícios no adaptador
        adapter.updateBenefitList(args.adInfo.getBenefitsList())
    }

    /**
     * Método responsável por vincular as informações do anúncio na interface.
     *
     * As informações vinculadas incluem título, descrição, quantidade de quartos, área, clientes,
     * estacionamento e o valor do aluguel.
     */
    private fun bindInfo() {
        binding.adTitle.text = args.adInfo.title
        binding.localTv.text = args.adInfo.localToString()
        binding.adDescriptionTv.text = args.adInfo.description
        binding.adAddressTv.text = args.adInfo.localCompleteToString()

        binding.bedroomTv.text = args.adInfo.bedroom_qtt.toString()
        binding.suitesTv.text = args.adInfo.suite_qtt.toString()
        binding.areaTv.text = args.adInfo.area.toString()
        binding.clientsTv.text = args.adInfo.max_client.toString()
        binding.parkingTv.text = args.adInfo.parking_qtt.toString()

        binding.rentValue.text = args.adInfo.getValueString()

        // Observa a URL da imagem do anúncio e carrega usando o Glide
        adViewModel.adImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.adImgRv)
        }

        // Carrega a imagem do anúncio com base no seu ID
        val adId = args.adInfo.id.toString()
        adViewModel.loadAdImage(adId)
    }

    /**
     * Método para abrir o Google Maps e exibir a localização do anúncio.
     * A localização é extraída dos detalhes do anúncio e convertida em um endereço de busca.
     */
    private fun goToMap() {
        val address =
            "${args.adInfo.local?.street}, ${args.adInfo.local?.number}, ${args.adInfo.local?.nb}, ${args.adInfo.local?.city}, ${args.adInfo.local?.state}"

        val webUri = Uri.parse("https://www.google.com/maps/search/$address")

        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        startActivity(webIntent)
    }
}