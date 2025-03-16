package com.example.roommate.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

class FragmentSignup2 : Fragment(R.layout.fragment_signup2), View.OnClickListener {
    private lateinit var binding: FragmentSignup2Binding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var imageView: ImageView
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    private val args: FragmentSignup2Args by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permissão concedida com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Permissão negada. Não será possível selecionar imagens.", Toast.LENGTH_SHORT).show()
                }
            }

        pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let {
                imageView.setImageURI(it)
                args.userInfo.photo_uri = it.toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentSignup2Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserInfo()

        imageView = binding.profileImage

        binding.signup2GoBtn.setOnClickListener(this)
        binding.editImgL.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.signup2_go_btn) {
            args.userInfo.bio = binding.userBioEt.text.toString()

            val action = FragmentSignup2Directions.actionFragmentSignup2ToFragmentSignup3(args.userInfo)
            findNavController().navigate(action)

        } else if(view.id == R.id.edit_img_l){
            checkAccessPermission()
        }
    }

    private fun setUserInfo() {
        binding.usernameEt.setText(args.userInfo.name)
        binding.userPhoneEt.setText(args.userInfo.phone)
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