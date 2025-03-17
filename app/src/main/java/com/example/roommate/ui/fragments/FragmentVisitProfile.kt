package com.example.roommate.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.roommate.R
import com.example.roommate.databinding.FragmentVisitProfileBinding
import com.example.roommate.viewModel.UserViewModel

/**
 * Fragmento responsável por exibir o perfil de outro usuário.
 *
 * O [FragmentVisitProfile] exibe informações do perfil de um usuário que foi selecionado, como nome,
 * bio e número de telefone. Além disso, carrega a imagem do perfil do usuário utilizando a
 * biblioteca Glide.
 * O fragmento recebe as informações do usuário a partir dos argumentos passados pela navegação.
 */
class FragmentVisitProfile : Fragment(R.layout.fragment_visit_profile) {

    // Binding da view do fragmento, utilizado para acessar os elementos da interface
    private lateinit var binding: FragmentVisitProfileBinding

    // Argumentos passados para o fragmento contendo as informações do usuário
    private val args: FragmentVisitProfileArgs by navArgs()

    // ViewModel responsável pela gestão de dados do usuário, como a imagem do perfil
    private lateinit var userViewModel: UserViewModel

    /**
     * Método chamado para inflar a view do fragmento e inicializar o binding.
     *
     * Este método infla o layout do fragmento e retorna a raiz da view.
     *
     * @param inflater O objeto [LayoutInflater] usado para inflar a view.
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

        // Inicializa o binding para acessar os elementos da interface do fragmento
        binding = FragmentVisitProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para ser interagida.
     *
     * Este método configura a interface do usuário com as informações passadas nos argumentos, como
     * nome, bio e telefone. Também carrega a imagem de perfil do usuário utilizando o [UserViewModel].
     *
     * @param view A view recém-criada do fragmento.
     * @param savedInstanceState O estado salvo da instância do fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa o ViewModel responsável pelos dados do usuário
        userViewModel = UserViewModel()

        // Obtém o objeto 'user' dos argumentos passados para o fragmento
        val argsUser = args.user

        // Define os textos na interface com as informações do usuário
        binding.usernameTv.text = argsUser.name
        binding.userBioTv.text = argsUser.bio
        binding.userPhoneTv.text = argsUser.phone

        // Obtém o ID do usuário, utilizado para carregar a imagem do perfil
        val userId = argsUser.email.toString()

        // Observa a URL da imagem do perfil e carrega a imagem com Glide
        userViewModel.profileImageUrl.observe(viewLifecycleOwner) { url ->
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.profile_placeholder) // Placeholder enquanto a imagem não carrega
                .error(R.drawable.error_profile) // Imagem a ser exibida em caso de erro ao carregar
                .into(binding.profileImage) // Define a imagem carregada no ImageView
        }

        // Chama o método para carregar a imagem do perfil utilizando o e-mail do usuário
        userViewModel.loadProfileImage(userId)
    }
}