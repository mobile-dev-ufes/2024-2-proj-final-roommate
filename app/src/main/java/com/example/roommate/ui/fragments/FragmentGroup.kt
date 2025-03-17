package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.FragmentGroupBinding
import com.example.roommate.ui.adapters.ListMemberAdapter
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.GroupViewModel

/**
 * Fragmento responsável por exibir as informações de um grupo, seus membros e permitir ações de interação.
 *
 * O [FragmentGroup] exibe detalhes sobre o grupo, incluindo nome, descrição, quantidade de membros
 * e a imagem do grupo. Ele também permite que o usuário veja o perfil de outros membros e participe
 * do grupo.
 * O fragmento utiliza um [RecyclerView] para listar os membros do grupo e navega para o perfil do
 * usuário ao clicar em um membro. Além disso, o fragmento fornece a funcionalidade para o usuário
 * ingressar no grupo.
 */
class FragmentGroup : Fragment(R.layout.fragment_group) {

    // Binding da view do fragmento
    private lateinit var binding: FragmentGroupBinding

    // Adaptador para exibir a lista de membros do grupo no RecyclerView
    private lateinit var adapter: ListMemberAdapter

    // ViewModel responsável pelo gerenciamento das informações do grupo
    private val groupViewModel: GroupViewModel by viewModels()

    // Argumentos passados para o fragmento via Safe Args, incluindo informações do grupo
    private val args: FragmentGroupArgs by navArgs()

    /**
     * Método chamado para inflar a view do fragmento e inicializar o adaptador e ViewModel.
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
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        // Inicializa o adaptador para a lista de membros, configurando as ações de clique
        adapter = ListMemberAdapter { user ->
            val action = FragmentGroupDirections
                .actionFragmentGroupToFragmentVisitProfile(user) // ✅ Use Safe Args
            findNavController().navigate(action)
        }
        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para ser interagida.
     *
     * - Exibe as informações do grupo (nome, descrição, quantidade de membros).
     * - Configura a navegação para que o usuário entre no grupo.
     * - Configura o RecyclerView para exibir os membros do grupo.
     * - Observa as mudanças nas informações do grupo e membros.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o RecyclerView com o LayoutManager e o adaptador
        binding.recycleListAds.layoutManager = LinearLayoutManager(context)
        binding.recycleListAds.adapter = adapter

        // Obtém os dados do grupo passados como argumentos
        val argsGroup = args.group

        // Exibe as informações do grupo na interface
        binding.groupNameTv.text = argsGroup.name
        binding.groupDescriptionTv.text = argsGroup.description
        binding.groupQttMembersTv.text = getString(R.string.show_members_qtt, argsGroup.qttMembers.toString())

        // Obtém os membros do grupo
        groupViewModel.getMembersFromGroup(argsGroup.id)

        // Configura o botão para o usuário entrar no grupo
        val userEmail = userManager.user.email.toString()
        binding.groupGetInBtn.setOnClickListener {
            // Chama a lógica para ingressar no grupo
            groupViewModel.groupEntryLogic(argsGroup.id, userEmail)
        }

        // Observa a imagem do grupo e a carrega utilizando o Glide
        val groupId = args.group.id
        groupViewModel.groupImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.error_profile)
                .into(binding.profileImage)
        }
        // Carrega a imagem do grupo
        groupViewModel.loadGroupImage(groupId)

        // Observa as mudanças nos membros do grupo e atualiza o adaptador
        observerGroups()
    }

    /**
     * Método que observa as mudanças na lista de membros do grupo e atualiza o adaptador com os novos membros.
     */
    private fun observerGroups() {
        groupViewModel.members.observe(viewLifecycleOwner) { members ->
            adapter.updateMemberList(members.toMutableList())
        }
    }
}