package com.example.roommate.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.databinding.FragmentSignup2Binding


/**
 * Fragmento responsável pela segunda etapa do processo de cadastro do usuário.
 *
 * O [FragmentSignup2] permite que o usuário adicione uma biografia e escolha uma foto de perfil
 * durante o processo de cadastro. O fragmento gerencia a interação do usuário com os campos
 * de texto e a seleção de uma imagem da galeria.
 *
 * O fragmento também verifica as permissões necessárias para acessar a galeria de imagens
 * e lida com o lançamento de imagens para a escolha da foto de perfil.
 */
class FragmentSignup2 : Fragment(R.layout.fragment_signup2), View.OnClickListener {
    // Binding da view do fragmento, utilizado para acessar os elementos da interface
    private lateinit var binding: FragmentSignup2Binding

    // Lançador de permissão para acessar a mídia do dispositivo
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    // ImageView para exibir a foto de perfil
    private lateinit var imageView: ImageView
    // Lançador para selecionar uma imagem da galeria
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    // Argumentos passados do fragmento anterior contendo as informações do usuário
    private val args: FragmentSignup2Args by navArgs()

    /**
     * Método chamado quando o fragmento é criado. Este método inicializa os lançadores de
     * permissão e a seleção de imagem para a foto de perfil do usuário.
     *
     * @param savedInstanceState O estado salvo do fragmento, se disponível.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lançador para a solicitação de permissão de acesso à mídia
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    manageImage()
                    // Toast.makeText(requireContext(), "Permissão concedida com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permissão negada. Não será possível selecionar imagens.", Toast.LENGTH_SHORT).show()
                }
            }

        // Lançador para selecionar uma imagem da galeria
        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                args.userInfo.photo_uri = it.toString()
            }
        }
    }

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
        binding = FragmentSignup2Binding.inflate(inflater, container, false)

        return binding.root
    }


    /**
     * Método chamado quando a view do fragmento foi criada e está pronta para interação.
     *
     * Este método configura os dados do usuário, os listeners de clique e inicializa a imagem de
     * perfil.
     *
     * @param view A view recém-criada do fragmento.
     * @param savedInstanceState O estado salvo da instância do fragmento.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preenche os campos de nome e telefone com as informações passadas do fragmento anterior
        setUserInfo()

        // Inicializa o ImageView para a foto de perfil
        imageView = binding.profileImage

        // Configura os listeners para os botões de navegação e edição de imagem
        binding.signup2GoBtn.setOnClickListener(this)
        binding.editImgL.setOnClickListener(this)
    }

    /**
     * Método de callback para os cliques nos botões da interface.
     *
     * O comportamento do clique é determinado pelo id da view clicada:
     * - Ao clicar no botão de navegação, as informações da biografia são salvas e a navegação
     *   para a próxima etapa do cadastro é realizada.
     * - Ao clicar no botão de editar imagem, o processo de seleção de imagem é iniciado.
     *
     * @param view A view que foi clicada.
     */
    override fun onClick(view: View) {
        // Verifica se clicou no botão de prosseguit
        if (view.id == R.id.signup2_go_btn) {
            // Atualiza a biografia do usuário e navega para o próximo fragmento
            args.userInfo.bio = binding.userBioEt.text.toString()
            val action = FragmentSignup2Directions.actionFragmentSignup2ToFragmentSignup3(args.userInfo)
            findNavController().navigate(action)

        // Verifica se clicou no botão de carregar imagem
        } else if(view.id == R.id.edit_img_l){
            // Verifica e solicita a permissão para acessar a mídia
            checkAccessPermission()
        }
    }

    /**
     * Preenche os campos de nome e telefone com as informações passadas dos fragmentos anteriores.
     */
    private fun setUserInfo() {
        binding.usernameEt.setText(args.userInfo.name)
        binding.userPhoneEt.setText(args.userInfo.phone)
    }

    /**
     * Verifica se a permissão de acesso à mídia foi concedida. Se concedida, inicia o processo
     * de seleção de imagem. Caso contrário, solicita a permissão ao usuário.
     */
    private fun checkAccessPermission(){
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                manageImage() // Se a permissão for concedida, gerencia a seleção de imagens
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_MEDIA_LOCATION) // Solicita a permissão
            }
        }
    }

    /**
     * Inicia o processo de seleção de uma imagem da galeria. O usuário pode selecionar apenas imagens.
     */
    private fun manageImage(){
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}