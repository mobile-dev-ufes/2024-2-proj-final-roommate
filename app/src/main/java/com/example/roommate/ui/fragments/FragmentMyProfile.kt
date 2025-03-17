package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.data.repository.UserRepository
import com.example.roommate.databinding.FragmentMyProfileBinding
import com.example.roommate.utils.userManager
import com.example.roommate.viewModel.UserViewModel

/**
 * Fragment que exibe o perfil do usuário, incluindo informações pessoais e imagem de perfil.
 *
 * Este fragmento é responsável por mostrar o nome, telefone, bio e imagem de perfil do usuário.
 * Também permite navegar para outras telas, como os anúncios do usuário e seus anúncios favoritos.
 *
 * @see [UserViewModel] Responsável por fornecer a URL da imagem de perfil.
 * @see [UserRepository] Responsável pela interação com os dados do usuário.
 */
class FragmentMyProfile : Fragment(R.layout.fragment_signup3) {
    private lateinit var binding: FragmentMyProfileBinding
    private lateinit var userRepository: UserRepository
    private lateinit var userViewModel: UserViewModel

    /**
     * Cria a view do fragmento e inicializa as dependências necessárias.
     *
     * Este método é chamado para inflar a view do fragmento e configurar as interações
     * de navegação para as telas de anúncios e anúncios favoritos.
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
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Chamado após a view ser criada. Configura as interações da UI e carrega as informações do usuário.
     *
     * Neste método, são configurados os cliques nos botões para navegar para as telas de anúncios
     * e anúncios favoritos. Além disso, é inicializado o `UserRepository` e o `UserViewModel` para
     * carregar as informações do perfil do usuário.
     *
     * @param view A view do fragmento.
     * @param savedInstanceState O estado salvo da instância anterior, se existir.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navega para a tela de anúncios do usuário quando o botão é clicado.
        binding.myAdsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMyProfile_to_fragmentMyAds)
        }

        // Navega para a tela de anúncios favoritos do usuário quando o botão é clicado.
        binding.myFavoriteAdsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMyProfile_to_fragmentFavoriteAds)
        }

        // Inicializa o UserRepository e o UserViewModel.
        userRepository = UserRepository()
        userViewModel = UserViewModel()

        // Carrega as informações do usuário.
        setInfos()
    }

    /**
     * Configura as informações do perfil do usuário na UI.
     *
     * Este método é responsável por preencher os campos de nome, telefone, bio e a imagem de perfil
     * do usuário. A imagem de perfil é carregada utilizando a URL fornecida pelo `UserViewModel`.
     */
    private fun setInfos(){
        // Define o nome, telefone e bio do usuário na UI.
        binding.usernameTv.text = userManager.user.name
        binding.userPhoneTv.text = userManager.user.phone
        binding.userBioTv.text = userManager.user.bio

        // Observa a URL da imagem de perfil do usuário e carrega a imagem usando Glide.
        val userId = userManager.user.email.toString()

        userViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder)  // Placeholder enquanto a imagem não carrega.
                .error(R.drawable.error_profile)  // Imagem de erro, caso falhe ao carregar.
                .into(binding.profileImage)  // Exibe a imagem de perfil no ImageView.
        }

        // Carrega a imagem de perfil do usuário usando o ID do usuário.
        userViewModel.loadProfileImage(userId)
    }
}