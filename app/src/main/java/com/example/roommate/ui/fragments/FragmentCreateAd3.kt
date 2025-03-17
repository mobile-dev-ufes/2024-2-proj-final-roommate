package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.FragmentCreateAd3Binding
import com.example.roommate.utils.statusEnum
import com.example.roommate.viewModel.CreateAdViewModel

/**
 * Fragmento responsável pela terceira e última etapa do processo de criação de um anúncio de imóvel.
 * Nesta etapa, o usuário finaliza o cadastro do anúncio, incluindo a seleção de benefícios adicionais
 * (ex: garagem, internet, ar-condicionado) e o preenchimento das quantidades de quartos, suítes,
 * vagas de estacionamento e área do imóvel.
 *
 * O fragmento coleta essas informações e as envia para o ViewModel para registro.
 * Após o registro, o usuário será redirecionado para a tela de anúncios ou para a tela de meus
 * anúncios, dependendo da origem da navegação.
 */
class FragmentCreateAd3 : Fragment(R.layout.fragment_create_ad3) {

    // Binding da view do fragmento, que acessa os elementos da interface
    private lateinit var binding: FragmentCreateAd3Binding

    // ViewModel responsável pelo gerenciamento do processo de criação do anúncio
    private lateinit var createAdVM: CreateAdViewModel

    // Argumentos passados para o fragmento, que contêm dados sobre o anúncio e a navegação
    private val args: FragmentCreateAd3Args by navArgs()

    /**
     * Método chamado para inflar a view do fragmento e inicializar a interface.
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

        // Infla o layout do fragmento e inicializa o binding
        binding = FragmentCreateAd3Binding.inflate(inflater, container, false)

        // Inicializa o ViewModel
        createAdVM = ViewModelProvider(this)[CreateAdViewModel::class.java]

        return binding.root
    }

    /**
     * Método chamado quando a view foi criada e está pronta para interagir.
     * Configura os listeners para o botão de finalizar o cadastro e o observador do estado de
     * registro.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define o observador para o status de registro do anúncio
        setObserver()

        // Configura o clique do botão de finalizar o cadastro do anúncio
        binding.ad3FinishRegistration.setOnClickListener {
            createAdVM.registerAd(adModelFromViewInfo())  // Registra o anúncio com as informações coletadas
        }
    }

    /**
     * Cria um objeto [AdModel] com as informações coletadas da interface.
     * As informações incluem os benefícios selecionados, além das quantidades de quartos, suítes, vagas
     * de estacionamento e área do imóvel.
     *
     * @return O modelo de anúncio com as informações coletadas.
     */
    private fun adModelFromViewInfo(): AdModel {
        // Mapeia os benefícios selecionados pelo usuário
        val benefits = mapOf(
            "ladies" to binding.ladiesCb.isChecked,
            "garage" to binding.garageCb.isChecked,
            "internet" to binding.internetCb.isChecked,
            "hotWater" to binding.hotWaterCb.isChecked,
            "ar" to binding.arCb.isChecked,
            "pool" to binding.poolCb.isChecked,
            "pet" to binding.petCb.isChecked,
            "security" to binding.securityCb.isChecked)

        // Cria o objeto AdModel com as informações preenchidas
        return AdModel(
            owner = args.adInfo.owner,
            title = args.adInfo.title,
            rent_value = args.adInfo.rent_value,
            cond_value = args.adInfo.cond_value,
            max_client = args.adInfo.max_client,
            description = args.adInfo.description,
            local = args.adInfo.local,
            photos = args.adInfo.photos,
            bedroom_qtt = binding.bedroomEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            suite_qtt = binding.suitesEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            parking_qtt = binding.parkingEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            area = binding.areaEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0,
            benefits = benefits,
            groups = arrayOf()
        )
    }

    /**
     * Navega para a tela de anúncios ou para a tela de meus anúncios, dependendo da origem da navegação.
     * Após o registro do anúncio, a pilha de navegação é limpa para evitar voltar para o processo de criação.
     */
    private fun navigate(){
        val navOptions: NavOptions
        val id: Int

        // Verifica a origem da navegação e define a tela para a qual o usuário será redirecionado
        if (args.route == 0){
            navOptions =
                NavOptions.Builder()
                    .setPopUpTo(R.id.fragmentAds, true) // Limpa a pilha de navegação até o fragmento de anúncios
                    .build()
            id = R.id.action_fragmentCreateAd3_to_fragmentAds
        } else {
            navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.fragmentMyAds, true) // Limpa a pilha de navegação até o fragmento de meus anúncios
                .build()
            id = R.id.action_fragmentCreateAd3_to_fragmentMyAds
        }

        // Realiza a navegação para a tela selecionada
        findNavController().navigate(id, null, navOptions)
    }

    /**
     * Define o observador para o status de registro do anúncio. Quando o status muda,
     * o usuário é informado e a navegação para a tela correta é realizada.
     */
    private fun setObserver() {
        createAdVM.isRegistered().observe(viewLifecycleOwner) { status ->
            when (status) {
                statusEnum.SUCCESS -> {
                    Toast.makeText(
                        requireContext(),
                        "Anúncio registrado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate() // Navega após o sucesso do registro
                }

                statusEnum.FAIL -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro! Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate()  // Navega após falha no registro
                }

                statusEnum.FAIL_IMG -> {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um ao salvar a imagem.",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigate() // Navega após erro no upload da imagem
                }

                else -> UInt
            }
        }
    }
}