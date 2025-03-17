package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roommate.R
import com.example.roommate.databinding.FragmentMyGroupsBinding
import com.example.roommate.ui.adapters.ListMyGroupAdapter
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.UserViewModel

/**
 * Fragment que exibe a lista de grupos aos quais o usuário pertence.
 *
 * Este fragmento é responsável por carregar e exibir os grupos do usuário
 * através do ViewModel `UserViewModel` e permite a navegação para a tela
 * de detalhes de um grupo selecionado.
 *
 * @see [UserViewModel] Responsável por obter os grupos do usuário.
 * @see [ListMyGroupAdapter] Adaptador utilizado para exibir a lista de grupos.
 */
class FragmentMyGroups : Fragment(R.layout.fragment_my_groups) {
    private lateinit var binding: FragmentMyGroupsBinding
    private lateinit var adapter: ListMyGroupAdapter
    private val userViewModel: UserViewModel by viewModels()

    /**
     * Cria a view do fragmento e inicializa as dependências necessárias.
     *
     * Este método é chamado para inflar a view do fragmento e configurar o
     * ViewModel, o adaptador e a navegação para a tela de detalhes de um grupo.
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

        // Infla a view associada ao fragmento.
        binding = FragmentMyGroupsBinding.inflate(inflater, container, false)

        // Configura o adaptador para a lista de grupos.
        adapter = ListMyGroupAdapter { group ->
            // Ação de navegação para a tela de detalhes do grupo.
            val action = FragmentMyGroupsDirections
                .actionFragmentMyGroupsToFragmentGroup(group)
            findNavController().navigate(action)
        }

        return binding.root
    }

    /**
     * Chamado após a view ser criada. Configura o layout da lista e solicita os dados do usuário.
     *
     * Neste método, o RecyclerView é configurado e o ViewModel é utilizado para obter os grupos
     * do usuário. O observador de grupos também é configurado para atualizar a UI quando a lista
     * de grupos for carregada.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView para exibir os grupos.
        binding.recycleListGroups.layoutManager = LinearLayoutManager(context)
        binding.recycleListGroups.adapter = adapter

        // Solicita os grupos do usuário a partir do UserViewModel.
        userViewModel.getGroupsForUser(userManager.user.email.toString())

        // Configura o observador para a lista de grupos.
        observerGroups()
    }

    /**
     * Configura o observador para a lista de grupos do usuário.
     *
     * Este método configura o observador para os grupos do ViewModel. Quando a lista de grupos é
     * atualizada, o adaptador é notificado e a UI é atualizada automaticamente.
     */
    private fun observerGroups() {
        userViewModel.groups.observe(viewLifecycleOwner) { groups ->
            adapter.updateGroupList(groups.toMutableList())
        }
    }
}
