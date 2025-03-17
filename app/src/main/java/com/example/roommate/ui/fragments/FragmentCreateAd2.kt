package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.Address
import com.example.roommate.databinding.FragmentCreateAd2Binding

/**
 * Fragmento responsável pela segunda etapa do processo de criação de um anúncio de imóvel.
 * Nesta etapa, o usuário informa o endereço completo do imóvel, incluindo CEP, número, rua,
 * bairro, cidade e estado.
 *
 * O fragmento coleta as informações inseridas pelo usuário e as associa ao objeto de anúncio.
 * Após a coleta dos dados, o usuário pode prosseguir para a próxima etapa do processo de criação de
 * anúncio.
 */
class FragmentCreateAd2 : Fragment(R.layout.fragment_create_ad2) {

    // Binding da view do fragmento, que acessa os elementos da interface
    private lateinit var binding: FragmentCreateAd2Binding

    // Argumentos passados para o fragmento, que contêm dados sobre o anúncio e a navegação
    private val args: FragmentCreateAd2Args by navArgs()

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

        binding = FragmentCreateAd2Binding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Método chamado quando a view foi criada e está pronta para interagir.
     * Configura os listeners para o botão de prosseguir, que coleta as informações do endereço
     * e navega para a próxima etapa.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o clique do botão para prosseguir para a próxima etapa
        binding.ad2ProceedBtn.setOnClickListener {
            updateAdModelFromViewInfo()  // Atualiza o modelo de anúncio com as informações do endereço
            val action = FragmentCreateAd2Directions.actionFragmentCreateAd2ToFragmentCreateAd3(args.adInfo, args.route)
            findNavController().navigate(action)  // Navega para a próxima etapa
        }
    }

    /**
     * Atualiza o modelo de anúncio [AdModel] com as informações do endereço coletadas
     * a partir dos campos inseridos pelo usuário.
     */
    private fun updateAdModelFromViewInfo(){
        // Cria um objeto Address com as informações inseridas pelo usuário
        val address = Address(
            cep = binding.cepEt.masked.toString(),
            number = binding.numberEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0,
            street = binding.streetEt.text.toString(),
            nb = binding.nbEt.text.toString(),
            city = binding.cityEt.text.toString(),
            state = binding.stateEt.text.toString(),
        )

        // Associa o endereço ao anúncio
        args.adInfo.local = address
    }
}