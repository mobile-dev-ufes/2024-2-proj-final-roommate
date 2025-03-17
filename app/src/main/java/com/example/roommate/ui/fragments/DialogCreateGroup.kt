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

class DialogCreateGroup: DialogFragment(R.layout.dialog_create_group) {
    private lateinit var binding: DialogCreateGroupBinding
    private lateinit var groupViewModel: GroupViewModel

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var imageView: ImageView
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    private val args: DialogCreateGroupArgs by navArgs()

    private var photo_uri = String()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                photo_uri = it.toString()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogCreateGroupBinding.inflate(inflater, container, false)
        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = binding.profileImage

        binding.imageL.setOnClickListener{
            checkAccessPermission()
        }

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

            groupViewModel.registerGroup(group, onSuccess = {
                // Navega apenas após o grupo ser registrado com sucesso
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.fragmentInterestedGroups, true)
                    .build()

                val action = DialogCreateGroupDirections
                    .actionDialogCreateGroupToFragmentInterestedGroups(args.advertisementId)

                findNavController().navigate(action, navOptions)
            }, onFailure = { error ->
                // Exibe mensagem se houver erro
                Toast.makeText(requireContext(), "Erro ao criar grupo: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // This removes the default dark background
    }

    private fun checkAccessPermission(){
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                manageImage()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_MEDIA_LOCATION)
            }
        }
    }

    private fun manageImage(){
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}