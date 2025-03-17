package com.example.roommate.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.roommate.data.model.AdModel
import com.example.roommate.databinding.FragmentCreateAd1Binding
import com.example.roommate.utils.userManager

/**
 * Fragmento responsável pela criação do primeiro passo de um anúncio, onde o usuário insere
 * informações básicas, como título, valor do aluguel, descrição e fotos do imóvel.
 *
 * O [FragmentCreateAd1] permite ao usuário inserir detalhes essenciais de um anúncio, como título,
 * valor de aluguel, valor de condomínio, quantidade de clientes e descrição. Além disso, o fragmento
 * possibilita o upload de imagens para o anúncio.
 *
 * O fragmento valida e coleta essas informações e, em seguida, navega para o próximo passo
 * do processo de criação de anúncio [FragmentCreateAd2].
 */
class FragmentCreateAd1 : Fragment(R.layout.fragment_create_ad1) {

    // Binding da view do fragmento, que acessa os elementos da interface
    private lateinit var binding: FragmentCreateAd1Binding

    // Launcher para solicitar permissão de acesso à mídia (fotos)
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    // Launcher para selecionar uma imagem da galeria
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    // Argumentos passados para o fragmento, que contêm dados sobre a navegação
    private val args: FragmentCreateAd1Args by navArgs()

    // Objeto AdModel que armazena as informações do anúncio
    private var ad = AdModel()

    /**
     * Método chamado para configurar os listeners para o lançamento de permissões e seleção de
     *  imagem.
     * A permissão é solicitada para acessar a galeria de mídia e as imagens selecionadas são
     *  adicionadas à lista de fotos do anúncio.
     *
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configura o launcher para solicitar permissão de acesso à mídia
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

        // Configura o launcher para selecionar imagens da galeria
        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                ad.photos.add(it.toString())
                binding.qttPhotosTv.text = ad.photos.size.toString()
            }
        }
    }

    /**
     * Método chamado para inflar a view do fragmento e inicializar a interface.
     *
     * @param inflater O objeto LayoutInflater usado para inflar a view.
     * @param container O container no qual a view será colocada.
     * @param savedInstanceState O estado salvo da Activity, caso haja.
     * @return A raiz da view inflada.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        // Inicializa o binding da fragmento
        binding = FragmentCreateAd1Binding.inflate(inflater, container, false)

        // Inicializa informação na tela
        binding.qttPhotosTv.text = ad.photos.size.toString()

        return binding.root
    }

    /**
     * Método chamado quando a view foi criada e está pronta para interagir.
     * Configura os listeners para navegação e seleção de imagens.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura o clique do botão para prosseguir para a próxima etapa
        binding.ad1ProceedBtn.setOnClickListener {
            updateAdFromViewInfo()
            val action = FragmentCreateAd1Directions.actionFragmentCreateAd1ToFragmentCreateAd2(ad, args.route)
            findNavController().navigate(action)
        }

        // Configura o clique para adicionar fotos ao anúncio
        binding.addPhotosL.setOnClickListener{
            checkAccessPermission()
        }
    }

    /**
     * Atualiza o objeto de anúncio com as informações inseridas na interface de usuário.
     * Isso inclui título, valores de aluguel, descrição e quantidade de clientes.
     */
    private fun updateAdFromViewInfo(){
        ad.owner = userManager.user.email
        ad.title = binding.titleEt.text.toString()
        ad.rent_value =binding.rentEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
        ad.cond_value = binding.condEt.text.toString().takeIf { it.isNotEmpty() }?.toDouble() ?: 0.0
        ad.max_client = binding.clientsEt.text.toString().takeIf { it.isNotEmpty() }?.toLong() ?: 0
        ad.description = binding.descriptionEt.text.toString()
        ad.suite_qtt = null
        ad.bedroom_qtt = null
        ad.parking_qtt = null
        ad.area = null
        ad.local = null
        ad.groups = arrayOf() // Adiciona um array vazio de grupos
    }

    /**
     * Verifica se o aplicativo tem permissão para acessar a mídia (fotos) do dispositivo.
     * Caso não tenha, solicita a permissão ao usuário.
     */
    private fun checkAccessPermission(){
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                manageImage()  // Se a permissão for concedida, gerencia a seleção de imagens
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_MEDIA_LOCATION) // Solicita a permissão
            }
        }
    }

    /**
     * Inicia o processo de seleção de uma imagem da galeria de mídia do dispositivo.
     */
    private fun manageImage(){
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}