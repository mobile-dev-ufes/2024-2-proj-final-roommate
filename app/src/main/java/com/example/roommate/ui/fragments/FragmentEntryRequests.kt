package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.roommate.R
import com.example.roommate.databinding.FragmentEntryRequestsBinding

/**
 * Fragment responsável por exibir as solicitações de entrada.
 *
 * Este fragmento inflará a interface de usuário do layout [R.layout.fragment_entry_requests],
 * onde o usuário pode visualizar ou interagir com as solicitações de entrada, conforme
 * necessário. A interação com a interface de usuário pode ser configurada no método [onViewCreated].
 *
 * A classe usa o [FragmentEntryRequestsBinding] para fazer o binding da interface com os
 * elementos de UI no layout XML.
 */
class FragmentEntryRequests : Fragment(R.layout.fragment_entry_requests) {
    // Instância do binding
    private lateinit var binding: FragmentEntryRequestsBinding

    /**
     * Método chamado para inflar a interface de usuário do fragmento.
     *
     * Aqui, o layout [R.layout.fragment_entry_requests] é inflado e o binding é inicializado
     * para que os elementos de UI possam ser acessados e manipulados programaticamente.
     *
     * @param inflater O [LayoutInflater] utilizado para inflar a view.
     * @param container O [ViewGroup] pai do fragmento, onde a view será anexada.
     * @param savedInstanceState O estado salvo da instância do fragmento, se disponível.
     *
     * @return A [View] raiz do fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Infla o layout e inicializar o binding
        binding = FragmentEntryRequestsBinding.inflate(inflater, container, false)

        return binding.root
    }

    /*
    * O código comentado abaixo poderia ser usado para configurar um botão
    * que leva o usuário a outro fragmento (ex: Fragmento de grupos de interesse).
    *
    * Descomente o código quando for necessário implementar a navegação para
    * outro fragmento.
    */
    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.adGroupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentAdvertisement_to_fragmentInterestedGroups)
        }

    } */
}