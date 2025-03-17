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
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.roommate.R
import com.example.roommate.data.model.GroupModel
import com.example.roommate.databinding.DialogCreateGroupBinding
import com.example.roommate.viewModel.GroupViewModel

/**
 * Fragment de dialog responsável pela criação de um novo grupo.
 *
 * A [DialogCreateGroup] permite ao usuário criar um grupo, fornecendo informações como nome,
 *  descrição, privacidade e imagem do grupo. O grupo é registrado no banco de dados e o usuário é
 *  redirecionado para a tela de grupos interessados após o sucesso da criação.
 *
 * O fragmento também gerencia a permissão de acesso à mídia para permitir que o usuário selecione
 * uma imagem para o grupo. A seleção da imagem só é possível após a permissão ser concedida.
 */
class DialogCreateGroup: DialogFragment(R.layout.dialog_create_group) {
    // Binding da Activity para acessar os elementos da interface
    private lateinit var binding: DialogCreateGroupBinding

    // ViewModel responsável pelo gerenciamento dos grupos
    private lateinit var groupViewModel: GroupViewModel

    // Launcher para solicitar permissão de acesso à mídia
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    // ImageView para exibir a imagem do grupo
    private lateinit var imageView: ImageView
    // Launcher para selecionar a imagem da galeria
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>
    // URI da foto selecionada para o grupo
    private var photo_uri = String()

    // Argumentos passados para o Fragmento
    private val args: DialogCreateGroupArgs by navArgs()

    /**
     * Método chamado para configurar as permissões e inicializar os lançadores de atividades.
     *
     * - Configura o lançador de permissão para acesso à mídia.
     * - Configura o lançador para selecionar uma imagem da galeria.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o lançador de permissão para acessar a mídia
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    manageImage()
                    // Toast.makeText(requireContext(), "Permissão concedida com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    // Exibe mensagem quando a permissão for negada
                    Toast.makeText(requireContext(), "Permissão negada. Não será possível selecionar imagens.", Toast.LENGTH_SHORT).show()
                }
            }

        // Inicializa o lançador para selecionar a imagem da galeria
        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                photo_uri = it.toString()
            }
        }
    }

    /**
     * Método chamado para inflar a view do fragmento e configurar o ViewModel.
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

        // Realiza o binding
        binding = DialogCreateGroupBinding.inflate(inflater, container, false)
        // Instancia o View Model
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        return binding.root
    }

    /**
     * Método chamado quando a view do fragmento foi criada.
     *
     * Configura os listeners de eventos para os botões e a imagem do grupo.
     *
     * - Verifica a permissão para selecionar uma imagem quando o usuário clica na área da imagem.
     * - Registra o grupo quando o botão de criação de grupo é pressionado.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura a referência para o ImageView da imagem do grupo
        imageView = binding.profileImage

        // Configura o clique na área da imagem para selecionar uma nova imagem
        binding.imageL.setOnClickListener{
            checkAccessPermission()
        }

        // Configura o clique no botão para criar o grupo
        binding.createGroupBtn.setOnClickListener {
            val group = GroupModel(
                id = "",
                name = binding.createGroupNameTv.text.toString(),
                description = binding.createGroupDescriptionTv.text.toString(),
                advertisementId = args.advertisementId,
                qttMembers = 0,
                isPrivate = binding.switchGroup.isChecked,
                photoUri = photo_uri
            )

            // Registra o grupo no ViewModel
            groupViewModel.registerGroup(group, onSuccess = {
                // Navega apenas após o grupo ser registrado com sucesso
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.fragmentInterestedGroups, true)
                    .build()

                val action = DialogCreateGroupDirections
                    .actionDialogCreateGroupToFragmentInterestedGroups(args.advertisementId)

                findNavController().navigate(action, navOptions)
            }, onFailure = { error ->
                // Exibe mensagem de erro caso o registro do grupo falhe
                Toast.makeText(requireContext(), "Erro ao criar grupo: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    /**
     * Método chamado para configurar a largura e altura do diálogo.
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // This removes the default dark background
    }

    /**
     * Verifica a permissão de acesso à mídia do dispositivo.
     * Se a permissão for concedida, chama o método para selecionar uma imagem.
     */
    private fun checkAccessPermission(){
        when {
            // Verifica se a permissão foi concedida
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                manageImage()
            }
            else -> {
                // Solicita a permissão de acesso à mídia
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_MEDIA_LOCATION)
            }
        }
    }

    /**
     * Lança a seleção de uma imagem da galeria do dispositivo.
     */
    private fun manageImage(){
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}